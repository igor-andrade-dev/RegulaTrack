package com.regulatrack.backend.controller.license;

import com.regulatrack.backend.domain.license.LicenseStatus;
import com.regulatrack.backend.dto.common.PageResponse;
import com.regulatrack.backend.dto.license.CreateLicenseRequest;
import com.regulatrack.backend.dto.license.LicenseResponse;
import com.regulatrack.backend.dto.license.UpdateLicenseRequest;
import com.regulatrack.backend.service.license.LicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
@Tag(name = "Licenses", description = "Endpoints for managing regulatory licenses")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @Operation(summary = "List all licenses")
    @GetMapping
    public ResponseEntity<List<LicenseResponse>> findAll() {
        return ResponseEntity.ok(licenseService.findAll());
    }

    @Operation(summary = "List active licenses")
    @GetMapping("/active")
    public ResponseEntity<List<LicenseResponse>> findActive() {
        return ResponseEntity.ok(licenseService.findActive());
    }

    @Operation(summary = "List expired licenses")
    @GetMapping("/expired")
    public ResponseEntity<List<LicenseResponse>> findExpired() {
        return ResponseEntity.ok(licenseService.findExpired());
    }

    @Operation(summary = "List licenses expiring soon")
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<LicenseResponse>> findExpiringSoon() {
        return ResponseEntity.ok(licenseService.findExpiringSoon());
    }

    @Operation(summary = "List pending licenses")
    @GetMapping("/pending")
    public ResponseEntity<List<LicenseResponse>> findPending() {
        return ResponseEntity.ok(licenseService.findPending());
    }

    @Operation(summary = "Search licenses with filters and pagination")
    @GetMapping("/search")
    public ResponseEntity<PageResponse<LicenseResponse>> search(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long branchId,
            @RequestParam(required = false) LicenseStatus status,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                licenseService.search(companyId, branchId, status, category, name, page, size)
        );
    }

    @Operation(summary = "Find licenses by company")
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<LicenseResponse>> findByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(licenseService.findByCompanyId(companyId));
    }

    @Operation(summary = "Find licenses by branch")
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<LicenseResponse>> findByBranchId(@PathVariable Long branchId) {
        return ResponseEntity.ok(licenseService.findByBranchId(branchId));
    }

    @Operation(summary = "Find license by ID")
    @GetMapping("/{id}")
    public ResponseEntity<LicenseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(licenseService.findById(id));
    }

    @Operation(summary = "Create a new license")
    @PostMapping
    public ResponseEntity<LicenseResponse> create(@Valid @RequestBody CreateLicenseRequest request) {
        LicenseResponse response = licenseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Update a license")
    @PutMapping("/{id}")
    public ResponseEntity<LicenseResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateLicenseRequest request
    ) {
        return ResponseEntity.ok(licenseService.update(id, request));
    }

    @Operation(summary = "Delete a license")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        licenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}