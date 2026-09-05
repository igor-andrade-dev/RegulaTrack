package com.regulatrack.backend.domain.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔐 Username único para login
    @Column(nullable = false, unique = true, length = 80)
    private String username;

    // 🔐 Senha (armazenada com BCrypt futuramente)
    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 160)
    private String email;

    // 👤 Perfil base do usuário (ADMIN / USER)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // 🔐 Permissões granulares por módulo/ação
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_permissions", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "permission", nullable = false, length = 80)
    @Enumerated(EnumType.STRING)
    private Set<Permission> permissions = new HashSet<>();

    // 📅 Auditoria
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // 🚀 executado ao criar registro
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // 🚀 executado ao atualizar registro
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ================================
    // GETTERS & SETTERS
    // ================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions == null ? new HashSet<>() : new HashSet<>(permissions);
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}