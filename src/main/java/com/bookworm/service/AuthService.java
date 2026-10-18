package com.bookworm.service;

import com.bookworm.dto.LoginRequest;
import com.bookworm.dto.LoginResponse;
import com.bookworm.entity.User;
import com.bookworm.exception.BusinessException;
import com.bookworm.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new BusinessException("Invalid email or password"));

        if (!verifyPassword(request.password(), user.getPasswordHash())) {
            throw new BusinessException("Invalid email or password");
        }

        return new LoginResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                user.getGiftPointsBalance(),
                "Login successful"
        );
    }

    /**
     * SHA-256 based password verification.
     * The hash stored in DB is Base64(SHA-256(password)).
     */
    private boolean verifyPassword(String rawPassword, String storedHash) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            String computed = Base64.getEncoder().encodeToString(hashBytes);
            return MessageDigest.isEqual(computed.getBytes(StandardCharsets.UTF_8),
                                         storedHash.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password verification error", e);
        }
    }
}
