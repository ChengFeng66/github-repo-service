package com.github.githubreposervice.config;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Logging functionality test
 */
@Slf4j
@SpringBootTest
public class LoggingTest {
    
    @Test
    public void testLoggingLevels() {
        log.trace("This is TRACE level log");
        log.debug("This is DEBUG level log");
        log.info("This is INFO level log");
        log.warn("This is WARN level log");
        log.error("This is ERROR level log");
        
        // Test parameterized logging
        String user = "testUser";
        int count = 100;
        log.info("User {} executed {} operations", user, count);
        
        // Test exception logging
        try {
            throw new RuntimeException("Test exception");
        } catch (Exception e) {
            log.error("Caught exception: {}", e.getMessage(), e);
        }
        
        log.info("Logging test completed");
    }
    
    @Test
    public void testBusinessLog() {
        log.info("========== Business Process Logging Test ==========");
        
        // Simulate business process
        String repoName = "spring-framework/spring-boot";
        log.info("Starting to get repository information: {}", repoName);
        
        log.debug("Checking if exists in cache: {}", repoName);
        log.debug("Cache miss, preparing to call GitHub API");
        
        log.info("Calling GitHub API: https://api.github.com/repos/{}", repoName);
        log.info("Successfully retrieved repository information");
        
        log.debug("Saving to database cache");
        log.info("Business process completed: {}", repoName);
        
        log.info("========== Business Process Logging Test Completed ==========");
    }
}
