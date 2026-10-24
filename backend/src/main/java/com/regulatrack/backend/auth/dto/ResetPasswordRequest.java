package com.regulatrack.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "Reset token is required")
        @Size(max = 256, message = "Reset token is invalid")
        String token,

        @NotBlank(message = "Password is required")
        @Size(min = 12, max = 128, message = "Password must contain between 12 and 128 characters")
        String password
) {}
