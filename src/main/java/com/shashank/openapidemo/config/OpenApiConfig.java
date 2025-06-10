package com.shashank.openapidemo.config;

import com.shashank.openapidemo.annotation.HideInProduction;
import io.swagger.v3.oas.models.Operation;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;

/**
 * Configuration for OpenAPI documentation
 */
@Configuration
public class OpenApiConfig {

    private final Environment environment;

    public OpenApiConfig(Environment environment) {
        this.environment = environment;
    }


    /**
     * Creates a GroupedOpenApi bean that filters operations based on annotations.
     * The springdoc library ensures this filtering only happens during OpenAPI
     * documentation generation, not during regular request processing.
     *
     * @return Configured GroupedOpenApi with custom operation filter
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**")
                .addOperationCustomizer(hideEndpointsInProduction())
                .build();
    }

    /**
     * Creates an operation customizer that hides endpoints in production.
     * This customizer is only applied during OpenAPI documentation generation.
     *
     * @return OperationCustomizer that filters endpoints based on environment
     */
    @Bean
    public OperationCustomizer hideEndpointsInProduction() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            boolean isProdActive = Arrays.asList(environment.getActiveProfiles()).contains("prod");
            if (!isProdActive) {
                // In non-production environments, show all operations
                return operation;
            }

            // In production, hide operations with @HideInProduction annotation
            HideInProduction methodAnnotation = handlerMethod.getMethodAnnotation(HideInProduction.class);
            HideInProduction classAnnotation = handlerMethod.getBeanType().getAnnotation(HideInProduction.class);

            if (methodAnnotation != null || classAnnotation != null) {
                // Setting to null causes springdoc to exclude this operation
                return null;
            }

            return operation;
        };
    }

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
