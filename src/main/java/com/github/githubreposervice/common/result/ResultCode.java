package com.github.githubreposervice.common.result;

import lombok.Getter;

/**
 * Response status code enum
 */
@Getter
public enum ResultCode {
    
    /**
     * Success
     */
    SUCCESS(200, "Operation successful"),
    
    /**
     * Failure
     */
    FAIL(500, "Operation failed"),
    
    /**
     * Parameter error
     */
    PARAM_ERROR(400, "Parameter error"),
    
    /**
     * Not found
     */
    NOT_FOUND(404, "Resource not found"),
    
    /**
     * Internal server error
     */
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    
    /**
     * GitHub API call failed
     */
    GITHUB_API_ERROR(1001, "GitHub API call failed"),
    
    /**
     * Repository not found
     */
    REPO_NOT_FOUND(1002, "Repository not found"),
    
    /**
     * GitHub API rate limit
     */
    GITHUB_RATE_LIMIT(1003, "GitHub API request rate limit exceeded");
    
    private final Integer code;
    private final String message;
    
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
