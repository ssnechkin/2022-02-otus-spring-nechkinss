package ru.rncb.dpec.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Dp-config API", version = "v1"))
//http://localhost:8080/swagger-ui/index.html
public class SwaggerConfig {
}