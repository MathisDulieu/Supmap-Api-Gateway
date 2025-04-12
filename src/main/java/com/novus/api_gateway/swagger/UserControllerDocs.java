package com.novus.api_gateway.swagger;

import com.novus.shared_models.request.User.CreateAdminAccountRequest;
import com.novus.shared_models.request.User.RateApplicationRequest;
import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import com.novus.shared_models.request.User.UpdateUserLocationRequest;
import com.novus.shared_models.response.User.GetAllUsersResponse;
import com.novus.shared_models.response.User.GetAuthenticatedUserDetailsResponse;
import com.novus.shared_models.response.User.GetUserAdminDashboardDataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

public class UserControllerDocs {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get authenticated user details",
            description = "Retrieves the complete profile and preference information for the currently authenticated user.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User details successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetAuthenticatedUserDetailsResponse.class),
                            examples = @ExampleObject(
                                    name = "Complete user profile",
                                    summary = "Full user profile with all information",
                                    value = """
                                        {
                                          "username": "sarah_johnson42",
                                          "email": "sarah.johnson@company.com",
                                          "profileImage": "https://storage.example.com/profiles/sarah_johnson42.jpg",
                                          "role": "USER",
                                          "lastKnownLocation": {
                                            "latitude": 48.8566,
                                            "longitude": 2.3522
                                          },
                                          "navigationPreferences": {
                                            "avoidTolls": true,
                                            "avoidHighways": false,
                                            "avoidTraffic": true,
                                            "showUsers": true,
                                            "proximityAlertDistance": 750,
                                            "preferredTransportMode": "CAR"
                                          },
                                          "favoriteLocations": [
                                            {
                                              "name": "Home",
                                              "formattedAddress": "123 Residential St, Paris, 75008, France",
                                              "coordinates": {
                                                "latitude": 48.8659,
                                                "longitude": 2.3501
                                              },
                                              "placeId": "ChIJH2TTo6RlwoARyc-ot3DTscA",
                                              "street": "123 Residential St",
                                              "city": "Paris",
                                              "postalCode": "75008",
                                              "country": "France",
                                              "locationType": "HOME"
                                            },
                                            {
                                              "name": "Office",
                                              "formattedAddress": "456 Business Ave, Paris, 75002, France",
                                              "coordinates": {
                                                "latitude": 48.8698,
                                                "longitude": 2.3529
                                              },
                                              "placeId": "ChIJH3Tla8RlwoYRzc-ot4ETayA",
                                              "street": "456 Business Ave",
                                              "city": "Paris",
                                              "postalCode": "75002",
                                              "country": "France",
                                              "locationType": "WORK"
                                            },
                                            {
                                              "name": "Gym",
                                              "formattedAddress": "789 Fitness Blvd, Paris, 75015, France",
                                              "coordinates": {
                                                "latitude": 48.8417,
                                                "longitude": 2.3238
                                              },
                                              "placeId": "ChIJd5TkX7RlwoBRyc-oq1DTscA",
                                              "street": "789 Fitness Blvd",
                                              "city": "Paris",
                                              "postalCode": "75015",
                                              "country": "France",
                                              "locationType": "FAVORITE"
                                            }
                                          ],
                                          "stats": {
                                            "totalReportsSubmitted": 153,
                                            "validatedReports": 142,
                                            "totalRoutesCompleted": 207,
                                            "totalDistanceTraveled": 5482,
                                            "totalTimeSaved": 724,
                                            "reportsValidatedByOthers": 118,
                                            "rank": "Navigator Pro",
                                            "rankImage": "https://storage.example.com/ranks/navigator_pro.png",
                                            "error": null
                                          },
                                          "notificationSettings": {
                                            "emailEnabled": true
                                          },
                                          "isValidEmail": true,
                                          "hasVoted": false,
                                          "error": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "error": "Authentication is required to access this resource."
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
                                    value = """
                                        {
                                          "error": "User account not found."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "error": "An internal server error occurred. Please try again later."
                                        }
                                        """
                            )
                    )
            )
    })
    public @interface GetAuthenticatedUserDetailsDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Delete user account",
            description = "Permanently deletes the authenticated user's account and all associated data from the system.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account successfully deleted",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Your account has been successfully deleted. All personal data has been removed from our system. We're sorry to see you go!"
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
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "User account not found."
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
    public @interface DeleteAuthenticatedUserAccountDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Set user profile image",
            description = "Uploads and sets a new profile image for the authenticated user. Accepts image files (JPEG, PNG, etc.) as multipart/form-data.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            name = "file",
            description = "Profile image file to upload",
            required = true,
            in = ParameterIn.DEFAULT,
            content = @Content(mediaType = "multipart/form-data")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile image successfully updated",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Profile image successfully updated! Your new profile picture is now visible to other users."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "No image file provided or invalid image format",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "No image file provided. Please select an image to upload."
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
                    responseCode = "413",
                    description = "File too large",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "The uploaded file exceeds the maximum allowed size."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Failed to process the image. Please try again with a different image or contact support if the issue persists."
                            )
                    )
            )
    })
    public @interface SetAuthenticatedUserProfileImageDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update user profile information",
            description = "Updates the authenticated user's profile information, including username, email, or password. At least one field must be provided to make changes.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "User profile information to update",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdateAuthenticatedUserDetailsRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Update username only",
                                    summary = "Update only the username",
                                    value = """
                                        {
                                          "username": "sarah_johnson_updated"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Update email only",
                                    summary = "Update only the email address",
                                    value = """
                                        {
                                          "email": "sarah.johnson.new@company.com"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Update password only",
                                    summary = "Update only the password",
                                    value = """
                                        {
                                          "oldPassword": "S3cure@Pass2024",
                                          "newPassword": "NewS3cure@Pass2025"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Update all fields",
                                    summary = "Update username, email, and password",
                                    value = """
                                        {
                                          "username": "sarah_johnson_updated",
                                          "email": "sarah.johnson.new@company.com",
                                          "oldPassword": "S3cure@Pass2024",
                                          "newPassword": "NewS3cure@Pass2025"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Profile information successfully updated",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Your profile information has been successfully updated. The changes are now visible in your account."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation errors",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "No fields provided",
                                            summary = "No fields to update were provided",
                                            value = "At least one field (email, username, or password) must be provided to update your profile. Please specify what you want to change."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid username",
                                            summary = "Username format is invalid",
                                            value = "Invalid username format. Username must be between 3-20 characters and contain only letters, numbers, and underscores."
                                    ),
                                    @ExampleObject(
                                            name = "Username taken",
                                            summary = "Username is already in use",
                                            value = "This username is already taken. Please choose a different username."
                                    ),
                                    @ExampleObject(
                                            name = "Same username",
                                            summary = "New username is the same as current",
                                            value = "The new username is the same as your current username. Please enter a different username to make a change."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid email",
                                            summary = "Email format is invalid",
                                            value = "Invalid email format. Please enter a valid email address."
                                    ),
                                    @ExampleObject(
                                            name = "Email taken",
                                            summary = "Email is already in use",
                                            value = "This email address is already registered with another account. Please use a different email address."
                                    ),
                                    @ExampleObject(
                                            name = "Same email",
                                            summary = "New email is the same as current",
                                            value = "The new email is the same as your current email. Please enter a different email address to make a change."
                                    ),
                                    @ExampleObject(
                                            name = "Weak password",
                                            summary = "Password doesn't meet requirements",
                                            value = "Password does not meet security requirements. Please use at least 8 characters including uppercase, lowercase, numbers, and special characters."
                                    ),
                                    @ExampleObject(
                                            name = "Same password",
                                            summary = "New password is the same as current",
                                            value = "The new password cannot be the same as your current password. Please choose a different password."
                                    ),
                                    @ExampleObject(
                                            name = "Incorrect old password",
                                            summary = "Current password verification failed",
                                            value = "Incorrect current password. Please enter your current password correctly to verify your identity."
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
    public @interface UpdateAuthenticatedUserDetailsDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Create a new administrator account",
            description = "Creates a new administrator account with the provided credentials. This endpoint is only accessible to users with admin privileges.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "Admin account details",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = CreateAdminAccountRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Standard admin account",
                                    summary = "Create standard admin account",
                                    value = """
                                        {
                                          "username": "admin_johnson",
                                          "email": "admin.johnson@company.com",
                                          "password": "Adm1n@S3cure2024",
                                          "profileImage": "https://storage.example.com/profiles/default_admin.jpg",
                                          "isValidEmail": true
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Super admin account",
                                    summary = "Create super admin account",
                                    value = """
                                        {
                                          "username": "super_admin",
                                          "email": "super.admin@company.com",
                                          "password": "Sup3rAdm1n#2024",
                                          "profileImage": "https://storage.example.com/profiles/super_admin.jpg",
                                          "isValidEmail": true
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Support admin account",
                                    summary = "Create support admin account",
                                    value = """
                                        {
                                          "username": "support_admin",
                                          "email": "support.admin@company.com",
                                          "password": "Supp0rt@Adm1n2024",
                                          "profileImage": "",
                                          "isValidEmail": true
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Administrator account successfully created",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Admin account successfully created. The new administrator can now log in with the provided credentials."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request or validation errors",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid username",
                                            summary = "Username format is invalid",
                                            value = "Invalid username format. Username must be between 3-20 characters and contain only letters, numbers, and underscores."
                                    ),
                                    @ExampleObject(
                                            name = "Username taken",
                                            summary = "Username is already in use",
                                            value = "Username already exists. Please choose a different username."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid email",
                                            summary = "Email format is invalid",
                                            value = "Invalid email format. Please enter a valid email address."
                                    ),
                                    @ExampleObject(
                                            name = "Email taken",
                                            summary = "Email is already in use",
                                            value = "Email address is already registered. Please use a different email."
                                    ),
                                    @ExampleObject(
                                            name = "Weak password",
                                            summary = "Password doesn't meet requirements",
                                            value = "Password does not meet security requirements. Please use at least 8 characters including uppercase, lowercase, numbers, and special characters."
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
                                    value = "Authentication is required to access this resource."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "You do not have sufficient permissions to create administrator accounts."
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
    public @interface CreateAdminAccountDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Delete an administrator account",
            description = "Permanently removes an administrator account from the system. This endpoint is only accessible to users with admin privileges.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            name = "id",
            description = "ID of the administrator account to delete",
            required = true,
            example = "a7b9c3d5-e8f2-41g6-h1i7-j9k2l4m6n8o1"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Administrator account successfully deleted",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Admin account successfully deleted. The user will no longer have administrator privileges."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request - user is not an administrator",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "This operation can only be performed on admin accounts. The specified user is not an administrator."
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
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "You do not have sufficient permissions to delete administrator accounts."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Administrator account not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "User not found. The specified admin account does not exist or has already been deleted."
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
    public @interface DeleteAdminAccountDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get all users",
            description = "Retrieves a paginated list of all users in the system, with optional filtering by username keyword. This endpoint is only accessible to users with admin privileges.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            name = "keyword",
            description = "Optional search keyword to filter users by username prefix",
            example = "john"
    )
    @Parameter(
            name = "page",
            description = "Page number for pagination (zero-based indexing)",
            example = "0"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetAllUsersResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "users": [
                                            {
                                              "id": "6428a7b9c3d5e8f241g6h1i7",
                                              "username": "sarah_johnson42",
                                              "email": "sarah.johnson@company.com",
                                              "password": "$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS",
                                              "profileImage": "https://storage.example.com/profiles/sarah_johnson42.jpg",
                                              "role": "USER",
                                              "createdAt": "2024-02-15T10:30:45.123Z",
                                              "updatedAt": "2024-03-10T14:25:12.456Z",
                                              "todayStatus": "ACTIVE",
                                              "monthStatus": "ACTIVE",
                                              "lastKnownLocation": {
                                                "latitude": 48.8566,
                                                "longitude": 2.3522
                                              },
                                              "navigationPreferences": {
                                                "avoidTolls": true,
                                                "avoidHighways": false,
                                                "avoidTraffic": true,
                                                "showUsers": true,
                                                "proximityAlertDistance": 750,
                                                "preferredTransportMode": "CAR"
                                              },
                                              "recentLocationIds": [
                                                "loc123", "loc456", "loc789"
                                              ],
                                              "recentRouteIds": [
                                                "route123", "route456"
                                              ],
                                              "favoriteLocationIds": [
                                                "fav123", "fav456", "fav789"
                                              ],
                                              "stats": {
                                                "totalReportsSubmitted": 153,
                                                "validatedReports": 142,
                                                "invalidatedReports": 11,
                                                "totalRoutesCompleted": 207,
                                                "totalDistanceTraveled": 5482,
                                                "totalTimeSaved": 724,
                                                "rank": "NAVIGATION_LEGEND",
                                                "rankImage": "https://storage.example.com/ranks/navigation_legend.png",
                                                "lastActiveDate": "2024-04-03T18:45:22.789Z",
                                                "trustScore": 92,
                                                "reportsValidatedByOthers": 118
                                              },
                                              "notificationSettings": {
                                                "emailEnabled": true
                                              },
                                              "isValidEmail": true,
                                              "lastLoginDate": "2024-04-03T18:30:00.123Z",
                                              "lastActivityDate": "2024-04-03T19:15:42.456Z",
                                              "lastLoginIp": "192.168.1.100"
                                            },
                                            {
                                              "id": "7539a8b2c1d4e6f340g5h9i3",
                                              "username": "alex_student99",
                                              "email": "alex.student@university.edu",
                                              "password": "$2a$10$zKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS",
                                              "profileImage": "https://storage.example.com/profiles/alex_student99.jpg",
                                              "role": "USER",
                                              "createdAt": "2024-01-20T08:15:30.789Z",
                                              "updatedAt": "2024-03-05T11:40:22.123Z",
                                              "todayStatus": "INACTIVE",
                                              "monthStatus": "ACTIVE",
                                              "lastKnownLocation": {
                                                "latitude": 40.7128,
                                                "longitude": -74.0060
                                              },
                                              "navigationPreferences": {
                                                "avoidTolls": false,
                                                "avoidHighways": true,
                                                "avoidTraffic": true,
                                                "showUsers": false,
                                                "proximityAlertDistance": 300,
                                                "preferredTransportMode": "BICYCLE"
                                              },
                                              "recentLocationIds": [
                                                "loc234", "loc567", "loc890"
                                              ],
                                              "recentRouteIds": [
                                                "route234", "route567", "route890"
                                              ],
                                              "favoriteLocationIds": [
                                                "fav234", "fav567"
                                              ],
                                              "stats": {
                                                "totalReportsSubmitted": 87,
                                                "validatedReports": 76,
                                                "invalidatedReports": 11,
                                                "totalRoutesCompleted": 124,
                                                "totalDistanceTraveled": 2310,
                                                "totalTimeSaved": 346,
                                                "rank": "ROAD_EXPLORER",
                                                "rankImage": "https://storage.example.com/ranks/road_explorer.png",
                                                "lastActiveDate": "2024-04-01T09:30:15.456Z",
                                                "trustScore": 78,
                                                "reportsValidatedByOthers": 62
                                              },
                                              "notificationSettings": {
                                                "emailEnabled": false
                                              },
                                              "isValidEmail": true,
                                              "lastLoginDate": "2024-04-01T09:15:00.789Z",
                                              "lastActivityDate": "2024-04-01T10:45:33.123Z",
                                              "lastLoginIp": "192.168.2.200"
                                            }
                                          ],
                                          "totalPages": 5,
                                          "error": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid page",
                                            summary = "Page number is negative",
                                            value = """
                                                {
                                                  "users": null,
                                                  "totalPages": 0,
                                                  "error": "Invalid page number. Page number must be zero or a positive integer."
                                                }
                                                """
                                    ),
                                    @ExampleObject(
                                            name = "Invalid keyword",
                                            summary = "Keyword contains spaces",
                                            value = """
                                                {
                                                  "users": null,
                                                  "totalPages": 0,
                                                  "error": "Search keyword cannot contain spaces. Please use a single word or connected words."
                                                }
                                                """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "users": null,
                                          "totalPages": 0,
                                          "error": "Authentication is required to access this resource."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "users": null,
                                          "totalPages": 0,
                                          "error": "You do not have sufficient permissions to access user data."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "users": null,
                                          "totalPages": 0,
                                          "error": "An internal server error occurred. Please try again later."
                                        }
                                        """
                            )
                    )
            )
    })
    public @interface GetAllUsersDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get admin dashboard data",
            description = "Retrieves statistical data for the admin dashboard, including user activity metrics, growth statistics, top contributors, and app ratings. This endpoint is only accessible to users with admin privileges.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Dashboard data successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetUserAdminDashboardDataResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "appRatingByNumberOfRate": {
                                            "841": 4.2
                                          },
                                          "topContributors": [
                                            {
                                              "userId": "6428a7b9c3d5e8f241g6h1i7",
                                              "username": "sarah_johnson42",
                                              "contributionCount": 153,
                                              "trustScore": 92.5,
                                              "rank": "NAVIGATION_LEGEND",
                                              "rankImage": "https://storage.example.com/ranks/navigation_legend.png"
                                            },
                                            {
                                              "userId": "7539a8b2c1d4e6f340g5h9i3",
                                              "username": "alex_student99",
                                              "contributionCount": 129,
                                              "trustScore": 88.3,
                                              "rank": "ROUTE_MASTER",
                                              "rankImage": "https://storage.example.com/ranks/route_master.png"
                                            },
                                            {
                                              "userId": "8642b9c5d7e2f3a481j6k2l4",
                                              "username": "dev_marcus",
                                              "contributionCount": 112,
                                              "trustScore": 85.7,
                                              "rank": "TRAFFIC_SCOUT",
                                              "rankImage": "https://storage.example.com/ranks/traffic_scout.png"
                                            },
                                            {
                                              "userId": "9753c8d4e6f1g2h387k5l9m2",
                                              "username": "emily_parker87",
                                              "contributionCount": 98,
                                              "trustScore": 81.2,
                                              "rank": "ROAD_EXPLORER",
                                              "rankImage": "https://storage.example.com/ranks/road_explorer.png"
                                            },
                                            {
                                              "userId": "1864d9e5f2g3h4j589l7m3n6",
                                              "username": "michael_business",
                                              "contributionCount": 76,
                                              "trustScore": 77.8,
                                              "rank": "NAVIGATOR_NOVICE",
                                              "rankImage": "https://storage.example.com/ranks/navigator_novice.png"
                                            }
                                          ],
                                          "userGrowthStats": [
                                            {
                                              "month": "2024-01",
                                              "newUsers": 287,
                                              "totalUsers": 5732
                                            },
                                            {
                                              "month": "2024-02",
                                              "newUsers": 342,
                                              "totalUsers": 6074
                                            },
                                            {
                                              "month": "2024-03",
                                              "newUsers": 415,
                                              "totalUsers": 6489
                                            },
                                            {
                                              "month": "2024-04",
                                              "newUsers": 356,
                                              "totalUsers": 6845
                                            }
                                          ],
                                          "userActivityMetrics": {
                                            "dailyActiveUsers": 4283,
                                            "monthlyActiveUsers": 5942,
                                            "retentionRate": 86.8
                                          },
                                          "error": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized access",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "appRatingByNumberOfRate": null,
                                          "topContributors": null,
                                          "userGrowthStats": null,
                                          "userActivityMetrics": null,
                                          "error": "Authentication is required to access this resource."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Insufficient permissions",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "appRatingByNumberOfRate": null,
                                          "topContributors": null,
                                          "userGrowthStats": null,
                                          "userActivityMetrics": null,
                                          "error": "You do not have sufficient permissions to access dashboard data."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Dashboard data not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "appRatingByNumberOfRate": null,
                                          "topContributors": null,
                                          "userGrowthStats": null,
                                          "userActivityMetrics": null,
                                          "error": "Dashboard data not found. The user administration statistics have not been generated yet."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "appRatingByNumberOfRate": null,
                                          "topContributors": null,
                                          "userGrowthStats": null,
                                          "userActivityMetrics": null,
                                          "error": "An internal server error occurred. Please try again later."
                                        }
                                        """
                            )
                    )
            )
    })
    public @interface GetUserAdminDashboardDataDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Rate the application",
            description = "Allows authenticated users to submit a rating for the application. The rating data is used to gather user feedback and improve the service.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "Application rating information",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RateApplicationRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "5-star rating",
                                    summary = "Excellent rating (5 stars)",
                                    value = """
                                        {
                                          "rate": 5
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "4-star rating",
                                    summary = "Good rating (4 stars)",
                                    value = """
                                        {
                                          "rate": 4
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "3-star rating",
                                    summary = "Average rating (3 stars)",
                                    value = """
                                        {
                                          "rate": 3
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "2-star rating",
                                    summary = "Below average rating (2 stars)",
                                    value = """
                                        {
                                          "rate": 2
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "1-star rating",
                                    summary = "Poor rating (1 star)",
                                    value = """
                                        {
                                          "rate": 1
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rating successfully submitted",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Thank you for rating our application! Your feedback helps us improve our service."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid rating value",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Invalid rating value. Rating must be an integer between 1 and 5."
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
    public @interface RateApplicationDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Update user location",
            description = "Updates the authenticated user's current geographic location. This data is used to provide accurate navigation services, nearby alerts, and location-based features.",
            tags = {"User"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "User location coordinates",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UpdateUserLocationRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Paris location",
                                    summary = "Location in Paris, France",
                                    value = """
                                        {
                                          "location": {
                                            "latitude": 48.8566,
                                            "longitude": 2.3522
                                          }
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "New York location",
                                    summary = "Location in New York City, USA",
                                    value = """
                                        {
                                          "location": {
                                            "latitude": 40.7128,
                                            "longitude": -74.0060
                                          }
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Tokyo location",
                                    summary = "Location in Tokyo, Japan",
                                    value = """
                                        {
                                          "location": {
                                            "latitude": 35.6762,
                                            "longitude": 139.6503
                                          }
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Location successfully updated",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Your location has been successfully updated. This will help provide more accurate navigation and alerts."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid location coordinates",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Invalid location coordinates. Latitude must be between -90 and 90, and longitude must be between -180 and 180."
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
    public @interface UpdateUserLocationDoc {}

}
