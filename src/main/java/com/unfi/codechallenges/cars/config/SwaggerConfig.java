package com.unfi.codechallenges.cars.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class SwaggerConfig {
	
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("UNFI Test Project")
                        .version("1.0")
                        .description("UNFI Test Project API documentation using OpenAPI."));
    }
}


