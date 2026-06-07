package com.ajudaqui.porteiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    String description = "Esta API é responsável por fornecer autenticação de usuários a outros sistemas. Ela faz isso fornecendo um ponto único de entrada para a autenticação de usuários e fornecendo um conjunto de APIs que podem ser usadas para autenticar usuários em outros sistema";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Auth")
                        .description(description)
                        .version("1.0"));
    }
}
