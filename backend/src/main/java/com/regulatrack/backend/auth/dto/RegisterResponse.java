package com.regulatrack.backend.auth.dto;

public record RegisterResponse(

        Long id,
        String username,
        String role

) {
}