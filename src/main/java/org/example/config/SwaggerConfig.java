package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Swagger/OpenAPI documentation.
 * Sets up API metadata for the RAG Chat Storage microservice.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Creates and configures the OpenAPI bean for API documentation.
     * @return configured OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("RAG Chat Storage API")
                        .version("1.0")
                        .description("API documentation for the RAG Chat Storage microservice."));
    }
}
