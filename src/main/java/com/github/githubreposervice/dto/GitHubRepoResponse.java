package com.github.githubreposervice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.ZonedDateTime;

/**
 * Raw repository data returned by GitHub API
 */
@Data
public class GitHubRepoResponse {
    
    /**
     * Repository ID
     */
    private Long id;
    
    /**
     * Repository name
     */
    private String name;
    
    /**
     * Repository full name
     */
    @JsonProperty("full_name")
    private String fullName;
    
    /**
     * Repository description
     */
    private String description;
    
    /**
     * Clone URL
     */
    @JsonProperty("clone_url")
    private String cloneUrl;
    
    /**
     * Git URL
     */
    @JsonProperty("git_url")
    private String gitUrl;
    
    /**
     * SSH URL
     */
    @JsonProperty("ssh_url")
    private String sshUrl;
    
    /**
     * Star count
     */
    @JsonProperty("stargazers_count")
    private Integer stargazersCount;
    
    /**
     * Watcher count
     */
    @JsonProperty("watchers_count")
    private Integer watchersCount;
    
    /**
     * Fork count
     */
    @JsonProperty("forks_count")
    private Integer forksCount;
    
    /**
     * Created time
     */
    @JsonProperty("created_at")
    private ZonedDateTime createdAt;
    
    /**
     * Updated time
     */
    @JsonProperty("updated_at")
    private ZonedDateTime updatedAt;
    
    /**
     * Pushed time
     */
    @JsonProperty("pushed_at")
    private ZonedDateTime pushedAt;
    
    /**
     * Repository size
     */
    private Integer size;
    
    /**
     * Default branch
     */
    @JsonProperty("default_branch")
    private String defaultBranch;
    
    /**
     * Is private repository
     */
    @JsonProperty("private")
    private Boolean isPrivate;
    
    /**
     * Homepage URL
     */
    @JsonProperty("html_url")
    private String htmlUrl;
}
