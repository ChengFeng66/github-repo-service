package com.github.githubreposervice.common.handler;

import com.github.githubreposervice.common.exception.BusinessException;
import com.github.githubreposervice.common.exception.GitHubApiException;
import com.github.githubreposervice.common.exception.RepoNotFoundException;
import com.github.githubreposervice.common.result.Result;
import com.github.githubreposervice.common.result.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Global exception handler
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handle business exception
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleBusinessException(BusinessException e) {
        log.error("Business exception: {}", e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }
    
    /**
     * Handle GitHub API exception
     */
    @ExceptionHandler(GitHubApiException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<Void> handleGitHubApiException(GitHubApiException e) {
        log.error("GitHub API exception: {}", e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }
    
    /**
     * Handle repository not found exception
     */
    @ExceptionHandler(RepoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<Void> handleRepoNotFoundException(RepoNotFoundException e) {
        log.warn("Repository not found: {}", e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }
    
    /**
     * Handle parameter validation exception - @Valid
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("Parameter validation exception: {}", e.getMessage());
        String errorMessage = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), errorMessage);
    }
    
    /**
     * Handle parameter binding exception
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleBindException(BindException e) {
        log.error("Parameter binding exception: {}", e.getMessage());
        String errorMessage = e.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), errorMessage);
    }
    
    /**
     * Handle constraint violation exception - @Validated
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        log.error("Constraint violation exception: {}", e.getMessage());
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), errorMessage);
    }
    
    /**
     * Handle parameter type mismatch exception
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("Parameter type mismatch exception: {}", e.getMessage());
        String errorMessage = String.format("Parameter '%s' has incorrect type", e.getName());
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), errorMessage);
    }
    
    /**
     * Handle IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("Illegal argument exception: {}", e.getMessage(), e);
        return Result.fail(ResultCode.PARAM_ERROR.getCode(), e.getMessage());
    }
    
    /**
     * Handle all uncaught exceptions
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<Void> handleException(Exception e) {
        log.error("System exception: {}", e.getMessage(), e);
        return Result.fail(ResultCode.INTERNAL_SERVER_ERROR.getCode(), 
                          "Internal server error, please contact administrator");
    }
}
