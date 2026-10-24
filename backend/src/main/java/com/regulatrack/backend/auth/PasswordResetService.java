package com.regulatrack.backend.auth;

import com.regulatrack.backend.domain.user.PasswordResetToken;
import com.regulatrack.backend.domain.user.User;
import com.regulatrack.backend.exception.BadRequestException;
import com.regulatrack.backend.repository.user.PasswordResetTokenRepository;
import com.regulatrack.backend.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Locale;

@Service
public class PasswordResetService {
    private static final Logger log = LoggerFactory.getLogger(PasswordResetService.class);
    private static final int TOKEN_VALIDITY_MINUTES = 30;

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetNotificationService notificationService;
    private final SecureRandom secureRandom = new SecureRandom();
    private final String frontendBaseUrl;

    public PasswordResetService(
            UserRepository userRepository,
            PasswordResetTokenRepository tokenRepository,
            PasswordEncoder passwordEncoder,
            PasswordResetNotificationService notificationService,
            @Value("${app.frontend.base-url:http://localhost:4200}") String frontendBaseUrl) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.notificationService = notificationService;
        this.frontendBaseUrl = frontendBaseUrl;
    }

    @Transactional
    public void requestReset(String email) {
        if (email == null || email.isBlank()) {
            return;
        }

        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        userRepository.findByEmailIgnoreCase(normalizedEmail).ifPresent(user -> {
            LocalDateTime now = LocalDateTime.now();
            tokenRepository.findByUser_IdAndUsedAtIsNullAndExpiresAtAfter(user.getId(), now)
                    .forEach(token -> token.setUsedAt(now));

            byte[] bytes = new byte[32];
            secureRandom.nextBytes(bytes);
            String rawToken = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);

            PasswordResetToken token = new PasswordResetToken();
            token.setUser(user);
            token.setTokenHash(hash(rawToken));
            token.setExpiresAt(now.plusMinutes(TOKEN_VALIDITY_MINUTES));
            tokenRepository.save(token);

            String resetUrl = frontendBaseUrl.replaceAll("/+$", "") + "/reset-password#token=" + rawToken;
            try {
                notificationService.sendResetLink(normalizedEmail, resetUrl);
            } catch (RuntimeException exception) {
                // Never log the URL or raw token. Avoid revealing account existence through the API response.
                token.setUsedAt(LocalDateTime.now());
                tokenRepository.save(token);
                log.warn("Password-reset email delivery failed for user id {}", user.getId());
            }
        });
    }

    @Transactional
    public void resetPassword(String rawToken, String newPassword) {
        if (rawToken == null || rawToken.isBlank() || newPassword == null || newPassword.length() < 12 || newPassword.length() > 128) {
            throw new BadRequestException("The reset link is invalid or expired, or the new password does not meet the requirements.");
        }

        LocalDateTime now = LocalDateTime.now();
        PasswordResetToken token = tokenRepository.findByTokenHash(hash(rawToken))
                .orElseThrow(() -> new BadRequestException("The reset link is invalid or expired."));

        if (token.getUsedAt() != null || !token.getExpiresAt().isAfter(now)) {
            throw new BadRequestException("The reset link is invalid or expired.");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setTokenVersion(user.getTokenVersion() + 1L);

        // A successful password change invalidates every outstanding reset link for the account.
        tokenRepository.findByUser_IdAndUsedAtIsNull(user.getId()).forEach(other -> other.setUsedAt(now));
        token.setUsedAt(now);
        userRepository.save(user);
        tokenRepository.save(token);
    }

    private String hash(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(value.getBytes(StandardCharsets.UTF_8));
            StringBuilder result = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                result.append(String.format(Locale.ROOT, "%02x", b));
            }
            return result.toString();
        } catch (Exception exception) {
            throw new IllegalStateException("Could not hash reset token", exception);
        }
    }
}
