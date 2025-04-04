package com.novus.api_gateway.swagger;

import com.novus.shared_models.request.Map.GetAllAlertsByPositionRequest;
import com.novus.shared_models.request.Map.GetAllAlertsByRouteRequest;
import com.novus.shared_models.request.Map.SaveNewAlertRequest;
import com.novus.shared_models.request.Map.SaveNewUserFavoriteLocationRequest;
import com.novus.shared_models.response.Map.GetAllAlertsByPositionResponse;
import com.novus.shared_models.response.Map.GetAllAlertsByRouteResponse;
import com.novus.shared_models.response.Map.GetMapAdminDashboardDataResponse;
import com.novus.shared_models.response.Map.GetUserFavoriteLocationsResponse;
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

public class MapControllerDoc {

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get map admin dashboard data",
            description = "Retrieves statistical data for the map administration dashboard, including incident metrics, route calculations, and map usage statistics. This endpoint is only accessible to users with admin privileges.",
            tags = {"Map"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Map dashboard data successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetMapAdminDashboardDataResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "routeRecalculations": [
                                            {
                                              "hour": 0,
                                              "recalculationCount": 152
                                            },
                                            {
                                              "hour": 1,
                                              "recalculationCount": 98
                                            },
                                            {
                                              "hour": 2,
                                              "recalculationCount": 67
                                            },
                                            {
                                              "hour": 3,
                                              "recalculationCount": 43
                                            },
                                            {
                                              "hour": 4,
                                              "recalculationCount": 78
                                            },
                                            {
                                              "hour": 5,
                                              "recalculationCount": 156
                                            }
                                          ],
                                          "incidentConfirmationRate": 82.7,
                                          "incidentsByType": {
                                            "ACCIDENT": 876,
                                            "CONSTRUCTION": 342,
                                            "ROAD_CLOSURE": 193,
                                            "TRAFFIC_JAM": 1254,
                                            "HAZARD": 453,
                                            "POLICE": 187,
                                            "WEATHER": 134
                                          },
                                          "totalRoutesProposed": 45632,
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
                                          "routeRecalculations": null,
                                          "incidentConfirmationRate": null,
                                          "incidentsByType": null,
                                          "totalRoutesProposed": null,
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
                                          "routeRecalculations": null,
                                          "incidentConfirmationRate": null,
                                          "incidentsByType": null,
                                          "totalRoutesProposed": null,
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
                                          "routeRecalculations": null,
                                          "incidentConfirmationRate": null,
                                          "incidentsByType": null,
                                          "totalRoutesProposed": null,
                                          "error": "Dashboard data not found. The administrative statistics have not been generated yet."
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
                                          "routeRecalculations": null,
                                          "incidentConfirmationRate": null,
                                          "incidentsByType": null,
                                          "totalRoutesProposed": null,
                                          "error": "An internal server error occurred. Please try again later."
                                        }
                                        """
                            )
                    )
            )
    })
    public @interface GetMapAdminDashboardDataDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Create a new map alert",
            description = "Allows users to report a new incident or alert on the map. These alerts notify other users in the area about traffic conditions, hazards, or other relevant information.",
            tags = {"Map"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "Alert information including type and location",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaveNewAlertRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Traffic accident",
                                    summary = "Report a traffic accident",
                                    value = """
                                        {
                                          "alertType": "ACCIDENT",
                                          "latitude": 48.8566,
                                          "longitude": 2.3522
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Road construction",
                                    summary = "Report road construction work",
                                    value = """
                                        {
                                          "alertType": "CONSTRUCTION",
                                          "latitude": 40.7128,
                                          "longitude": -74.0060
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Road closure",
                                    summary = "Report a closed road",
                                    value = """
                                        {
                                          "alertType": "ROAD_CLOSURE",
                                          "latitude": 51.5074,
                                          "longitude": -0.1278
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Traffic jam",
                                    summary = "Report heavy traffic",
                                    value = """
                                        {
                                          "alertType": "TRAFFIC_JAM",
                                          "latitude": 35.6762,
                                          "longitude": 139.6503
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Road hazard",
                                    summary = "Report a hazard on the road",
                                    value = """
                                        {
                                          "alertType": "HAZARD",
                                          "latitude": 52.5200,
                                          "longitude": 13.4050
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Police checkpoint",
                                    summary = "Report police presence",
                                    value = """
                                        {
                                          "alertType": "POLICE",
                                          "latitude": 41.9028,
                                          "longitude": 12.4964
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Weather condition",
                                    summary = "Report severe weather affecting traffic",
                                    value = """
                                        {
                                          "alertType": "WEATHER",
                                          "latitude": 48.2082,
                                          "longitude": 16.3738
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Alert successfully created",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Alert successfully created! Other users in the area will now be notified about this incident."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Invalid alert type",
                                            summary = "The alert type is not recognized",
                                            value = "Invalid alert type. Please use one of the supported alert types: ACCIDENT, CONSTRUCTION, ROAD_CLOSURE, TRAFFIC_JAM, HAZARD, POLICE, WEATHER."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid coordinates",
                                            summary = "The coordinates are out of valid range",
                                            value = "Invalid location coordinates. Latitude must be between -90 and 90, and longitude must be between -180 and 180."
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
                    responseCode = "429",
                    description = "Too many reports",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "You have submitted too many alerts in a short period. Please wait before reporting another incident."
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
    public @interface SaveNewAlertDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get alerts by geographic position",
            description = "Retrieves all active alerts in the vicinity of the specified geographic coordinates. This allows users to see reported incidents, traffic conditions, and other relevant information in their area.",
            tags = {"Map"}
    )
    @RequestBody(
            description = "Geographic coordinates to search for nearby alerts",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetAllAlertsByPositionRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Paris location",
                                    summary = "Search for alerts near Paris, France",
                                    value = """
                                        {
                                          "latitude": 48.8566,
                                          "longitude": 2.3522
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "New York location",
                                    summary = "Search for alerts near New York City, USA",
                                    value = """
                                        {
                                          "latitude": 40.7128,
                                          "longitude": -74.0060
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Tokyo location",
                                    summary = "Search for alerts near Tokyo, Japan",
                                    value = """
                                        {
                                          "latitude": 35.6762,
                                          "longitude": 139.6503
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved alerts by position",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetAllAlertsByPositionResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "alerts": [
                                            {
                                              "id": "6428a7b9c3d5e8f241g6h1i7",
                                              "type": "TRAFFIC_JAM",
                                              "location": {
                                                "latitude": 48.8580,
                                                "longitude": 2.3526
                                              },
                                              "roadName": "Avenue des Champs-Élysées",
                                              "description": "Heavy traffic due to demonstration",
                                              "createdAt": "2024-04-03T14:35:22.789Z",
                                              "updatedAt": "2024-04-03T14:35:22.789Z",
                                              "expiresAt": "2024-04-03T17:35:22.789Z",
                                              "reportedByUserId": "7539a8b2c1d4e6f340g5h9i3"
                                            },
                                            {
                                              "id": "7539a8b2c1d4e6f340g5h9i3",
                                              "type": "CONSTRUCTION",
                                              "location": {
                                                "latitude": 48.8570,
                                                "longitude": 2.3510
                                              },
                                              "roadName": "Rue de Rivoli",
                                              "description": "Lane closed for construction work",
                                              "createdAt": "2024-04-03T10:15:45.123Z",
                                              "updatedAt": "2024-04-03T10:15:45.123Z",
                                              "expiresAt": "2024-04-05T18:00:00.000Z",
                                              "reportedByUserId": "8642b9c5d7e2f3a481j6k2l4"
                                            },
                                            {
                                              "id": "8642b9c5d7e2f3a481j6k2l4",
                                              "type": "ACCIDENT",
                                              "location": {
                                                "latitude": 48.8590,
                                                "longitude": 2.3540
                                              },
                                              "roadName": "Boulevard Haussmann",
                                              "description": "Multi-vehicle collision, emergency services on scene",
                                              "createdAt": "2024-04-03T15:20:33.456Z",
                                              "updatedAt": "2024-04-03T15:20:33.456Z",
                                              "expiresAt": "2024-04-03T18:20:33.456Z",
                                              "reportedByUserId": "9753c8d4e6f1g2h387k5l9m2"
                                            }
                                          ],
                                          "error": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid coordinates",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "alerts": null,
                                          "error": "Invalid location coordinates. Latitude must be between -90 and 90, and longitude must be between -180 and 180."
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
                                          "alerts": null,
                                          "error": "An internal server error occurred. Please try again later."
                                        }
                                        """
                            )
                    )
            )
    })
    public @interface GetAllAlertsByPositionDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get alerts along a route",
            description = "Retrieves all active alerts along a specified route defined by a series of geographic coordinates. This allows users to see reported incidents, traffic conditions, and other relevant information that might affect their journey.",
            tags = {"Map"}
    )
    @RequestBody(
            description = "Series of geographic coordinates defining the route to check for alerts",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = GetAllAlertsByRouteRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Paris city route",
                                    summary = "Route through central Paris",
                                    value = """
                                        {
                                          "routePoints": [
                                            {
                                              "latitude": 48.8584,
                                              "longitude": 2.2945
                                            },
                                            {
                                              "latitude": 48.8617,
                                              "longitude": 2.3126
                                            },
                                            {
                                              "latitude": 48.8634,
                                              "longitude": 2.3371
                                            },
                                            {
                                              "latitude": 48.8580,
                                              "longitude": 2.3522
                                            },
                                            {
                                              "latitude": 48.8566,
                                              "longitude": 2.3522
                                            }
                                          ]
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "New York city route",
                                    summary = "Route through Manhattan",
                                    value = """
                                        {
                                          "routePoints": [
                                            {
                                              "latitude": 40.7411,
                                              "longitude": -73.9897
                                            },
                                            {
                                              "latitude": 40.7546,
                                              "longitude": -73.9841
                                            },
                                            {
                                              "latitude": 40.7614,
                                              "longitude": -73.9776
                                            },
                                            {
                                              "latitude": 40.7700,
                                              "longitude": -73.9745
                                            },
                                            {
                                              "latitude": 40.7831,
                                              "longitude": -73.9712
                                            }
                                          ]
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved alerts along the route",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetAllAlertsByRouteResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "alerts": [
                                            {
                                              "id": "6428a7b9c3d5e8f241g6h1i7",
                                              "type": "TRAFFIC_JAM",
                                              "location": {
                                                "latitude": 48.8617,
                                                "longitude": 2.3126
                                              },
                                              "roadName": "Avenue des Champs-Élysées",
                                              "description": "Heavy traffic due to demonstration",
                                              "createdAt": "2024-04-03T14:35:22.789Z",
                                              "updatedAt": "2024-04-03T14:35:22.789Z",
                                              "expiresAt": "2024-04-03T17:35:22.789Z",
                                              "reportedByUserId": "7539a8b2c1d4e6f340g5h9i3"
                                            },
                                            {
                                              "id": "7539a8b2c1d4e6f340g5h9i3",
                                              "type": "CONSTRUCTION",
                                              "location": {
                                                "latitude": 48.8634,
                                                "longitude": 2.3371
                                              },
                                              "roadName": "Boulevard Haussmann",
                                              "description": "Lane closed for construction work",
                                              "createdAt": "2024-04-03T10:15:45.123Z",
                                              "updatedAt": "2024-04-03T10:15:45.123Z",
                                              "expiresAt": "2024-04-05T18:00:00.000Z",
                                              "reportedByUserId": "8642b9c5d7e2f3a481j6k2l4"
                                            },
                                            {
                                              "id": "8642b9c5d7e2f3a481j6k2l4",
                                              "type": "ROAD_CLOSURE",
                                              "location": {
                                                "latitude": 48.8580,
                                                "longitude": 2.3522
                                              },
                                              "roadName": "Rue de Rivoli",
                                              "description": "Street closed for public event until 8 PM",
                                              "createdAt": "2024-04-03T09:20:33.456Z",
                                              "updatedAt": "2024-04-03T09:20:33.456Z",
                                              "expiresAt": "2024-04-03T20:00:00.000Z",
                                              "reportedByUserId": "9753c8d4e6f1g2h387k5l9m2"
                                            }
                                          ],
                                          "error": null
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid route points",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "alerts": null,
                                          "error": "Invalid route points. Please provide at least two valid geographic coordinates to define a route."
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
                                          "alerts": null,
                                          "error": "An internal server error occurred. Please try again later."
                                        }
                                        """
                            )
                    )
            )
    })
    public @interface GetAllAlertsByRouteDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Get user's favorite locations",
            description = "Retrieves all saved favorite locations for the authenticated user. These locations can be used for quick navigation and route planning.",
            tags = {"Map"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved user's favorite locations",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GetUserFavoriteLocationsResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "locations": [
                                            {
                                              "id": "6428a7b9c3d5e8f241g6h1i7",
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
                                              "locationType": "HOME",
                                              "createdAt": "2024-03-15T10:30:45.123Z",
                                              "updatedAt": "2024-03-15T10:30:45.123Z",
                                              "userId": "7539a8b2c1d4e6f340g5h9i3"
                                            },
                                            {
                                              "id": "7539a8b2c1d4e6f340g5h9i3",
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
                                              "locationType": "WORK",
                                              "createdAt": "2024-03-15T10:32:18.456Z",
                                              "updatedAt": "2024-03-15T10:32:18.456Z",
                                              "userId": "7539a8b2c1d4e6f340g5h9i3"
                                            },
                                            {
                                              "id": "8642b9c5d7e2f3a481j6k2l4",
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
                                              "locationType": "FAVORITE",
                                              "createdAt": "2024-03-17T18:45:22.789Z",
                                              "updatedAt": "2024-03-17T18:45:22.789Z",
                                              "userId": "7539a8b2c1d4e6f340g5h9i3"
                                            },
                                            {
                                              "id": "9753c8d4e6f1g2h387k5l9m2",
                                              "name": "Friend's House",
                                              "formattedAddress": "321 Social St, Paris, 75020, France",
                                              "coordinates": {
                                                "latitude": 48.8656,
                                                "longitude": 2.4036
                                              },
                                              "placeId": "ChIJk4TlY9RlwoCRzc-oq3DTscB",
                                              "street": "321 Social St",
                                              "city": "Paris",
                                              "postalCode": "75020",
                                              "country": "France",
                                              "locationType": "FAVORITE",
                                              "createdAt": "2024-03-20T20:15:10.456Z",
                                              "updatedAt": "2024-03-20T20:15:10.456Z",
                                              "userId": "7539a8b2c1d4e6f340g5h9i3"
                                            },
                                            {
                                              "id": "1864d9e5f2g3h4j589l7m3n6",
                                              "name": "Shopping Mall",
                                              "formattedAddress": "555 Commerce Ave, Paris, 75009, France",
                                              "coordinates": {
                                                "latitude": 48.8731,
                                                "longitude": 2.3421
                                              },
                                              "placeId": "ChIJm4TlW8RlwoBSzc-oq4DTscC",
                                              "street": "555 Commerce Ave",
                                              "city": "Paris",
                                              "postalCode": "75009",
                                              "country": "France",
                                              "locationType": "FAVORITE",
                                              "createdAt": "2024-03-22T14:30:05.789Z",
                                              "updatedAt": "2024-03-22T14:30:05.789Z",
                                              "userId": "7539a8b2c1d4e6f340g5h9i3"
                                            }
                                          ],
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
                                          "locations": null,
                                          "error": "Authentication is required to access this resource."
                                        }
                                        """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No favorite locations found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                        {
                                          "locations": [],
                                          "error": null
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
                                          "locations": null,
                                          "error": "An internal server error occurred. Please try again later."
                                        }
                                        """
                            )
                    )
            )
    })
    public @interface GetUserFavoriteLocationsDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Save a new favorite location",
            description = "Saves a new location to the authenticated user's favorites list. This location can later be used for quick navigation and route planning.",
            tags = {"Map"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @RequestBody(
            description = "Location details to be saved as favorite",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SaveNewUserFavoriteLocationRequest.class),
                    examples = {
                            @ExampleObject(
                                    name = "Home location",
                                    summary = "Save home address as favorite",
                                    value = """
                                        {
                                          "name": "Home",
                                          "formattedAddress": "123 Residential St, Paris, 75008, France",
                                          "coordinates": {
                                            "latitude": 48.8659,
                                            "longitude": 2.3501
                                          },
                                          "street": "123 Residential St",
                                          "city": "Paris",
                                          "postalCode": "75008",
                                          "country": "France",
                                          "locationType": "HOME"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Work location",
                                    summary = "Save work address as favorite",
                                    value = """
                                        {
                                          "name": "Office",
                                          "formattedAddress": "456 Business Ave, Paris, 75002, France",
                                          "coordinates": {
                                            "latitude": 48.8698,
                                            "longitude": 2.3529
                                          },
                                          "street": "456 Business Ave",
                                          "city": "Paris",
                                          "postalCode": "75002",
                                          "country": "France",
                                          "locationType": "WORK"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Gym location",
                                    summary = "Save gym address as favorite",
                                    value = """
                                        {
                                          "name": "Gym",
                                          "formattedAddress": "789 Fitness Blvd, Paris, 75015, France",
                                          "coordinates": {
                                            "latitude": 48.8417,
                                            "longitude": 2.3238
                                          },
                                          "street": "789 Fitness Blvd",
                                          "city": "Paris",
                                          "postalCode": "75015",
                                          "country": "France",
                                          "locationType": "FAVORITE"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Restaurant location",
                                    summary = "Save favorite restaurant address",
                                    value = """
                                        {
                                          "name": "Le Bistro",
                                          "formattedAddress": "42 Cuisine St, Paris, 75006, France",
                                          "coordinates": {
                                            "latitude": 48.8513,
                                            "longitude": 2.3364
                                          },
                                          "street": "42 Cuisine St",
                                          "city": "Paris",
                                          "postalCode": "75006",
                                          "country": "France",
                                          "locationType": "FAVORITE"
                                        }"""
                            ),
                            @ExampleObject(
                                    name = "Custom location",
                                    summary = "Save custom location",
                                    value = """
                                        {
                                          "name": "Vacation Spot",
                                          "formattedAddress": "123 Beach Ave, Nice, 06000, France",
                                          "coordinates": {
                                            "latitude": 43.7102,
                                            "longitude": 7.2620
                                          },
                                          "street": "123 Beach Ave",
                                          "city": "Nice",
                                          "postalCode": "06000",
                                          "country": "France",
                                          "locationType": "CUSTOM"
                                        }"""
                            )
                    }
            )
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Location successfully saved to favorites",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Location successfully saved to your favorites! You can now easily access it for future navigation."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid location data",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(
                                            name = "Missing coordinates",
                                            summary = "Location coordinates are missing",
                                            value = "Location coordinates are required. Please provide valid latitude and longitude values."
                                    ),
                                    @ExampleObject(
                                            name = "Invalid location type",
                                            summary = "Location type is not recognized",
                                            value = "Invalid location type. Please use one of the supported types: HOME, WORK, FAVORITE, RECENT, CUSTOM."
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
    public @interface SaveNewUserFavoriteLocationDoc {}

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @Operation(
            summary = "Delete a favorite location",
            description = "Removes a location from the authenticated user's favorites list by its unique identifier.",
            tags = {"Map", "User", "Location"},
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @Parameter(
            name = "id",
            description = "Unique identifier of the favorite location to delete",
            required = true,
            example = "6428a7b9c3d5e8f241g6h1i7"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Location successfully removed from favorites",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Favorite location has been successfully deleted."
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
                    description = "Forbidden - User does not own the location",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "You do not have permission to delete this location. You can only delete your own favorite locations."
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Location not found",
                    content = @Content(
                            mediaType = "text/plain",
                            examples = @ExampleObject(
                                    value = "Favorite location not found. It may have been already deleted or the ID is invalid."
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
    public @interface DeleteUserFavoriteLocationDoc {}
    public @interface UpdateUserFavoriteLocationDoc {}
    public @interface ValidateUserAlertDoc {}
    public @interface InvalidateUserAlertDoc {}
    public @interface SaveUserRouteDoc {}
    public @interface GetUserRouteHistoryDoc {}
    public @interface UpdateUserNavigationPreferencesDoc {}
    public @interface GetNearbyUsersDoc {}
    public @interface ShareLocationDoc {}
    public @interface ShareRouteDoc {}
    public @interface SaveNewRouteRecalculationDoc {}

}
