package com.example.task_tracker.controller;

import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.dto.TaskDtos.UpdateTaskRequest;
import com.example.task_tracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> list(@AuthenticationPrincipal User owner) {
        return taskService.listForUser(owner);
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable Long id,
                            @AuthenticationPrincipal User owner) {
        return taskService.getById(id, owner);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody CreateTaskRequest req,
            @AuthenticationPrincipal User owner) {
        TaskResponse created = taskService.create(req, owner);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/tasks/" + created.id())
                .body(created);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id,
                               @Valid @RequestBody UpdateTaskRequest req,
                               @AuthenticationPrincipal User owner) {
        return taskService.update(id, req, owner);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id,
                                       @AuthenticationPrincipal User owner) {
        taskService.delete(id, owner);
        return ResponseEntity.noContent().build();
    }
}