package com.regulatrack.backend.dto.branch;

import java.time.LocalDateTime;

public record BranchResponse(
        Long id,
        Long companyId,
        String companyName,
        String name,
        String address,
        String city,
        String state,
        String country,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}