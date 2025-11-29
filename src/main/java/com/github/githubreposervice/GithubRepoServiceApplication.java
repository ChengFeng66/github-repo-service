package com.github.githubreposervice;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication
@MapperScan("com.github.githubreposervice.mapper")
public class GithubRepoServiceApplication {

    public static void main(String[] args) {
        try {
            SpringApplication app = new SpringApplication(GithubRepoServiceApplication.class);
            Environment env = app.run(args).getEnvironment();

            String protocol = "http";
            if (env.getProperty("server.ssl.key-store") != null) {
                protocol = "https";
            }

            String hostAddress = "localhost";
            try {
                hostAddress = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.warn("Failed to get host address, using localhost", e);
            }

            String serverPort = env.getProperty("server.port", "8080");
            String contextPath = env.getProperty("server.servlet.context-path", "");

            log.info("\n----------------------------------------------------------\n\t" + "Application '{}' is running! Access URLs:\n\t" + "Local: \t\t{}://localhost:{}{}\n\t" + "External: \t{}://{}:{}{}\n\t" + "Profile(s): \t{}\n" + "----------------------------------------------------------", env.getProperty("spring.application.name", "github-repo-service"), protocol, serverPort, contextPath, protocol, hostAddress, serverPort, contextPath, env.getActiveProfiles().length == 0 ? "default" : String.join(", ", env.getActiveProfiles()));
        } catch (Exception e) {
            log.error("Failed to start application", e);
            throw e;
        }
    }

}
