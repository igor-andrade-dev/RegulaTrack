package com.regulatrack.backend.auth.dto;

import java.util.Set;

public record CurrentUserResponse(
        Long id,
        String username,
        String role,
        Set<String> permissions
) {}
