package com.example.task_tracker.support;

import com.example.task_tracker.domain.User;
import com.example.task_tracker.security.JwtService;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public final class JwtTestSupport {

    private JwtTestSupport() {}

    /**
     * Injects a real, signed JWT for the given user.
     * Exercises the REAL JwtAuthFilter — no mocks.
     */
    public static RequestPostProcessor jwtFor(JwtService jwt, User user) {
        String token = jwt.generateToken(user.getId(), user.getEmail());
        return request -> {
            request.addHeader("Authorization", "Bearer " + token);
            return request;
        };
    }
}