package com.regulatrack.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(max = 80)
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        @Size(max = 160)
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 6, message = "Password must contain at least 6 characters")
        String password,

        @NotBlank(message = "Role is required")
        String role,

        Set<String> permissions
) {}
