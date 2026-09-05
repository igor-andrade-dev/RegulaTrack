package com.regulatrack.backend.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.mail.enabled", havingValue = "true")
public class SmtpPasswordResetNotificationService implements PasswordResetNotificationService {
    private final JavaMailSender mailSender;
    private final String from;
    public SmtpPasswordResetNotificationService(JavaMailSender mailSender, @Value("${app.mail.from:no-reply@regulatrack.local}") String from) {
        this.mailSender = mailSender; this.from = from;
    }
    @Override public void sendResetLink(String email, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from); message.setTo(email); message.setSubject("RegulaTrack password reset");
        message.setText("We received a request to reset your RegulaTrack password.\n\nUse this link within 30 minutes:\n" + resetUrl + "\n\nIf you did not request this, you can ignore this email.");
        mailSender.send(message);
    }
}
