package com.regulatrack.backend.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "false", matchIfMissing = true)
public class LoggingPasswordResetNotificationService implements PasswordResetNotificationService {
    private static final Logger log = LoggerFactory.getLogger(LoggingPasswordResetNotificationService.class);
    @Override public void sendResetLink(String email, String resetUrl) {
        log.info("Password reset link for {}: {}", email, resetUrl);
    }
}
