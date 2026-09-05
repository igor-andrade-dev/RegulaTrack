package com.regulatrack.backend.dto.user;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse(
        Long id,
        String username,
        String email,
        String role,
        Set<String> permissions,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
