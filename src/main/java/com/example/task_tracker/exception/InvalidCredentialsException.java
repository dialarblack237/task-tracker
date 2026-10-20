
package com.example.task_tracker.exception;

import org.springframework.http.HttpStatus;

/**
 * 401 — thrown when email is unknown OR password is wrong.
 * Same exception for both so the client cannot tell which failed.
 */
public class InvalidCredentialsException extends ApiException {

    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "invalid credentials");
    }
}