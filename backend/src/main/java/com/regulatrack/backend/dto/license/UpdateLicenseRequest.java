package com.regulatrack.backend.dto.license;

import com.regulatrack.backend.domain.license.LicenseStatus;

import java.time.LocalDate;

public record UpdateLicenseRequest(
        Long companyId,
        Long branchId,
        String name,
        String description,
        String category,
        String issuer,
        LocalDate expiresAt,
        LicenseStatus status
) {
}