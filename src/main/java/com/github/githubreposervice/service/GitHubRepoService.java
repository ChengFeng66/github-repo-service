package com.github.githubreposervice.service;

import com.github.githubreposervice.dto.GitHubRepoDTO;

/**
 * GitHub repository service interface
 */
public interface GitHubRepoService {
    
    /**
     * Get repository information by owner and repository name
     * 
     * @param owner Repository owner
     * @param repo  Repository name
     * @return Repository information DTO
     */
    GitHubRepoDTO getRepoInfo(String owner, String repo);
}
