package com.github.githubreposervice.common.exception;

import com.github.githubreposervice.common.result.ResultCode;

/**
 * GitHub API exception class
 */
public class GitHubApiException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public GitHubApiException(String message) {
        super(ResultCode.GITHUB_API_ERROR.getCode(), message);
    }
    
    public GitHubApiException(String message, Throwable cause) {
        super(ResultCode.GITHUB_API_ERROR.getCode(), message, cause);
    }
    
    public GitHubApiException(Integer code, String message) {
        super(code, message);
    }
    
    public GitHubApiException(ResultCode resultCode) {
        super(resultCode);
    }
}
