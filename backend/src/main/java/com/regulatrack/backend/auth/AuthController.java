package com.regulatrack.backend.auth;

import com.regulatrack.backend.auth.dto.LoginRequest;
import com.regulatrack.backend.auth.dto.LoginResponse;
import com.regulatrack.backend.auth.dto.RegisterRequest;
import com.regulatrack.backend.auth.dto.RegisterResponse;
import com.regulatrack.backend.auth.dto.CurrentUserResponse;
import jakarta.validation.Valid;
import com.regulatrack.backend.auth.dto.ForgotPasswordRequest;
import com.regulatrack.backend.auth.dto.ResetPasswordRequest;
import com.regulatrack.backend.auth.dto.MessageResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String token = authService.login(
                request.username(),
                request.password()
        );

        return new LoginResponse(token);
    }

    @PostMapping("/password-reset/request")
    public MessageResponse requestPasswordReset(@Valid @RequestBody ForgotPasswordRequest request) {
        passwordResetService.requestReset(request.email());
        return new MessageResponse("If an account exists for that email, a password reset link has been sent.");
    }

    @PostMapping("/password-reset/confirm")
    public MessageResponse confirmPasswordReset(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.password());
        return new MessageResponse("Password updated successfully.");
    }

    @GetMapping("/me")
    public CurrentUserResponse me(Authentication authentication) {
        return authService.me(authentication.getName());
    }

    @PostMapping("/register")
    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    public RegisterResponse register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return authService.register(request);
    }
}