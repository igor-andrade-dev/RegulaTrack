package com.regulatrack.backend.dto.license;

import com.regulatrack.backend.domain.license.LicenseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LicenseResponse(
        Long id,
        Long companyId,
        Long branchId,
        String name,
        String description,
        String category,
        String licenseNumber,
        String issuer,
        LocalDate issuedAt,
        LocalDate expiresAt,
        String responsibleName,
        String responsibleEmail,
        LicenseStatus status,
        String notes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}