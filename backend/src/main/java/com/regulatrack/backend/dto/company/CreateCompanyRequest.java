package com.regulatrack.backend.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCompanyRequest(

        @NotBlank(message = "O nome da empresa é obrigatório")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String name,

        @Size(max = 50, message = "O documento deve ter no máximo 50 caracteres")
        String documentNumber,

        @Size(max = 100, message = "O segmento deve ter no máximo 100 caracteres")
        String segment,

        @Size(max = 100, message = "O país deve ter no máximo 100 caracteres")
        String country,

        @Size(max = 100, message = "A cidade deve ter no máximo 100 caracteres")
        String city
) {
}