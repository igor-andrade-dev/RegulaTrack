package com.regulatrack.backend.dto.branch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateBranchRequest(

        @NotNull(message = "O id da empresa é obrigatório")
        Long companyId,

        @NotBlank(message = "O nome da unidade é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String name,

        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
        String address,

        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
        String city,

        @Size(max = 100, message = "O estado deve ter no máximo 100 caracteres")
        String state,

        @Size(max = 100, message = "O país deve ter no máximo 100 caracteres")
        String country
) {
}