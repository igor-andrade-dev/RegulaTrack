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
        String issuer,
        LocalDate expiresAt,
        LicenseStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}