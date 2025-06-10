package com.shashank.openapidemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.shashank.openapidemo.annotation.HideInProduction;

/**
 * Controller providing basic greeting endpoints
 */
@RestController
@Tag(name = "Greeting", description = "Greeting API with various salutation endpoints")
public class GreetingController {

    /**
     * Returns a hello message
     * This endpoint's documentation is hidden in production environment
     * @return greeting message
     */
    @Operation(
        summary = "Get a hello greeting",
        description = "Returns a simple hello message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
        }
    )
    @GetMapping("/hello")
    @HideInProduction("Security sensitive endpoint hidden in production")
    public String sayHello() {
        return "Hello";
    }

    /**
     * Returns a hi message
     * @return greeting message
     */
    @Operation(
        summary = "Get a hi greeting",
        description = "Returns a simple hi message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
        }
    )
    @GetMapping("/hi")
    public String sayHi() {
        return "Hi";
    }

    /**
     * Returns a bye message
     * @return farewell message
     */
    @Operation(
        summary = "Get a bye farewell",
        description = "Returns a simple bye message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
        }
    )
    @GetMapping("/bye")
    public String sayBye() {
        return "Bye";
    }

    /**
     * Returns a goodbye message
     * @return farewell message
     */
    @Operation(
        summary = "Get a goodbye farewell",
        description = "Returns a simple goodbye message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Successful operation",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string"))
            )
        }
    )
    @GetMapping("/goodbye")
    public String sayGoodbye() {
        return "Goodbye";
    }
}
