package com.novus.api_gateway.swagger;

import com.novus.shared_models.request.Notification.UpdateAuthenticatedUserNotificationPreferencesRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class NotificationControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update notification preferences",
            description = "Updates the notification preferences for the authenticated user. Allows users to opt in or out of different notification channels.",
            tags = {"Notification"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "Notification preferences to update",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdateAuthenticatedUserNotificationPreferencesRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Enable email notifications",
                                    summary = "Enable email notifications for the user",
                                    value = """
                                        {
                                          "email": true
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Disable email notifications",
                                    summary = "Disable email notifications for the user",
                                    value = """
                                        {
                                          "email": false
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Notification preferences successfully updated",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Notification preferences successfully updated. You will now receive notifications according to your selected settings."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Authentication is required to access this resource."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "An internal server error occurred. Please try again later."
                            )
                    )
            )
    })
    public @interface UpdateAuthenticatedUserNotificationPreferencesDoc {}

}
