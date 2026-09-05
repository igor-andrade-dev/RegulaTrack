package com.regulatrack.backend.service.user;

import com.regulatrack.backend.domain.user.Permission;
import com.regulatrack.backend.domain.user.Role;
import com.regulatrack.backend.domain.user.User;
import com.regulatrack.backend.dto.user.*;
import com.regulatrack.backend.exception.ResourceNotFoundException;
import com.regulatrack.backend.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(getUser(id));
    }

    @Transactional
    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (request.email() != null && userRepository.findByEmail(request.email().trim().toLowerCase(Locale.ROOT)).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User user = new User();
        user.setUsername(request.username().trim());
        user.setEmail(request.email().trim().toLowerCase(Locale.ROOT));
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(parseRole(request.role()));
        user.setPermissions(parsePermissions(request.permissions()));
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = getUser(id);
        if (!user.getUsername().equals(request.username()) && userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists");
        }
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
        if (!normalizedEmail.equalsIgnoreCase(user.getEmail() == null ? "" : user.getEmail())
                && userRepository.findByEmail(normalizedEmail).filter(existing -> !existing.getId().equals(id)).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        Role newRole = parseRole(request.role());
        if (user.getRole() == Role.ADMIN && newRole != Role.ADMIN &&
                userRepository.findAll().stream().filter(u -> u.getRole() == Role.ADMIN).count() <= 1) {
            throw new IllegalArgumentException("É necessário manter pelo menos um administrador");
        }
        user.setUsername(request.username().trim());
        user.setEmail(normalizedEmail);
        user.setRole(newRole);
        user.setPermissions(parsePermissions(request.permissions()));
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id, String currentUsername) {
        User user = getUser(id);
        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("Você não pode excluir o próprio usuário");
        }
        if (user.getRole() == Role.ADMIN && userRepository.findAll().stream().filter(u -> u.getRole() == Role.ADMIN).count() <= 1) {
            throw new IllegalArgumentException("Não é possível excluir o último administrador");
        }
        userRepository.delete(user);
    }

    public List<PermissionOptionResponse> permissionOptions() {
        return Arrays.stream(Permission.values()).map(permission -> {
            String code = permission.name();
            String[] parts = code.split("_");
            String action = parts[parts.length - 1];
            String module = String.join("_", Arrays.copyOf(parts, parts.length - 1));
            return new PermissionOptionResponse(code, module, action);
        }).toList();
    }

    private User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Usuário não encontrado com id: " + id));
    }

    private Role parseRole(String role) {
        try { return Role.valueOf(role.toUpperCase(Locale.ROOT)); }
        catch (Exception e) { throw new IllegalArgumentException("Role inválida. Use ADMIN ou USER"); }
    }

    private Set<Permission> parsePermissions(Set<String> permissions) {
        if (permissions == null) return new HashSet<>();
        try {
            return permissions.stream().map(value -> Permission.valueOf(value.toUpperCase(Locale.ROOT))).collect(java.util.stream.Collectors.toSet());
        } catch (Exception e) {
            throw new IllegalArgumentException("Permissão inválida");
        }
    }

    private UserResponse toResponse(User user) {
        Set<String> permissions = new HashSet<>();
        if (user.getRole() == Role.ADMIN) {
            Arrays.stream(Permission.values()).forEach(p -> permissions.add(p.name()));
        } else {
            user.getPermissions().forEach(p -> permissions.add(p.name()));
        }
        return new UserResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name(), permissions, user.getCreatedAt(), user.getUpdatedAt());
    }
}
