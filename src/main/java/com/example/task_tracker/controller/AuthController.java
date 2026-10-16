package com.example.task_tracker.controller;

import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.AuthDtos.RegisterRequest;
import com.example.task_tracker.dto.AuthDtos.UserResponse;
import com.example.task_tracker.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @Valid @RequestBody RegisterRequest req) {

        User user = authService.register(req);
        UserResponse body = new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getCreatedAt());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", "/api/v1/users/" + user.getId())
                .body(body);
    }
}