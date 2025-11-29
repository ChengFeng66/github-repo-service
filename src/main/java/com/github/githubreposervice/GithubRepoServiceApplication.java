package com.github.githubreposervice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.github.githubreposervice.mapper")
public class GithubRepoServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubRepoServiceApplication.class, args);
    }

}
