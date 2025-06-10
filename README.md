# OpenAPI Documentation with Production Endpoint Hiding

[![Java](https://img.shields.io/badge/Java-22-orange)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Latest-green)](https://spring.io/projects/spring-boot)
[![Tomcat](https://img.shields.io/badge/Tomcat-Compatible-red)](https://tomcat.apache.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A Spring-based application that demonstrates how to implement environment-specific API documentation with OpenAPI and Swagger UI. This solution allows you to hide specific endpoints in production environments while keeping them visible in development.

## Key Features

- **Environment-Aware Documentation**: Automatically hides endpoints in production that are annotated with `@HideInProduction`
- **Performance Optimized**: Filtering logic only executes during Swagger/OpenAPI documentation requests
- **Tomcat Compatible**: Works perfectly when deployed as a WAR on Tomcat
- **Production-Ready**: Includes caching and optimized scanning for improved performance

## How It Works

The application uses a custom annotation (`@HideInProduction`) that can be applied to controllers or individual methods. When running with the "prod" profile, the OpenAPI documentation will automatically exclude these endpoints from the Swagger UI.

### Implementation Details

1. **Custom Annotation**: The `@HideInProduction` annotation marks endpoints that should be hidden in production

2. **OpenAPI Configuration**: Uses Spring's environment awareness to detect the active profile

3. **Operation Filtering**: Implements an `OperationCustomizer` that selectively includes/excludes endpoints

4. **Performance Optimization**: 
   - Filtering logic only runs during Swagger UI/OpenAPI documentation requests
   - Includes caching to prevent re-scanning on every request
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

### Annotating Controllers or Methods

```java
// Hide an entire controller in production
@RestController
@RequestMapping("/admin")
@HideInProduction
public class AdminController {
    // All endpoints in this controller will be hidden in production
}

// Hide specific methods in production
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        // Always visible in all environments
        return ResponseEntity.ok().build();
    }

    @GetMapping("/internal")
    @HideInProduction
    public ResponseEntity<?> internalEndpoint() {
        // Hidden in production, visible in dev
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

## How the Performance Optimization Works

This implementation is carefully designed to ensure that the endpoint filtering logic only runs when Swagger UI or OpenAPI documentation endpoints are accessed:

1. The `OperationCustomizer` bean is only invoked during OpenAPI documentation generation

2. For regular API requests, none of the filtering logic is executed

3. The caching configuration prevents re-scanning on every documentation request

4. Limiting package scanning improves both startup time and runtime performance

This means that your API performance is not affected by the documentation features, even in production environments.

## License

This project is licensed under the Apache License 2.0 - see the LICENSE file for details.
