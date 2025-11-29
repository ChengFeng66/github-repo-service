package com.github.githubreposervice.common.exception;

import com.github.githubreposervice.common.result.ResultCode;

/**
 * Repository not found exception
 */
public class RepoNotFoundException extends BusinessException {
    
    private static final long serialVersionUID = 1L;
    
    public RepoNotFoundException(String repoFullName) {
        super(ResultCode.REPO_NOT_FOUND.getCode(), 
              String.format("GitHub repository not found: %s", repoFullName));
    }
    
    public RepoNotFoundException(String message, Throwable cause) {
        super(ResultCode.REPO_NOT_FOUND.getCode(), message, cause);
    }
}
