package com.tecnoinf.gestedu.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.beans.factory.annotation.Configurable;

@Configurable
@OpenAPIDefinition(
        info = @Info(
                title = "Prueba Concepto API",
                version = "1.0",
                description = "Prueba Concepto API Documentation"
        )
)
public class OpenApiConfig {
}
