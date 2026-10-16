package com.example.task_tracker.mapper;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;
import com.example.task_tracker.dto.TaskDtos.UpdateTaskRequest;

public final class TaskMapper {

    private TaskMapper() {}

    // DTO -> Entity (create path)
    public static Task toEntity(CreateTaskRequest req, User owner) {
        Task t = new Task();
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setDueDate(req.dueDate());
        t.setStatus(TaskStatus.TODO);   // default — clients cannot set it
        t.setOwner(owner);              // owner comes from the JWT, not the body
        return t;
    }

    // DTO -> Existing Entity (update path — mutate in place, do not create)
    // Intentionally does NOT touch id or owner.
    public static void applyUpdate(Task target, UpdateTaskRequest req) {
        target.setTitle(req.title());
        target.setDescription(req.description());
        target.setStatus(req.status());
        target.setDueDate(req.dueDate());
    }

    // Entity -> DTO
    public static TaskResponse toResponse(Task t) {
        return new TaskResponse(
            t.getId(),
            t.getTitle(),
            t.getDescription(),
            t.getStatus(),
            t.getDueDate(),
            t.getCreatedAt(),
            t.getUpdatedAt()
        );
    }
}