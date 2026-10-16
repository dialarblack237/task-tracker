package com.example.task_tracker.exception;

import com.example.task_tracker.dto.ApiError;
import com.example.task_tracker.dto.TaskDtos.FieldError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1. Any domain exception (404, 409, 403, ...).
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApi(ApiException ex,
                                              HttpServletRequest req) {
        return build(ex.getStatus(), ex.getMessage(), req, List.of());
    }

    // 2. Bean Validation failures -> 400 with per-field details.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest req) {

        List<FieldError> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fe -> new FieldError(fe.getField(), fe.getDefaultMessage()))
                .toList();

        return build(HttpStatus.BAD_REQUEST, "validation failed", req, details);
    }

    // 3. Malformed JSON / wrong types -> 400 without details.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest req) {
        return build(HttpStatus.BAD_REQUEST, "malformed request body", req, List.of());
    }

    // 4. Catch-all -> 500. Never leak the exception message.
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(
            Exception ex,
            HttpServletRequest req) {
        log.error("Unhandled exception at {}: {}",
                  req.getRequestURI(), ex.getMessage(), ex);
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                     "something went wrong", req, List.of());
    }

    private ResponseEntity<ApiError> build(HttpStatus status,
                                           String message,
                                           HttpServletRequest req,
                                           List<FieldError> details) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                req.getRequestURI(),
                details
        );
        return ResponseEntity.status(status).body(body);
    }
}