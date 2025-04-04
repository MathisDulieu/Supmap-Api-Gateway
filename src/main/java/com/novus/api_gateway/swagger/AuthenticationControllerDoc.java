package com.novus.api_gateway.swagger;

import com.novus.shared_models.request.Authentication.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

public class AuthenticationControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Register a new user",
            description = "Allows a new user to register. A verification email will be sent to the provided address. The user must validate their account via the verification link before being able to log in.",
            tags = {"Authentication"}
    )
    @RequestBody(
            description = "User registration information",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Professional user",
                                    summary = "Professional user registration example",
                                    value = """
                                        {
                                          "username": "sarah_johnson42",
                                          "email": "sarah.johnson@company.com",
                                          "password": "S3cure@Pass2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Student user",
                                    summary = "Student user registration example",
                                    value = """
                                        {
                                          "username": "alex_student99",
                                          "email": "alex.student@university.edu",
                                          "password": "C0llege#Life2025"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Developer user",
                                    summary = "Developer user registration example",
                                    value = """
                                        {
                                          "username": "dev_marcus",
                                          "email": "marcus.dev@tech-company.io",
                                          "password": "C0d3M@ster!2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Personal user",
                                    summary = "Personal user registration example",
                                    value = """
                                        {
                                          "username": "emily_parker87",
                                          "email": "emily.parker87@gmail.com",
                                          "password": "Family&Home2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Business user",
                                    summary = "Business user registration example",
                                    value = """
                                        {
                                          "username": "michael_business",
                                          "email": "m.business@enterprise.org",
                                          "password": "Enterpr1se$Admin"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Registration successful. A verification email has been sent.",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Registration successful! A verification email has been sent to your address. Please check your inbox and follow the validation link before attempting to log in."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid registration data",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid email",
                                            summary = "Incorrect email format",
                                            value = "Invalid email format. Please enter a valid email address."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid username",
                                            summary = "Incorrect username format",
                                            value = "Invalid username. Username must be between 3-20 characters and contain only letters, numbers, and underscores."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid password",
                                            summary = "Password does not meet security requirements",
                                            value = "Password does not meet security requirements. Please use at least 8 characters including uppercase, lowercase, numbers, and special characters."
                                    ),
                                    @ExampleObject(
                                            name = "Username already used",
                                            summary = "Username already exists",
                                            value = "Username already exists. Please choose a different username."
                                    ),
                                    @ExampleObject(
                                            name = "Email already used",
                                            summary = "Email address is already registered",
                                            value = "Email address is already registered. Please use a different email or try to recover your account."
                                    )
                            }
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
    public @interface RegisterDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "User login",
            description = "Authenticates a user and provides a JWT token for authorized access to protected resources.",
            tags = {"Authentication"}
    )
    @RequestBody(
            description = "User login credentials",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Professional user",
                                    summary = "Professional user login example",
                                    value = """
                                        {
                                          "email": "sarah.johnson@company.com",
                                          "password": "S3cure@Pass2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Student user",
                                    summary = "Student user login example",
                                    value = """
                                        {
                                          "email": "alex.student@university.edu",
                                          "password": "C0llege#Life2025"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Developer user",
                                    summary = "Developer user login example",
                                    value = """
                                        {
                                          "email": "marcus.dev@tech-company.io",
                                          "password": "C0d3M@ster!2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Personal user",
                                    summary = "Personal user login example",
                                    value = """
                                        {
                                          "email": "emily.parker87@gmail.com",
                                          "password": "Family&Home2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Business user",
                                    summary = "Business user login example",
                                    value = """
                                        {
                                          "email": "m.business@enterprise.org",
                                          "password": "Enterpr1se$Admin"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, returns a JWT token",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Invalid password. Please try again."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "User not found. Please check your email address or register for a new account."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email not verified",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Email not verified. Please check your inbox and follow the validation link sent during registration."
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
    public @interface LoginDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Confirm user email address",
            description = "Validates a user's email address by verifying the token sent during registration. " +
                    "After successful verification, the user can log in to their account.",
            tags = {"Authentication"}
    )
    @Parameter(
            name = "token",
            description = "The JWT token received via email during registration",
            required = true,
            example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcklkIjoiYWJjMTIzIiwiZW1haWwiOiJ1c2VyQGV4YW1wbGUuY29tIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyNDI2MjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Email successfully verified",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Email successfully verified! You can now log in to your account."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid token or email already verified",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid token",
                                            summary = "The verification token is invalid or expired",
                                            value = "Invalid or expired verification token. Please request a new verification email."
                                    ),
                                    @ExampleObject(
                                            name = "Already verified",
                                            summary = "Email has already been verified",
                                            value = "Email already verified. You can log in to your account."
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "User account not found. Please register again."
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
    public @interface ConfirmEmailDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Resend registration confirmation email",
            description = "Sends a new verification email to a user who previously registered but hasn't confirmed their email address yet.",
            tags = {"Authentication"}
    )
    @RequestBody(
            description = "User email for resending confirmation",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResendRegisterConfirmationEmailRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Professional user",
                                    summary = "Professional user email example",
                                    value = """
                                        {
                                          "email": "sarah.johnson@company.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Student user",
                                    summary = "Student user email example",
                                    value = """
                                        {
                                          "email": "alex.student@university.edu"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Developer user",
                                    summary = "Developer user email example",
                                    value = """
                                        {
                                          "email": "marcus.dev@tech-company.io"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Personal user",
                                    summary = "Personal user email example",
                                    value = """
                                        {
                                          "email": "emily.parker87@gmail.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Business user",
                                    summary = "Business user email example",
                                    value = """
                                        {
                                          "email": "m.business@enterprise.org"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Confirmation email successfully resent",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Verification email has been resent. Please check your inbox and follow the validation link to activate your account."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email already verified",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Email already verified. You can log in to your account."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "User not found. Please check your email address or register for a new account."
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
    public @interface ResendRegisterConfirmationEmailDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Send password reset email",
            description = "Sends a password reset email to the specified user. The email contains a link with a token that allows the user to create a new password.",
            tags = {"Authentication"}
    )
    @RequestBody(
            description = "User email for password reset",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SendForgotPasswordRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Professional user",
                                    summary = "Professional user email example",
                                    value = """
                                        {
                                          "email": "sarah.johnson@company.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Student user",
                                    summary = "Student user email example",
                                    value = """
                                        {
                                          "email": "alex.student@university.edu"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Developer user",
                                    summary = "Developer user email example",
                                    value = """
                                        {
                                          "email": "marcus.dev@tech-company.io"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Personal user",
                                    summary = "Personal user email example",
                                    value = """
                                        {
                                          "email": "emily.parker87@gmail.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Business user",
                                    summary = "Business user email example",
                                    value = """
                                        {
                                          "email": "m.business@enterprise.org"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password reset email successfully sent",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Password reset email has been sent. Please check your inbox and follow the instructions to reset your password."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "User not found. Please check your email address or register for a new account."
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
    public @interface SendForgotPasswordEmailDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Reset user password",
            description = "Sets a new password for a user using the token received via email. After successful password reset, the user can log in with the new password.",
            tags = {"Authentication"}
    )
    @RequestBody(
            description = "Password reset information with token and new password",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ResetPasswordRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Professional user",
                                    summary = "Professional user password reset example",
                                    value = """
                                        {
                                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcklkIjoiYWJjMTIzIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyNDI2MjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                                          "newPassword": "New$ecure@Pass2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Student user",
                                    summary = "Student user password reset example",
                                    value = """
                                        {
                                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcklkIjoiZGVmNDU2IiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyNDI2MjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                                          "newPassword": "Campus#Str0ng2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Developer user",
                                    summary = "Developer user password reset example",
                                    value = """
                                        {
                                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcklkIjoiZ2hpNzg5IiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYyNDI2MjJ9.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                                          "newPassword": "C0d3&D3v!2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Personal user",
                                    summary = "Personal user password reset example",
                                    value = """
                                        {
                                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcklkIjoiamtsMTAxMiIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjQyNjIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                                          "newPassword": "Personal&H0me2024"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Business user",
                                    summary = "Business user password reset example",
                                    value = """
                                        {
                                          "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcklkIjoibW5vMTMxNCIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjQyNjIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c",
                                          "newPassword": "Business$Secure2024!"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Password successfully reset",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Password has been successfully reset. You can now log in with your new password."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or password requirements not met",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Weak password",
                                            summary = "Password does not meet security requirements",
                                            value = "Password is not strong enough. Please use at least 8 characters including uppercase, lowercase, numbers, and special characters."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid token",
                                            summary = "The reset token is invalid or expired",
                                            value = "Invalid or expired password reset token. Please request a new password reset link."
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Unauthorized access. User information could not be verified from the token."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "User account not found. Please contact support for assistance."
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
    public @interface ResetPasswordDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Login with Google OAuth",
            description = "Authenticates a user using Google OAuth credentials. The user must already have an account registered with the same email address as their Google account.",
            tags = {"Authentication"},
            security = @SecurityRequirement(name = "oauth2", scopes = {"openid", "email", "profile"})
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Authentication successful, returns a JWT token",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Email not provided by Google",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Email not provided by Google authentication. Please ensure your Google account has a verified email address."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Unauthorized access. Google OAuth authentication failed."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "No account found with this Google email. Please register first."
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
    public @interface GoogleLoginDoc {}

}
