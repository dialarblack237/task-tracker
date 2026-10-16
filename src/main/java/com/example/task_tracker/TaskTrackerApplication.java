package com.example.task_tracker;

import com.example.task_tracker.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(JwtProperties.class)
public class TaskTrackerApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskTrackerApplication.class, args);
    }
}