package com.regulatrack.backend.controller.user;

import com.regulatrack.backend.dto.user.*;
import com.regulatrack.backend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@org.springframework.security.access.prepost.PreAuthorize("hasAuthority('PERM_USERS_VIEW')")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserResponse> findAll() { return userService.findAll(); }

    @GetMapping("/permissions")
    public List<PermissionOptionResponse> permissionOptions() { return userService.permissionOptions(); }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Long id) { return userService.findById(id); }

    @PostMapping
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('PERM_USERS_CREATE')")
    public ResponseEntity<UserResponse> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @PutMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('PERM_USERS_UPDATE')")
    public UserResponse update(@PathVariable Long id, @Valid @RequestBody UpdateUserRequest request) {
        return userService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @org.springframework.security.access.prepost.PreAuthorize("hasAuthority('PERM_USERS_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, Authentication authentication) {
        userService.delete(id, authentication.getName());
    }
}
