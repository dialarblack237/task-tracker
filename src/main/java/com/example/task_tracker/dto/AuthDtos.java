package com.example.task_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthDtos {

    private AuthDtos() {}

    public record RegisterRequest(
        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid address")
        @Size(max = 255, message = "email must be at most 255 characters")
        String email,

        @NotBlank(message = "password is required")
        @Size(min = 8, max = 72, message = "password must be 8-72 characters")
        String password,

        @NotBlank(message = "displayName is required")
        @Size(min = 2, max = 50, message = "displayName must be 2-50 characters")
        String displayName
    ) {}

    public record LoginRequest(
        @NotBlank(message = "email is required")
        @Email(message = "email must be a valid address")
        String email,

        @NotBlank(message = "password is required")
        String password
    ) {}

    public record AuthResponse(
        String token,
        String tokenType,
        long expiresInSeconds
    ) {}

    public record UserResponse(
        Long id,
        String email,
        String displayName,
        java.time.Instant createdAt
    ) {}
}