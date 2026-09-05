package com.regulatrack.backend.auth;

public interface PasswordResetNotificationService {
    void sendResetLink(String email, String resetUrl);
}
