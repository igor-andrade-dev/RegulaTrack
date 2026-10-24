package com.regulatrack.backend.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

public record CreateUserRequest(
        @NotBlank(message = "Username is required")
        @Size(max = 80, message = "Username must be at most 80 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email address")
        @Size(max = 160, message = "Email must be at most 160 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 12, max = 128, message = "Password must contain between 12 and 128 characters")
        String password,

        @NotBlank(message = "Role is required")
        String role,

        Set<String> permissions
) {}
