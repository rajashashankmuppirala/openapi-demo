# Selective OpenAPI Documentation by Environment

[![Java](https://img.shields.io/badge/Java-22-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Latest-green)](https://spring.io/projects/spring-boot)
[![Tomcat](https://img.shields.io/badge/Tomcat-Compatible-red)](https://tomcat.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A Spring-based application that demonstrates how to selectively filter OpenAPI/Swagger documentation based on the active environment profile. This implementation allows you to hide specific endpoints from appearing in your API documentation in production environments, while keeping the actual endpoints fully functional.

Importantly, the filtering only affects documentation generation and does not impact the actual API endpoints or application performance.

## Key Features

- **Environment-Aware Documentation**: Hides sensitive endpoints from appearing in OpenAPI/Swagger documentation when in production
- **Endpoint Visibility Control**: All API endpoints remain fully functional even when hidden from documentation
- **Performance Optimized**: Filtering logic only executes during Swagger/OpenAPI documentation requests
- **Tomcat Compatible**: Works perfectly when deployed as a WAR on Tomcat
- **Production-Ready**: Includes caching and optimized scanning for improved performance

## How It Works

The application uses a custom annotation (`@HideInProduction`) that can be applied to controllers or individual methods. When running with the "prod" profile, the OpenAPI documentation generator will automatically exclude these endpoints from the Swagger UI, while the actual API endpoints remain fully accessible and functional.

### Implementation Details

1. **Custom Annotation**: The `@HideInProduction` annotation marks API operations that should be hidden from documentation in production

2. **Documentation-Only Filtering**: The filtering only affects what appears in the Swagger UI and OpenAPI specification - all endpoints remain fully functional

3. **Environment-Aware**: Uses Spring's environment awareness to detect the active profile

4. **OpenAPI Operation Customizer**: Implements an `OperationCustomizer` that selectively includes/excludes operations from documentation

5. **Performance Optimization**: 
   - Filtering logic only runs during Swagger UI/OpenAPI documentation requests
   - Includes caching to prevent re-scanning on every documentation request
   - Configures package scanning to improve startup and runtime performance

## Getting Started

### Prerequisites

- Java 22 or later
- Maven or Gradle

### Running the Application

#### Development Mode

```bash
# Run with dev profile (all endpoints visible)
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

#### Production Mode

```bash
# Run with prod profile (endpoints with @HideInProduction hidden)
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

### Deploying to Tomcat

1. Build the WAR file:
   ```bash
   ./mvnw clean package
   ```

2. Deploy the resulting WAR file to your Tomcat server

3. Set the active profile in Tomcat:
   - Edit `catalina.properties` or use `-Dspring.profiles.active=prod` in `CATALINA_OPTS`

## Usage Examples

### Annotating Controllers or Methods for Documentation Filtering

```java
// Hide entire controller from Swagger/OpenAPI documentation in production
@RestController
@RequestMapping("/admin")
@HideInProduction("Administrative endpoints hidden from API docs in production")
public class AdminController {
    // All endpoints in this controller will be hidden from API docs in production
    // But the endpoints themselves remain fully functional!

    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard() {
        // This endpoint works in all environments but won't appear in production Swagger docs
        return ResponseEntity.ok().build();
    }
}

// Hide specific methods from Swagger/OpenAPI documentation in production
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        // Always visible in documentation across all environments
        return ResponseEntity.ok().build();
    }

    @GetMapping("/internal")
    @HideInProduction("Internal endpoint hidden from API docs in production")
    public ResponseEntity<?> internalEndpoint() {
        // Endpoint is fully functional in all environments
        // But will only appear in Swagger documentation in non-production environments
        return ResponseEntity.ok().build();
    }
}
```

## Configuration Properties

Key properties in `application.properties`:

```properties
# OpenAPI Documentation Paths
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui

# Performance Optimization
springdoc.cache.disabled=false
springdoc.packages-to-scan=com.yourcompany.controllers
```

## How the Documentation Filtering Works

This implementation is carefully designed to ensure the filtering logic only affects documentation and doesn't impact your API functionality:

1. The `OperationCustomizer` bean is only invoked during OpenAPI documentation generation

2. For regular API requests, none of the filtering logic is executed

3. Annotated endpoints remain fully functional in all environments, they're simply not listed in the Swagger UI or OpenAPI specification when in production

4. The filtering process uses Spring's environment detection to determine when to hide endpoints

5. The caching configuration prevents re-scanning on every documentation request

This approach separates documentation concerns from API functionality, ensuring optimal performance while maintaining security through documentation hiding in production.

## The HideInProduction Annotation

The custom annotation is defined as:

```java
package com.shashank.openapidemo.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.swagger.v3.oas.annotations.Hidden;

/**
 * Annotation to mark controller methods that should have their
 * Swagger/OpenAPI documentation hidden when running in production profile.
 *
 * This is a functional replacement for conditionally applying the @Hidden annotation
 * based on the active Spring profile.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HideInProduction {
    /**
     * Optional description of why this endpoint is hidden in production
     */
    String value() default "";
}
```

Unlike the standard `@Hidden` annotation, this custom annotation only takes effect in production environments, allowing for environment-specific documentation.

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
