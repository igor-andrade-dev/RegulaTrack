package com.regulatrack.backend.dto.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateBranchRequest(

        @NotNull(message = "Empresa é obrigatória")
        Long companyId,

        @NotBlank(message = "Nome da filial é obrigatório")
        @Size(max = 150, message = "Nome da filial deve ter no máximo 150 caracteres")
        String name,

        @Size(max = 150, message = "Endereço deve ter no máximo 150 caracteres")
        String address,

        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        String city,

        @Size(max = 50, message = "Estado deve ter no máximo 50 caracteres")
        String state,

        @Size(max = 100, message = "País deve ter no máximo 100 caracteres")
        String country
) {
}