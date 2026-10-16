package com.example.task_tracker.dto;

import com.example.task_tracker.dto.TaskDtos.FieldError;

import java.time.Instant;
import java.util.List;

public record ApiError(
    Instant timestamp,
    int status,
    String error,
    String message,
    String path,
    List<FieldError> details
) {}