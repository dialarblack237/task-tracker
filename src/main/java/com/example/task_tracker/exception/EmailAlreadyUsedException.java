package com.example.task_tracker.exception;

import org.springframework.http.HttpStatus;

/**
 * 409 — unique constraint on a business key (email in v1).
 */
public class EmailAlreadyUsedException extends ApiException {

    public EmailAlreadyUsedException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}