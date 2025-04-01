package com.novus.api_gateway.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class UserControllerDocs {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            tags = {"User"},
            summary = "Get the connected user's details",
            description = "Retrieve the details of the user associated with the current authentication token. Requires valid authentication token.",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "User retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "User Example",
                                    value = """
                                            {
                                                "id": "12345",
                                                "username": "alice123",
                                                "email": "alice@example.com",
                                                "role": "USER"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized: Invalid or missing authentication token",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Unauthorized Example",
                                    value = """
                                            {
                                                "status": 401,
                                                "error": "Unauthorized",
                                                "message": "You must be authenticated to access this resource"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "User Not Found Example",
                                    value = """
                                            {
                                                "status": 404,
                                                "error": "Not Found",
                                                "message": "User not found"
                                            }
                                            """
                            )
                    )
            )
    })
    public @interface GetAuthenticatedUserDetailsDoc {}
    public @interface DeleteAuthenticatedUserAccountDoc {}
    public @interface SetAuthenticatedUserProfileImageDoc {}
    public @interface UpdateAuthenticatedUserDetailsDoc {}
    public @interface CreateAdminAccountDoc {}
    public @interface DeleteAdminAccountDoc {}
    public @interface GetAllUsersDoc {}
    public @interface GetUserAdminDashboardDataDoc {}
    public @interface RateApplicationDoc {}
    public @interface UpdateUserLocationDoc {}

}
