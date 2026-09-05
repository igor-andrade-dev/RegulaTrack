package com.regulatrack.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Reset token is required")
        String token,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must contain at least 6 characters")
        String password
) {}
