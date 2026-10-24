package com.regulatrack.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Username é obrigatório")
        @Size(max = 80, message = "Username deve ter no máximo 80 caracteres")
        String username,

        @NotBlank(message = "Password é obrigatória")
        @Size(min = 12, max = 128, message = "A senha deve ter entre 12 e 128 caracteres")
        String password,

        @NotBlank(message = "Role é obrigatória")
        String role
) {}
