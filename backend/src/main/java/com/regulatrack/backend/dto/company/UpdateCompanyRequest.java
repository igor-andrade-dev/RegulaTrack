package com.regulatrack.backend.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateCompanyRequest(

        @NotBlank(message = "Nome da empresa é obrigatório")
        @Size(max = 150, message = "Nome da empresa deve ter no máximo 150 caracteres")
        String name,

        @NotBlank(message = "Documento da empresa é obrigatório")
        @Size(max = 30, message = "Documento da empresa deve ter no máximo 30 caracteres")
        String documentNumber,

        @Size(max = 100, message = "Segmento deve ter no máximo 100 caracteres")
        String segment,

        @Size(max = 100, message = "País deve ter no máximo 100 caracteres")
        String country,

        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        String city
) {
}