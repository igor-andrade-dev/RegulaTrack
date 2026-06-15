package com.regulatrack.backend.dto.license;

import com.regulatrack.backend.domain.license.LicenseStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateLicenseRequest(

        @NotNull(message = "Empresa é obrigatória")
        Long companyId,

        @NotNull(message = "Filial é obrigatória")
        Long branchId,

        @NotBlank(message = "Nome da licença é obrigatório")
        @Size(max = 150, message = "Nome da licença deve ter no máximo 150 caracteres")
        String name,

        String description,

        @Size(max = 100, message = "Categoria deve ter no máximo 100 caracteres")
        String category,

        @Size(max = 100, message = "Número da licença deve ter no máximo 100 caracteres")
        String licenseNumber,

        @Size(max = 150, message = "Órgão emissor deve ter no máximo 150 caracteres")
        String issuer,

        LocalDate issuedAt,

        LocalDate expiresAt,

        @Size(max = 150, message = "Nome do responsável deve ter no máximo 150 caracteres")
        String responsibleName,

        @Email(message = "E-mail do responsável inválido")
        @Size(max = 150, message = "E-mail do responsável deve ter no máximo 150 caracteres")
        String responsibleEmail,

        LicenseStatus status,

        String notes
) {
}