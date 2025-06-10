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
 *
 * @see io.swagger.v3.oas.annotations.Hidden
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface HideInProduction {
    /**
     * Optional description of why this endpoint is hidden in production
     */
    String value() default "";
}
