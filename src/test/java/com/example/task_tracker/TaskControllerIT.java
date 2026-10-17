package com.example.task_tracker;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.repository.TaskRepository;
import com.example.task_tracker.repository.UserRepository;
import com.example.task_tracker.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerIT {

    @Autowired MockMvc mockMvc;
    @Autowired UserRepository userRepository;
    @Autowired TaskRepository taskRepository;
    @Autowired JwtService jwtService;

    private User owner;
    private User other;
    private Task ownersTask;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        owner = userRepository.save(newUser("owner@example.com", "Owner"));
        other = userRepository.save(newUser("other@example.com", "Other"));

        Task t = new Task("Private task", "desc", LocalDate.now().plusDays(1));
        t.setOwner(owner);
        t.setStatus(TaskStatus.TODO);
        ownersTask = taskRepository.save(t);
    }

    @Test
    void getById_returns200_whenTaskBelongsToCaller() throws Exception {
        String token = jwtService.generateToken(owner.getId(), owner.getEmail());

        mockMvc.perform(get("/api/v1/tasks/{id}", ownersTask.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ownersTask.getId()))
                .andExpect(jsonPath("$.title").value("Private task"));
    }

    @Test
    void getById_returns404_whenTaskBelongsToAnotherUser() throws Exception {
        String token = jwtService.generateToken(other.getId(), other.getEmail());

        mockMvc.perform(get("/api/v1/tasks/{id}", ownersTask.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task not found"));
    }

    @Test
    void getById_returns401_whenNoTokenProvided() throws Exception {
        mockMvc.perform(get("/api/v1/tasks/{id}", ownersTask.getId()))
                .andExpect(status().isUnauthorized());
    }

    private User newUser(String email, String displayName) {
        User u = new User(email, "x", displayName);
        u.setPassword("$2a$10$dummyhashforintegrationtest0000000000000000000000000000000");
        return u;
    }
}