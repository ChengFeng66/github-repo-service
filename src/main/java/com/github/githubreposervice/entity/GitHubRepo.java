package com.github.githubreposervice.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GitHub repository entity class
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("github_repo")
public class GitHubRepo implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Primary key ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * Repository full name (owner/repo)
     */
    @TableField("full_name")
    private String fullName;
    
    /**
     * Repository owner
     */
    @TableField("owner")
    private String owner;
    
    /**
     * Repository name
     */
    @TableField("repo_name")
    private String repoName;
    
    /**
     * Repository description
     */
    @TableField("description")
    private String description;
    
    /**
     * Clone URL
     */
    @TableField("clone_url")
    private String cloneUrl;
    
    /**
     * Star count
     */
    @TableField("stars")
    private Integer stars;
    
    /**
     * Fork count
     */
    @TableField("forks")
    private Integer forks;
    
    /**
     * Watcher count
     */
    @TableField("watchers")
    private Integer watchers;
    
    /**
     * Repository size (KB)
     */
    @TableField("repo_size")
    private Integer repoSize;
    
    /**
     * Default branch
     */
    @TableField("default_branch")
    private String defaultBranch;
    
    /**
     * Is private repository 0-No 1-Yes
     */
    @TableField("is_private")
    private Boolean isPrivate;
    
    /**
     * Repository homepage URL
     */
    @TableField("html_url")
    private String htmlUrl;
    
    /**
     * GitHub repository created time
     */
    @TableField("created_at")
    private LocalDateTime createdAt;
    
    /**
     * GitHub repository updated time
     */
    @TableField("updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * GitHub repository pushed time
     */
    @TableField("pushed_at")
    private LocalDateTime pushedAt;
    
    /**
     * Cache time
     */
    @TableField("cache_time")
    private LocalDateTime cacheTime;
    
    /**
     * Record created time
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * Record updated time
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    /**
     * Logical delete 0-Not deleted 1-Deleted
     */
    @TableLogic
    @TableField("is_deleted")
    private Boolean isDeleted;
}
