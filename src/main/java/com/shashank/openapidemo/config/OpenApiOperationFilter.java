package com.shashank.openapidemo.config;

import java.util.Arrays;
import java.util.Optional;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;

import com.shashank.openapidemo.annotation.HideInProduction;

/**
 * Configuration for defining a custom GroupedOpenApi that excludes
 * endpoints annotated with @HideInProduction when in production environment
 */
@Configuration
public class OpenApiOperationFilter {

    @Autowired
    private Environment environment;

    /**
     * Creates a GroupedOpenApi bean that filters operations based on annotations
     * 
     * @return Configured GroupedOpenApi with custom operation filter
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-api")
                .pathsToMatch("/**")
                // Add a custom predicate to filter out endpoints with @HideInProduction in prod
                .addOpenApiMethodFilter(method -> {
                    boolean isProdActive = Arrays.asList(environment.getActiveProfiles()).contains("prod");
                    if (!isProdActive) {
                        // In non-production environments, include all methods
                        return true;
                    }

                    // In production, exclude methods with @HideInProduction annotation
                    HideInProduction methodAnnotation = method.getAnnotation(HideInProduction.class);
                    HideInProduction classAnnotation = method.getDeclaringClass().getAnnotation(HideInProduction.class);

                    return methodAnnotation == null && classAnnotation == null;
                })
                .build();
    }
}
