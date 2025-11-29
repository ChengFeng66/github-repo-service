package com.github.githubreposervice.service.impl;

import com.github.githubreposervice.common.exception.GitHubApiException;
import com.github.githubreposervice.common.exception.RepoNotFoundException;
import com.github.githubreposervice.common.result.ResultCode;
import com.github.githubreposervice.dto.GitHubRepoDTO;
import com.github.githubreposervice.dto.GitHubRepoResponse;
import com.github.githubreposervice.entity.GitHubRepo;
import com.github.githubreposervice.mapper.GitHubRepoMapper;
import com.github.githubreposervice.service.GitHubRepoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import jakarta.annotation.Resource;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * GitHub repository service implementation class
 */
@Slf4j
@Service
public class GitHubRepoServiceImpl implements GitHubRepoService {
    
    private final WebClient webClient;
    
    @Resource
    private GitHubRepoMapper gitHubRepoMapper;
    
    @Value("${github.api.base-url:https://api.github.com}")
    private String githubApiBaseUrl;
    
    @Value("${github.api.timeout:10}")
    private Integer timeout;
    
    @Value("${cache.expire-minutes:30}")
    private Integer cacheExpireMinutes;
    
    public GitHubRepoServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }
    
    @Override
    public GitHubRepoDTO getRepoInfo(String owner, String repo) {
        // Parameter validation
        validateParams(owner, repo);
        
        String fullName = owner + "/" + repo;
        log.info("Starting to get repository information: {}", fullName);
        
        // 1. First query from database cache
        GitHubRepo cachedRepo = gitHubRepoMapper.selectByOwnerAndRepo(owner, repo);
        
        // 2. Check if cache is valid
        if (cachedRepo != null && isCacheValid(cachedRepo.getCacheTime())) {
            log.info("Get repository information from database cache: {}", fullName);
            return convertEntityToDTO(cachedRepo);
        }
        
        // 3. Cache invalid or not exists, call GitHub API
        log.info("Cache invalid or not exists, calling GitHub API to get repository information: {}", fullName);
        GitHubRepoResponse apiResponse = fetchFromGitHubApi(owner, repo);
        
        // 4. Save or update to database
        GitHubRepo repoEntity = convertResponseToEntity(apiResponse, owner, repo);
        saveOrUpdateRepo(repoEntity, cachedRepo);
        
        // 5. Return result
        return convertEntityToDTO(repoEntity);
    }
    
    /**
     * Parameter validation
     */
    private void validateParams(String owner, String repo) {
        if (!StringUtils.hasText(owner)) {
            throw new IllegalArgumentException("Repository owner cannot be empty");
        }
        if (!StringUtils.hasText(repo)) {
            throw new IllegalArgumentException("Repository name cannot be empty");
        }
        
        // Simple parameter format validation
        if (!owner.matches("^[a-zA-Z0-9-]+$")) {
            throw new IllegalArgumentException("Repository owner format is incorrect");
        }
        if (!repo.matches("^[a-zA-Z0-9._-]+$")) {
            throw new IllegalArgumentException("Repository name format is incorrect");
        }
    }
    
    /**
     * Call GitHub API to get repository information
     */
    private GitHubRepoResponse fetchFromGitHubApi(String owner, String repo) {
        String url = String.format("%s/repos/%s/%s", githubApiBaseUrl, owner, repo);
        log.info("Calling GitHub API: {}", url);
        
        try {
            GitHubRepoResponse response = webClient.get()
                    .uri(url)
                    .header("Accept", "application/vnd.github.v3+json")
                    .header("User-Agent", "GitHub-Repo-Service")
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                        if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                            log.warn("Repository not found: {}/{}", owner, repo);
                            return Mono.error(new RepoNotFoundException(owner + "/" + repo));
                        }
                        if (clientResponse.statusCode() == HttpStatus.FORBIDDEN) {
                            log.error("GitHub API request rate limit exceeded");
                            return Mono.error(new GitHubApiException(ResultCode.GITHUB_RATE_LIMIT));
                        }
                        log.error("GitHub API request failed, status code: {}", clientResponse.statusCode());
                        return Mono.error(new GitHubApiException("GitHub API request failed: " + clientResponse.statusCode()));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
                        log.error("GitHub API server error, status code: {}", clientResponse.statusCode());
                        return Mono.error(new GitHubApiException("GitHub API server error"));
                    })
                    .bodyToMono(GitHubRepoResponse.class)
                    .timeout(Duration.ofSeconds(timeout))
                    .block();
            
            if (response == null) {
                log.error("GitHub API returned empty data");
                throw new GitHubApiException("GitHub API returned empty data");
            }
            
            log.info("Successfully got repository information from GitHub API: {}", response.getFullName());
            return response;
            
        } catch (RepoNotFoundException | GitHubApiException e) {
            throw e;
        } catch (Exception e) {
            log.error("GitHub API call exception: {}", e.getMessage(), e);
            throw new GitHubApiException("GitHub API call failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if cache is valid
     */
    private boolean isCacheValid(LocalDateTime cacheTime) {
        if (cacheTime == null) {
            return false;
        }
        LocalDateTime expireTime = cacheTime.plusMinutes(cacheExpireMinutes);
        boolean isValid = LocalDateTime.now().isBefore(expireTime);
        log.debug("Cache time: {}, expiration time: {}, is valid: {}", cacheTime, expireTime, isValid);
        return isValid;
    }
    
    /**
     * Save or update repository information to database
     */
    private void saveOrUpdateRepo(GitHubRepo newRepo, GitHubRepo existingRepo) {
        if (existingRepo == null) {
            // Insert new
            log.info("Save new repository to database: {}", newRepo.getFullName());
            gitHubRepoMapper.insert(newRepo);
        } else {
            // Update
            log.info("Update repository information to database: {}", newRepo.getFullName());
            newRepo.setId(existingRepo.getId());
            gitHubRepoMapper.updateById(newRepo);
        }
    }
    
    /**
     * Convert GitHub API response to entity object
     */
    private GitHubRepo convertResponseToEntity(GitHubRepoResponse response, String owner, String repo) {
        return GitHubRepo.builder()
                .fullName(response.getFullName())
                .owner(owner)
                .repoName(repo)
                .description(response.getDescription())
                .cloneUrl(response.getCloneUrl())
                .stars(response.getStargazersCount())
                .forks(response.getForksCount())
                .watchers(response.getWatchersCount())
                .repoSize(response.getSize())
                .defaultBranch(response.getDefaultBranch())
                .isPrivate(response.getIsPrivate())
                .htmlUrl(response.getHtmlUrl())
                .createdAt(response.getCreatedAt() != null ? 
                        response.getCreatedAt().toLocalDateTime() : null)
                .updatedAt(response.getUpdatedAt() != null ? 
                        response.getUpdatedAt().toLocalDateTime() : null)
                .pushedAt(response.getPushedAt() != null ? 
                        response.getPushedAt().toLocalDateTime() : null)
                .cacheTime(LocalDateTime.now())
                .build();
    }
    
    /**
     * Convert entity object to DTO
     */
    private GitHubRepoDTO convertEntityToDTO(GitHubRepo entity) {
        return GitHubRepoDTO.builder()
                .fullName(entity.getFullName())
                .description(entity.getDescription())
                .cloneUrl(entity.getCloneUrl())
                .stars(entity.getStars())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
