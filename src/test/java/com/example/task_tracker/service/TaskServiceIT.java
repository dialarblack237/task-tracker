package com.example.task_tracker.service;

import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.repository.TaskRepository;
import com.example.task_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class TaskServiceIT {

    @Autowired TaskService taskService;
    @Autowired TaskRepository taskRepository;
    @Autowired UserRepository userRepository;

    @BeforeEach
    void clean() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void create_persistsTaskToDatabase() {
        User owner = new User();
        owner.setEmail("a@example.com");
        owner.setDisplayName("x");
        owner.setPassword("$2a$10$hash");
        owner = userRepository.save(owner);

        TaskResponse res = taskService.create(
                new CreateTaskRequest("Ship v1", null, null), owner);

        assertThat(res.id()).isNotNull();
        assertThat(taskRepository.findAllByOwnerId(owner.getId())).hasSize(1);
    }
}