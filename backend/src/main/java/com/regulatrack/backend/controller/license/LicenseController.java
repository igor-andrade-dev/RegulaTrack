package com.regulatrack.backend.controller.license;

import com.regulatrack.backend.dto.license.CreateLicenseRequest;
import com.regulatrack.backend.dto.license.LicenseResponse;
import com.regulatrack.backend.dto.license.UpdateLicenseRequest;
import com.regulatrack.backend.service.license.LicenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/licenses")
public class LicenseController {

    private final LicenseService licenseService;

    public LicenseController(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @GetMapping
    public ResponseEntity<List<LicenseResponse>> findAll() {
        return ResponseEntity.ok(licenseService.findAll());
    }
    @GetMapping("/active")
    public ResponseEntity<List<LicenseResponse>> findActive() {
        return ResponseEntity.ok(licenseService.findActive());
    }

    @GetMapping("/expired")
    public ResponseEntity<List<LicenseResponse>> findExpired() {
        return ResponseEntity.ok(licenseService.findExpired());
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<List<LicenseResponse>> findExpiringSoon() {
        return ResponseEntity.ok(licenseService.findExpiringSoon());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<LicenseResponse>> findPending() {
        return ResponseEntity.ok(licenseService.findPending());
    }
    @GetMapping("/{id}")
    public ResponseEntity<LicenseResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(licenseService.findById(id));
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<LicenseResponse>> findByCompanyId(@PathVariable Long companyId) {
        return ResponseEntity.ok(licenseService.findByCompanyId(companyId));
    }

    @GetMapping("/branch/{branchId}")
    public ResponseEntity<List<LicenseResponse>> findByBranchId(@PathVariable Long branchId) {
        return ResponseEntity.ok(licenseService.findByBranchId(branchId));
    }

    @PostMapping
    public ResponseEntity<LicenseResponse> create(@RequestBody CreateLicenseRequest request) {
        LicenseResponse response = licenseService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LicenseResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateLicenseRequest request
    ) {
        return ResponseEntity.ok(licenseService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        licenseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}