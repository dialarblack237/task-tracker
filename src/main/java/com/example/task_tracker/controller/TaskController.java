package com.example.task_tracker.controller;

import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.dto.TaskDtos.UpdateTaskRequest;
import com.example.task_tracker.exception.ResourceNotFoundException;
import com.example.task_tracker.repository.UserRepository;
import com.example.task_tracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;
    private final UserRepository userRepository;

    public TaskController(TaskService taskService, UserRepository userRepository) {
        this.taskService = taskService;
        this.userRepository = userRepository;
    }

    // ⚠️ TEMPORARY: userId comes from query param.
    // Will be replaced by @AuthenticationPrincipal User owner in Module 6.
    private User currentUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @GetMapping
    public List<TaskResponse> list(@RequestParam Long userId) {
        return taskService.listForUser(currentUser(userId));
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable Long id,
                            @RequestParam Long userId) {
        return taskService.getById(id, currentUser(userId));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody CreateTaskRequest req,
            @RequestParam Long userId) {
        TaskResponse created = taskService.create(req, currentUser(userId));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/tasks/" + created.id())
                .body(created);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id,
                               @Valid @RequestBody UpdateTaskRequest req,
                               @RequestParam Long userId) {
        return taskService.update(id, req, currentUser(userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @RequestParam Long userId) {
        taskService.delete(id, currentUser(userId));
        return ResponseEntity.noContent().build();
    }
}