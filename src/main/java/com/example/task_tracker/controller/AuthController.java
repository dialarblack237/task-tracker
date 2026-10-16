package com.example.task_tracker.controller;

import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.AuthDtos.AuthResponse;
import com.example.task_tracker.dto.AuthDtos.LoginRequest;
import com.example.task_tracker.dto.AuthDtos.RegisterRequest;
import com.example.task_tracker.dto.AuthDtos.UserResponse;
import com.example.task_tracker.security.JwtService;
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
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
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

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest req) {
        User user = authService.login(req);
        String token = jwtService.generateToken(user.getId(), user.getEmail());
        AuthResponse body = new AuthResponse(
                token, "Bearer", jwtService.expirySeconds());
        return ResponseEntity.ok(body);
    }
}