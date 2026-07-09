package com.regulatrack.backend.auth;

import com.regulatrack.backend.domain.user.User;
import com.regulatrack.backend.repository.user.UserRepository;
import com.regulatrack.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.regulatrack.backend.auth.dto.RegisterRequest;
import com.regulatrack.backend.auth.dto.RegisterResponse;
import com.regulatrack.backend.domain.user.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String username, String password) {

        // 🔐 valida credenciais (BCrypt + UserDetailsService)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // 🔍 busca usuário real no banco
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 gera token com ROLE
        return JwtUtil.generateToken(
                user.getUsername(),
                user.getRole().name()
        );
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Usuário já existe");
        }

        User user = new User();

        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(Role.valueOf(request.role().toUpperCase()));

        User saved = userRepository.save(user);

        return new RegisterResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getRole().name()
        );
    }
}