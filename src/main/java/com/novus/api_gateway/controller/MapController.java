package com.novus.api_gateway.controller;

import com.novus.api_gateway.prometheus.MapMetrics;
import com.novus.api_gateway.service.MapService;
import com.novus.api_gateway.swagger.MapControllerDoc;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Map.*;
import com.novus.shared_models.response.Map.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;
    private final MapMetrics metrics;

    @GetMapping("/private/admin/map/dashboard-data")
    @MapControllerDoc.GetMapAdminDashboardDataDoc
    public ResponseEntity<GetMapAdminDashboardDataResponse> getMapAdminDashboardData(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return metrics.recordTimedOperation(
                metrics.getGetMapAdminDashboardDataTimer(),
                "get_map_dashboard_data",
                () -> mapService.getMapAdminDashboardData(authenticatedUser, httpRequest)
        );
    }

    @PostMapping("/private/map/alert")
    @MapControllerDoc.SaveNewAlertDoc
    public ResponseEntity<String> saveNewAlert(
            @RequestBody SaveNewAlertRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getSaveNewAlertTimer(),
                "save_new_alert",
                () -> mapService.saveNewAlert(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordAlertOperation("alert_creation");
        }

        return response;
    }

    @PostMapping("/map/alerts/position")
    @MapControllerDoc.GetAllAlertsByPositionDoc
    public ResponseEntity<GetAllAlertsByPositionResponse> getAllAlertsByPosition(
            @RequestBody GetAllAlertsByPositionRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<GetAllAlertsByPositionResponse> response = metrics.recordTimedOperation(
                metrics.getGetAllAlertsByPositionTimer(),
                "get_alerts_by_position",
                () -> mapService.getAllAlertsByPosition(request, httpRequest)
        );

        if (!isEmpty(response.getBody())) {
            metrics.recordAlertsByPositionSearch(
                    request.getLatitude(),
                    request.getLongitude(),
                    response.getBody().getAlerts().size()
            );
        }

        return response;
    }

    @PostMapping("/map/alerts/route")
    @MapControllerDoc.GetAllAlertsByRouteDoc
    public ResponseEntity<GetAllAlertsByRouteResponse> getAllAlertsByRoute(
            @RequestBody GetAllAlertsByRouteRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<GetAllAlertsByRouteResponse> response = metrics.recordTimedOperation(
                metrics.getGetAllAlertsByRouteTimer(),
                "get_alerts_by_route",
                () -> mapService.getAllAlertsByRoute(request, httpRequest)
        );

        if (!isEmpty(response.getBody())) {
            metrics.recordAlertsByRouteSearch(response.getBody().getAlerts().size());
        }

        return response;
    }

    @GetMapping("/private/map/favorite/locations")
    @MapControllerDoc.GetUserFavoriteLocationsDoc
    public ResponseEntity<GetUserFavoriteLocationsResponse> getUserFavoriteLocations(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return metrics.recordTimedOperation(
                metrics.getGetUserFavoriteLocationsTimer(),
                "get_favorite_locations",
                () -> mapService.getUserFavoriteLocations(authenticatedUser, httpRequest)
        );
    }

    @PostMapping("/private/map/favorite/location")
    @MapControllerDoc.SaveNewUserFavoriteLocationDoc
    public ResponseEntity<String> saveNewUserFavoriteLocation(
            @RequestBody SaveNewUserFavoriteLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getSaveNewUserFavoriteLocationTimer(),
                "save_favorite_location",
                () -> mapService.saveNewUserFavoriteLocation(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordFavoriteLocationOperation("favorite_location_creation");
        }

        return response;
    }

    @DeleteMapping("/private/map/favorite/location/{id}")
    @MapControllerDoc.DeleteUserFavoriteLocationDoc
    public ResponseEntity<String> deleteUserFavoriteLocation(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getDeleteUserFavoriteLocationTimer(),
                "delete_favorite_location",
                () -> mapService.deleteUserFavoriteLocation(id, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordFavoriteLocationOperation("favorite_location_deletion");
        }

        return response;
    }

    @PutMapping("/private/map/favorite/location/{id}")
    @MapControllerDoc.UpdateUserFavoriteLocationDoc
    public ResponseEntity<String> updateUserFavoriteLocation(
            @PathVariable String id,
            @RequestBody UpdateUserFavoriteLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getUpdateUserFavoriteLocationTimer(),
                "update_favorite_location",
                () -> mapService.updateUserFavoriteLocation(id, request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordFavoriteLocationOperation("favorite_location_update");
        }

        return response;
    }

    @PutMapping("/private/map/alert/validate/{id}")
    @MapControllerDoc.ValidateUserAlertDoc
    public ResponseEntity<String> validateUserAlert(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getValidateUserAlertTimer(),
                "validate_alert",
                () -> mapService.validateUserAlert(id, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordAlertOperation("alert_validation");
        }

        return response;
    }

    @PutMapping("/private/map/alert/invalidate/{id}")
    @MapControllerDoc.InvalidateUserAlertDoc
    public ResponseEntity<String> invalidateUserAlert(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getInvalidateUserAlertTimer(),
                "invalidate_alert",
                () -> mapService.invalidateUserAlert(id, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordAlertOperation("alert_invalidation");
        }

        return response;
    }

    @PostMapping("/private/map/save-route")
    @MapControllerDoc.SaveUserRouteDoc
    public ResponseEntity<String> saveUserRoute(
            @RequestBody SaveUserRouteRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getSaveUserRouteTimer(),
                "save_route",
                () -> mapService.saveUserRoute(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordRouteOperation("route_save");
        }

        return response;
    }

    @GetMapping("/private/map/history/routes")
    @MapControllerDoc.GetUserRouteHistoryDoc
    public ResponseEntity<GetUserRouteHistoryResponse> getUserRouteHistory(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return metrics.recordTimedOperation(
                metrics.getGetUserRouteHistoryTimer(),
                "get_route_history",
                () -> mapService.getUserRouteHistory(authenticatedUser, httpRequest)
        );
    }

    @PutMapping("/private/map/navigation-preferences")
    @MapControllerDoc.UpdateUserNavigationPreferencesDoc
    public ResponseEntity<String> updateUserNavigationPreferences(
            @RequestBody UpdateUserNavigationPreferencesRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getUpdateUserNavigationPreferencesTimer(),
                "update_navigation_preferences",
                () -> mapService.updateUserNavigationPreferences(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordRouteOperation("navigation_preferences_update");
        }

        return response;
    }

    @GetMapping("/private/map/nearby-users")
    @MapControllerDoc.GetNearbyUsersDoc
    public ResponseEntity<GetNearbyUsersResponse> getNearbyUsers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<GetNearbyUsersResponse> response = metrics.recordTimedOperation(
                metrics.getGetNearbyUsersTimer(),
                "get_nearby_users",
                () -> mapService.getNearbyUsers(latitude, longitude, authenticatedUser, httpRequest)
        );

        if (!isEmpty(response.getBody())) {
            metrics.recordNearbyUsersSearch(
                    latitude,
                    longitude,
                    response.getBody().getUsers().size()
            );
        }

        return response;
    }

    @PostMapping("/private/map/location/share")
    @MapControllerDoc.ShareLocationDoc
    public ResponseEntity<String> shareLocation(
            @RequestBody ShareLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getShareLocationTimer(),
                "share_location",
                () -> mapService.shareLocation(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordShareOperation("location_share");
        }

        return response;
    }

    @PostMapping("/private/map/route/share")
    @MapControllerDoc.ShareRouteDoc
    public ResponseEntity<String> shareRoute(
            @RequestBody ShareRouteRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getShareRouteTimer(),
                "share_route",
                () -> mapService.shareRoute(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordShareOperation("route_share");
        }

        return response;
    }

    @PostMapping("/private/map/route-recalculation")
    @MapControllerDoc.SaveNewRouteRecalculationDoc
    public ResponseEntity<String> saveNewRouteRecalculation(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getSaveNewRouteRecalculationTimer(),
                "save_route_recalculation",
                () -> mapService.saveNewRouteRecalculation(authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordRouteRecalculation();
        }

        return response;
    }
}