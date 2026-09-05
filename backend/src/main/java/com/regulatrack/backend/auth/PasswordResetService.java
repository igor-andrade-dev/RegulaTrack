package com.regulatrack.backend.auth;

import com.regulatrack.backend.auth.dto.MessageResponse;
import com.regulatrack.backend.domain.user.PasswordResetToken;
import com.regulatrack.backend.domain.user.User;
import com.regulatrack.backend.repository.user.PasswordResetTokenRepository;
import com.regulatrack.backend.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PasswordResetService {
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetNotificationService notificationService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final String frontendBaseUrl;

    public PasswordResetService(UserRepository userRepository, PasswordResetTokenRepository tokenRepository, PasswordEncoder passwordEncoder, PasswordResetNotificationService notificationService, @Value("${app.frontend.base-url:http://localhost:4200}") String frontendBaseUrl) {
        this.userRepository=userRepository; this.tokenRepository=tokenRepository; this.passwordEncoder=passwordEncoder; this.notificationService=notificationService; this.frontendBaseUrl=frontendBaseUrl;
    }

    @Transactional
    public void requestReset(String email) {
        String normalized = email.trim().toLowerCase();
        userRepository.findByEmail(normalized).ifPresent(user -> {
            tokenRepository.findAll().stream().filter(t -> t.getUser().getId().equals(user.getId()) && t.getUsedAt() == null && t.getExpiresAt().isAfter(LocalDateTime.now())).forEach(t -> t.setUsedAt(LocalDateTime.now()));
            byte[] bytes = new byte[32]; secureRandom.nextBytes(bytes);
            String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            PasswordResetToken token = new PasswordResetToken();
            token.setUser(user); token.setTokenHash(hash(rawToken)); token.setExpiresAt(LocalDateTime.now().plusMinutes(30));
            tokenRepository.save(token);
            notificationService.sendResetLink(normalized, frontendBaseUrl + "/reset-password?token=" + rawToken);
        });
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        PasswordResetToken token = tokenRepository.findByTokenHash(hash(rawToken)).orElseThrow(() -> new IllegalArgumentException("Invalid or expired reset link."));
        if (token.getUsedAt() != null || token.getExpiresAt().isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Invalid or expired reset link.");
        User user = token.getUser(); user.setPassword(passwordEncoder.encode(newPassword));
        token.setUsedAt(LocalDateTime.now()); userRepository.save(user); tokenRepository.save(token);
    }

    private String hash(String value) {
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");
            byte[] hash=digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb=new StringBuilder(); for(byte b:hash) sb.append(String.format("%02x", b)); return sb.toString();
        } catch (Exception e) { throw new IllegalStateException("Could not hash reset token", e); }
    }
}
