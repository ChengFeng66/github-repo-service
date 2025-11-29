package com.github.githubreposervice.controller;

import com.github.githubreposervice.common.result.Result;
import com.github.githubreposervice.dto.GitHubRepoDTO;
import com.github.githubreposervice.service.GitHubRepoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * GitHub repository controller
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/repos")
public class GitHubRepoController {
    
    private final GitHubRepoService gitHubRepoService;
    
    /**
     * Get GitHub repository details
     * 
     * @param owner Repository owner
     * @param repo  Repository name
     * @return Repository details
     */
    @GetMapping("/{owner}/{repo}")
    public Result<GitHubRepoDTO> getRepoInfo(
            @PathVariable 
            @NotBlank(message = "Repository owner cannot be empty")
            @Pattern(regexp = "^[a-zA-Z0-9-]+$", message = "Repository owner format is incorrect")
            String owner,
            
            @PathVariable 
            @NotBlank(message = "Repository name cannot be empty")
            @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Repository name format is incorrect")
            String repo
    ) {
        log.info("Received request to get repository info: owner={}, repo={}", owner, repo);
        
        GitHubRepoDTO repoInfo = gitHubRepoService.getRepoInfo(owner, repo);
        
        log.info("Successfully returned repository info: {}", repoInfo.getFullName());
        return Result.success(repoInfo);
    }
}
