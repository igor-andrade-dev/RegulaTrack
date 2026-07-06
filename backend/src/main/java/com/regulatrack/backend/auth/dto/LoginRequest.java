package com.regulatrack.backend.auth.dto;

public record LoginRequest(
        String username,
        String password
) {}