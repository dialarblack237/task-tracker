package com.example.task_tracker.service;

import com.example.task_tracker.domain.User;
import com.example.task_tracker.dto.AuthDtos.RegisterRequest;
import com.example.task_tracker.exception.EmailAlreadyUsedException;
import com.example.task_tracker.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository users;
    private final PasswordEncoder encoder;

    public AuthService(UserRepository users, PasswordEncoder encoder) {
        this.users = users;
        this.encoder = encoder;
    }

    @Transactional
    public User register(RegisterRequest req) {
        String email = req.email().toLowerCase();

        if (users.existsByEmailIgnoreCase(email)) {
            throw new EmailAlreadyUsedException("email already registered");
        }

        User u = new User();
        u.setEmail(email);
        u.setDisplayName(req.displayName());
        u.setPassword(encoder.encode(req.password()));   // <-- hashed
        return users.save(u);
    }
}