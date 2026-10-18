package com.example.task_tracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class TaskTrackerApplicationTests {

    @Test
    void contextLoads() {
        // Empty by design.
        // If the context fails to start (bad config, missing bean,
        // broken Flyway migration, security misconfig), this test fails.
    }
}