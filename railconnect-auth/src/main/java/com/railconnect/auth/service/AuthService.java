package com.railconnect.auth.service;

import com.railconnect.auth.dto.AuthRequest;
import com.railconnect.auth.dto.AuthResponse;
import com.railconnect.auth.dto.RegisterRequest;
import com.railconnect.auth.jwt.JwtUtil;
import com.railconnect.auth.model.User;
import com.railconnect.auth.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername())) {
            throw new IllegalArgumentException("Username '" + req.getUsername() + "' is already taken.");
        }
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email '" + req.getEmail() + "' is already registered.");
        }
        if (userRepository.existsByPhone(req.getPhone())) {
            throw new IllegalArgumentException("Phone number '" + req.getPhone() + "' is already registered.");
        }

        String role = (req.getRole() != null && !req.getRole().isBlank()) ? req.getRole() : "ROLE_PASSENGER";
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        User user = new User(
                req.getUsername().trim(),
                req.getEmail().trim().toLowerCase(),
                req.getPhone().trim(),
                passwordEncoder.encode(req.getPassword()),
                role,
                req.getFirstName().trim(),
                req.getLastName().trim()
        );

        User saved = userRepository.save(user);
        String token = jwtUtil.generateToken(saved.getUsername(), saved.getRole(), saved.getId());

        return new AuthResponse(token, saved.getId(), saved.getUsername(), saved.getEmail(), saved.getRole(), saved.getFullName());
    }

    public AuthResponse login(AuthRequest req) {
        User user = userRepository.findByUsernameOrEmail(req.getUsername(), req.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        if (!"ACTIVE".equalsIgnoreCase(user.getStatus())) {
            throw new IllegalStateException("Account is suspended or inactive. Please contact railway administration.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole(), user.getId());
        return new AuthResponse(token, user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getFullName());
    }

    public User getProfile(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }
}
