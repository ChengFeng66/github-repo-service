package com.github.githubreposervice.controller;

import com.github.githubreposervice.common.result.Result;
import com.github.githubreposervice.dto.GitHubRepoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GitHubRepoController集成测试
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GitHubRepoControllerTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    /**
     * 测试获取存在的仓库信息 - 成功场景
     */
    @Test
    void testGetRepoInfo_Success() {
        // 构建请求URL
        String url = String.format("http://localhost:%d/api/repos/spring-projects/spring-boot", port);
        
        // 发送请求
        ResponseEntity<Result<GitHubRepoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<GitHubRepoDTO>>() {}
        );
        
        // 验证响应
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(200);
        assertThat(response.getBody().getData()).isNotNull();
        assertThat(response.getBody().getData().getFullName()).isEqualTo("spring-projects/spring-boot");
        assertThat(response.getBody().getData().getStars()).isGreaterThan(0);
        
        // 打印结果
        System.out.println("=== 测试成功 - 获取仓库信息 ===");
        System.out.println("仓库全名: " + response.getBody().getData().getFullName());
        System.out.println("仓库描述: " + response.getBody().getData().getDescription());
        System.out.println("克隆URL: " + response.getBody().getData().getCloneUrl());
        System.out.println("星标数: " + response.getBody().getData().getStars());
        System.out.println("创建时间: " + response.getBody().getData().getCreatedAt());
    }
    
    /**
     * 测试获取不存在的仓库 - 失败场景
     */
    @Test
    void testGetRepoInfo_RepoNotFound() {
        // 构建请求URL - 使用不存在的仓库
        String url = String.format("http://localhost:%d/api/repos/invalid-owner-12345/invalid-repo-67890", port);
        
        // 发送请求
        ResponseEntity<Result<GitHubRepoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<GitHubRepoDTO>>() {}
        );
        
        // 验证响应
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(1002);
        assertThat(response.getBody().getMessage()).contains("不存在");
        
        // 打印结果
        System.out.println("=== 测试成功 - 仓库不存在场景 ===");
        System.out.println("错误码: " + response.getBody().getCode());
        System.out.println("错误信息: " + response.getBody().getMessage());
    }
    
    /**
     * 测试参数格式错误 - 失败场景
     */
    @Test
    void testGetRepoInfo_InvalidParameter() {
        // 构建请求URL - 使用非法字符
        String url = String.format("http://localhost:%d/api/repos/invalid@owner/repo", port);
        
        // 发送请求
        ResponseEntity<Result<GitHubRepoDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Result<GitHubRepoDTO>>() {}
        );
        
        // 验证响应
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo(400);
        
        // 打印结果
        System.out.println("=== 测试成功 - 参数格式错误场景 ===");
        System.out.println("错误码: " + response.getBody().getCode());
        System.out.println("错误信息: " + response.getBody().getMessage());
    }
}
