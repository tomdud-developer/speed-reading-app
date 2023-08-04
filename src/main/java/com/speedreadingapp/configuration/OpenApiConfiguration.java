package com.speedreadingapp.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "tomdud-developer",
                        email = "tomasz.dudzik.it@gmail.com"
                ),
                description = "Documentation for SpeedReading Application.",
                title = "SpeedReading Application",
                version = "V2.0"
        )
)
@SecurityScheme(
        name = "BearerToken",
        description = "Authentication based on JWT token",
        scheme = "Bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {

}
