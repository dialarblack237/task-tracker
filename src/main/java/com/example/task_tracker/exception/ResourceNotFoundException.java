package com.example.task_tracker.exception;

import org.springframework.http.HttpStatus;

/**
 * 404 — resource does not exist, OR does not belong to the caller.
 */
public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}