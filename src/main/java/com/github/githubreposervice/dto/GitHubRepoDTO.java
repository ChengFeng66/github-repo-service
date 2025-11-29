package com.github.githubreposervice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * GitHub repository information DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubRepoDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Repository full name (owner/repo)
     */
    @JsonProperty("fullName")
    private String fullName;
    
    /**
     * Repository description
     */
    @JsonProperty("description")
    private String description;
    
    /**
     * Repository clone URL
     */
    @JsonProperty("cloneUrl")
    private String cloneUrl;
    
    /**
     * Star count
     */
    @JsonProperty("stars")
    private Integer stars;
    
    /**
     * Created date
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
}
