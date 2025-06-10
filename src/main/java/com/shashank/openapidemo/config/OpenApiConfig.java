package com.shashank.openapidemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;

/**
 * Configuration for OpenAPI documentation
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI documentation for the application
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Greeting API")
                        .description("Spring Boot REST API for greeting messages")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Shashank")
                                .email("example@example.com")
                                .url("https://example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
