package com.railconnect.auth;

import com.railconnect.auth.dto.AuthRequest;
import com.railconnect.auth.dto.AuthResponse;
import com.railconnect.auth.dto.RegisterRequest;
import com.railconnect.auth.jwt.JwtUtil;
import com.railconnect.auth.model.User;
import com.railconnect.auth.repository.UserRepository;
import com.railconnect.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", "RailConnectSuperSecretKeyForAuthentication2026SecureKeyWith256BitsMinimum");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 86400000L);
        authService = new AuthService(userRepository, passwordEncoder, jwtUtil);
    }

    @Test
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest();
        req.setUsername("rohit_test");
        req.setEmail("rohit@railconnect.com");
        req.setPhone("9988776655");
        req.setPassword("Secret@123");
        req.setFirstName("Rohit");
        req.setLastName("Kumar");

        when(userRepository.existsByUsername("rohit_test")).thenReturn(false);
        when(userRepository.existsByEmail("rohit@railconnect.com")).thenReturn(false);
        when(userRepository.existsByPhone("9988776655")).thenReturn(false);

        User savedUser = new User("rohit_test", "rohit@railconnect.com", "9988776655", "hashed", "ROLE_PASSENGER", "Rohit", "Kumar");
        savedUser.setId(10L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        AuthResponse resp = authService.register(req);
        assertNotNull(resp);
        assertEquals("rohit_test", resp.getUsername());
        assertNotNull(resp.getToken());
        assertTrue(jwtUtil.validateToken(resp.getToken(), "rohit_test"));
    }

    @Test
    void testLoginSuccess() {
        String rawPassword = "password123";
        String encoded = passwordEncoder.encode(rawPassword);

        User existingUser = new User("admin", "admin@railconnect.com", "9876543299", encoded, "ROLE_ADMIN", "Admin", "User");
        existingUser.setId(1L);

        when(userRepository.findByUsernameOrEmail("admin", "admin")).thenReturn(Optional.of(existingUser));

        AuthRequest loginReq = new AuthRequest("admin", rawPassword);
        AuthResponse resp = authService.login(loginReq);

        assertNotNull(resp);
        assertEquals("admin", resp.getUsername());
        assertEquals("ROLE_ADMIN", resp.getRole());
        assertTrue(jwtUtil.validateToken(resp.getToken(), "admin"));
    }
}
