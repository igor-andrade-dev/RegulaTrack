package com.regulatrack.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(

        @NotBlank(message = "Username é obrigatório")
        String username,

        @NotBlank(message = "Password é obrigatória")
        String password,

        @NotBlank(message = "Role é obrigatória")
        String role

) {
}