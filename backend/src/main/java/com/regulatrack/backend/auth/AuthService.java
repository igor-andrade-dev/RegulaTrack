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
import com.regulatrack.backend.auth.dto.CurrentUserResponse;
import java.util.stream.Collectors;

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

    public CurrentUserResponse me(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return new CurrentUserResponse(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),
                effectivePermissions(user)
        );
    }

    private java.util.Set<String> effectivePermissions(User user) {
        if (user.getRole() == Role.ADMIN) {
            return java.util.Arrays.stream(com.regulatrack.backend.domain.user.Permission.values())
                    .map(Enum::name).collect(Collectors.toSet());
        }
        return user.getPermissions().stream().map(Enum::name).collect(Collectors.toSet());
    }

    public RegisterResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
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