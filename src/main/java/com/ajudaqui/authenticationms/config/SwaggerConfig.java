package com.ajudaqui.authenticationms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	String description="Esta API é responsável por fornecer autenticação de usuários a outros sistemas. Ela faz isso fornecendo um ponto único de entrada para a autenticação de usuários e fornecendo um conjunto de APIs que podem ser usadas para autenticar usuários em outros sistema";
	
	 @Bean
	    public Docket api() {
		 
	        return new Docket(DocumentationType.SWAGGER_2)
	                .select()
	                .apis(RequestHandlerSelectors.any())
	                .paths(PathSelectors.any())
	                .build()
	                .apiInfo(new ApiInfoBuilder()
	                    .title("My Auth")
	                    .description(description)
	                    .build());
	    }

}
