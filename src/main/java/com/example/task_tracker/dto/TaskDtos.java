package com.example.task_tracker.dto;

import com.example.task_tracker.domain.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.time.LocalDate;

public class TaskDtos {

    private TaskDtos() {}

    public record CreateTaskRequest(
        @NotBlank(message = "title is required")
        @Size(min = 1, max = 120, message = "title must be 1-120 characters")
        String title,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        LocalDate dueDate
    ) {}

    public record UpdateTaskRequest(
        @NotBlank(message = "title is required")
        @Size(min = 1, max = 120, message = "title must be 1-120 characters")
        String title,

        @Size(max = 2000, message = "description must be at most 2000 characters")
        String description,

        @NotNull(message = "status is required")
        TaskStatus status,

        LocalDate dueDate
    ) {}

    public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        LocalDate dueDate,
        Instant createdAt,
        Instant updatedAt
    ) {}
}