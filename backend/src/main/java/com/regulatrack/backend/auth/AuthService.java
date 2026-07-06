package com.regulatrack.backend.auth;

import com.regulatrack.backend.domain.user.User;
import com.regulatrack.backend.repository.user.UserRepository;
import com.regulatrack.backend.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
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
}