package com.vms.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
    }

    @Test
    void generateTokenWithRawSecretShouldReturnValidToken() {
        String validSecret = Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8)
        );
        ReflectionTestUtils.setField(jwtUtils, "secret", validSecret);
        ReflectionTestUtils.setField(jwtUtils, "expiration", 3600000L);

        UserDetails userDetails = new User("user@example.com", "password", Collections.emptyList());

        String token = jwtUtils.generateToken(userDetails);

        assertNotNull(token);
        assertEquals("user@example.com", jwtUtils.extractUsername(token));
        assertTrue(jwtUtils.validateToken(token, userDetails));
        assertFalse(jwtUtils.extractExpiration(token).before(new java.util.Date()));
    }

    @Test
    void generateTokenWithShortSecretShouldThrow() {
        String shortSecret = Base64.getEncoder().encodeToString(
                "short-secret".getBytes(StandardCharsets.UTF_8)
        );
        ReflectionTestUtils.setField(jwtUtils, "secret", shortSecret);
        ReflectionTestUtils.setField(jwtUtils, "expiration", 3600000L);

        UserDetails userDetails = new User("user@example.com", "password", Collections.emptyList());

        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> jwtUtils.generateToken(userDetails));

        assertTrue(exception.getMessage().contains("JWT signing key is too short"));
    }

    @Test
    void tokenShouldBeInvalidAfterUsernameMismatch() {
        String validSecret = Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8)
        );
        ReflectionTestUtils.setField(jwtUtils, "secret", validSecret);
        ReflectionTestUtils.setField(jwtUtils, "expiration", 3600000L);

        UserDetails userDetails = new User("user@example.com", "password", Collections.emptyList());
        String token = jwtUtils.generateToken(userDetails);

        UserDetails differentUser = new User("other@example.com", "password", Collections.emptyList());

        assertFalse(jwtUtils.validateToken(token, differentUser));
    }
}
