package com.example.task_tracker.exception;

import org.springframework.http.HttpStatus;

/**
 * 403 — authenticated, but the action is not permitted.
 * v1 uses this rarely; we prefer 404 to avoid leaking resource existence.
 */
public class ForbiddenException extends ApiException {

    public ForbiddenException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}