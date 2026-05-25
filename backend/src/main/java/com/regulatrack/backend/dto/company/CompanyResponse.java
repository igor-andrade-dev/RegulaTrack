package com.regulatrack.backend.dto.company;

import java.time.LocalDateTime;

public record CompanyResponse(
        Long id,
        String name,
        String documentNumber,
        String segment,
        String country,
        String city,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}