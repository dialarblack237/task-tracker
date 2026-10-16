package com.example.task_tracker.mapper;

import com.example.task_tracker.domain.Task;
import com.example.task_tracker.domain.TaskStatus;
import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.TaskDtos.CreateTaskRequest;
import com.example.task_tracker.dto.TaskDtos.TaskResponse;

public final class TaskMapper {

    private TaskMapper() {}

    public static Task toEntity(CreateTaskRequest req, User owner) {
        Task t = new Task();
        t.setTitle(req.title());
        t.setDescription(req.description());
        t.setDueDate(req.dueDate());
        t.setStatus(TaskStatus.TODO);
        t.setOwner(owner);
        return t;
    }

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