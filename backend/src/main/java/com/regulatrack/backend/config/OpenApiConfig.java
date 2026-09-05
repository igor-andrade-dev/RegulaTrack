package com.regulatrack.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "RegulaTrack API",
                version = "1.0.0",
                description = "Backend API for managing companies, branches and regulatory licenses."
        )
)
public class OpenApiConfig {
}