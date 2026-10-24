package com.regulatrack.backend.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "false", matchIfMissing = true)
public class LoggingPasswordResetNotificationService implements PasswordResetNotificationService {
    private static final Logger log = LoggerFactory.getLogger(LoggingPasswordResetNotificationService.class);

    @Override
    public void sendResetLink(String email, String resetUrl) {
        // Intentionally do not log recipient PII, the URL, or the one-time token.
        log.warn("Password-reset email is disabled; no reset link was delivered. Configure SMTP for account recovery.");
    }
}
