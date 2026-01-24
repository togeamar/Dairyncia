package com.dairyncia.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "DairyNcia API",
        version = "1.0",
        description = "API Documentation for Dairy Management System",
        contact = @Contact(name = "DairyNcia Support", email = "support@dairyncia.com")
    ),
    // This applies security globally to all endpoints
    security = { @SecurityRequirement(name = "bearerAuth") } 
)
@SecurityScheme(
    name = "bearerAuth",
    description = "JWT Auth description",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class SwaggerConfig {
    // No code needed here, annotations do the work
}