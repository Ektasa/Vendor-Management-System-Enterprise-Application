package com.vms.service;

import com.vms.dto.AuthResponse;
import com.vms.dto.LoginRequest;
import com.vms.dto.RegisterRequest;
import com.vms.entity.User;
import com.vms.repository.UserRepository;
import com.vms.security.JwtUtils;

import software.amazon.awssdk.services.s3.S3Client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    private JwtUtils jwtUtils;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        String secret = Base64.getEncoder().encodeToString(
                "01234567890123456789012345678901".getBytes(StandardCharsets.UTF_8)
        );
        ReflectionTestUtils.setField(jwtUtils, "secret", secret);
        ReflectionTestUtils.setField(jwtUtils, "expiration", 3600000L);
        authService = new AuthService(userRepository, passwordEncoder, authenticationManager, userDetailsService, jwtUtils);
    }

    @Test
    void registerShouldSaveUserAndReturnAuthResponse() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded-password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        "user@example.com",
                        "encoded-password",
                        true,
                        true,
                        true,
                        true,
                        java.util.Collections.emptyList())
        );

        RegisterRequest request = new RegisterRequest("user@example.com", "password", "User Name", User.Role.VENDOR);
        AuthResponse response = authService.register(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("user@example.com", response.getEmail());
        assertEquals("User Name", response.getFullName());
        assertEquals(User.Role.VENDOR, response.getRole());
        assertNotNull(response.getToken());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("password");
    }

    @Test
    void registerShouldFailWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        RegisterRequest request = new RegisterRequest("user@example.com", "password", "User Name", User.Role.VENDOR);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.register(request));

        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void loginShouldAuthenticateAndReturnAuthResponse() {
        User user = new User();
        user.setId(2L);
        user.setEmail("login@example.com");
        user.setPassword("encoded-password");
        user.setFullName("Login User");
        user.setRole(User.Role.MANAGER);
        user.setEnabled(true);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail("login@example.com")).thenReturn(Optional.of(user));
        when(userDetailsService.loadUserByUsername("login@example.com")).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        "login@example.com",
                        "encoded-password",
                        true,
                        true,
                        true,
                        true,
                        java.util.Collections.emptyList())
        );

        LoginRequest request = new LoginRequest("login@example.com", "password");
        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertEquals("login@example.com", response.getEmail());
        assertEquals("Login User", response.getFullName());
        assertEquals(User.Role.MANAGER, response.getRole());
        assertNotNull(response.getToken());
        assertFalse(response.getToken().isBlank());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail("login@example.com");
    }

    @Test
    void loginShouldFailWhenUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest("missing@example.com", "password");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(request));

        assertEquals("User not found", exception.getMessage());
    }

    @Autowired
private S3Client s3Client;


public void test(){

    s3Client.listBuckets()
            .buckets()
            .forEach(System.out::println);

}
}
