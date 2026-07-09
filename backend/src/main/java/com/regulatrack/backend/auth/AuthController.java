package com.regulatrack.backend.auth;

import com.regulatrack.backend.auth.dto.LoginRequest;
import com.regulatrack.backend.auth.dto.LoginResponse;
import com.regulatrack.backend.auth.dto.RegisterRequest;
import com.regulatrack.backend.auth.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {

        String token = authService.login(
                request.username(),
                request.password()
        );

        return new LoginResponse(token);
    }

    @PostMapping("/register")
    public RegisterResponse register(
            @RequestBody @Valid RegisterRequest request
    ) {
        return authService.register(request);
    }
}