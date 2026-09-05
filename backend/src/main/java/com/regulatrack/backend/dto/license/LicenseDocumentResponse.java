package com.regulatrack.backend.dto.license;

import java.time.LocalDateTime;

public record LicenseDocumentResponse(
        Long id,
        String originalName,
        String contentType,
        long size,
        LocalDateTime uploadedAt
) {}
