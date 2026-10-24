package com.regulatrack.backend.repository.user;

import com.regulatrack.backend.domain.user.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    List<PasswordResetToken> findByUser_IdAndUsedAtIsNullAndExpiresAtAfter(
            Long userId, LocalDateTime now);

    List<PasswordResetToken> findByUser_IdAndUsedAtIsNull(Long userId);
}
