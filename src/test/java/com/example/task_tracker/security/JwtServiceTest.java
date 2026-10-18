package com.example.task_tracker.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        JwtProperties props = new JwtProperties(
                "test-secret-that-is-at-least-32-bytes-long-for-hmac-sha256", 3600);
        jwtService = new JwtService(props);
    }

    @Test
    void generateToken_roundTripsUserIdAndEmail() {
        String token = jwtService.generateToken(42L, "a@example.com");
        assertThat(jwtService.extractUserId(token)).isEqualTo(42L);
        assertThat(jwtService.extractEmail(token)).isEqualTo("a@example.com");
        assertThat(jwtService.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_returnsFalse_whenTokenIsMalformed() {
        assertThat(jwtService.isTokenValid("not-a-jwt")).isFalse();
    }

    @Test
    void isTokenValid_returnsFalse_whenTokenIsEmpty() {
        assertThat(jwtService.isTokenValid("")).isFalse();
    }

    @Test
    void expirySeconds_returnsConfiguredValue() {
        assertThat(jwtService.expirySeconds()).isEqualTo(3600);
    }
}