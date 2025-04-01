package com.novus.api_gateway.controller;

import com.novus.api_gateway.service.MapService;
import com.novus.api_gateway.swagger.MapControllerDoc;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Map.*;
import com.novus.shared_models.response.Map.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MapController {

    private final MapService mapService;

    @GetMapping("/private/admin/map/dashboard-data")
    @MapControllerDoc.GetMapAdminDashboardDataDoc
    public ResponseEntity<GetMapAdminDashboardDataResponse> getMapAdminDashboardData(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.getMapAdminDashboardData(authenticatedUser);
    }

    @PostMapping("/private/map/alert")
    @MapControllerDoc.SaveNewAlertDoc
    public ResponseEntity<String> saveNewAlert(
            @RequestBody SaveNewAlertRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.saveNewAlert(request, authenticatedUser);
    }

    @PostMapping("/map/alerts/position")
    @MapControllerDoc.GetAllAlertsByPositionDoc
    public ResponseEntity<GetAllAlertsByPositionResponse> getAllAlertsByPosition(
            @RequestBody GetAllAlertsByPositionRequest request
    ) {
        return mapService.getAllAlertsByPosition(request);
    }

    @PostMapping("/map/alerts/route")
    @MapControllerDoc.GetAllAlertsByRouteDoc
    public ResponseEntity<GetAllAlertsByRouteResponse> getAllAlertsByRoute(
            @RequestBody GetAllAlertsByRouteRequest request
    ) {
        return mapService.getAllAlertsByRoute(request);
    }

    @GetMapping("/private/map/favorite/locations")
    @MapControllerDoc.GetUserFavoriteLocationsDoc
    public ResponseEntity<GetUserFavoriteLocationsResponse> getUserFavoriteLocations(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.getUserFavoriteLocations(authenticatedUser);
    }

    @PostMapping("/private/map/favorite/location")
    @MapControllerDoc.SaveNewUserFavoriteLocationDoc
    public ResponseEntity<String> saveNewUserFavoriteLocation(
            @RequestBody SaveNewUserFavoriteLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.saveNewUserFavoriteLocation(request, authenticatedUser);
    }

    @DeleteMapping("/private/map/favorite/location/{id}")
    @MapControllerDoc.DeleteUserFavoriteLocationDoc
    public ResponseEntity<String> deleteUserFavoriteLocation(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.deleteUserFavoriteLocation(id, authenticatedUser);
    }

    @PutMapping("/private/map/favorite/location/{id}")
    @MapControllerDoc.UpdateUserFavoriteLocationDoc
    public ResponseEntity<String> updateUserFavoriteLocation(
            @PathVariable String id,
            @RequestBody UpdateUserFavoriteLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.updateUserFavoriteLocation(id, request, authenticatedUser);
    }

    @PutMapping("/private/map/alert/validate/{id}")
    @MapControllerDoc.ValidateUserAlertDoc
    public ResponseEntity<String> validateUserAlert(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.validateUserAlert(id, authenticatedUser);
    }

    @PutMapping("/private/map/alert/invalidate/{id}")
    @MapControllerDoc.InvalidateUserAlertDoc
    public ResponseEntity<String> invalidateUserAlert(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.invalidateUserAlert(id, authenticatedUser);
    }

    @PostMapping("/private/map/save-route")
    @MapControllerDoc.SaveUserRouteDoc
    public ResponseEntity<String> saveUserRoute(
            @RequestBody SaveUserRouteRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.saveUserRoute(request, authenticatedUser);
    }

    @GetMapping("/private/map/history/routes")
    @MapControllerDoc.GetUserRouteHistoryDoc
    public ResponseEntity<GetUserRouteHistoryResponse> getUserRouteHistory(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.getUserRouteHistory(authenticatedUser);
    }

    @PutMapping("/private/map/navigation-preferences")
    @MapControllerDoc.UpdateUserNavigationPreferencesDoc
    public ResponseEntity<String> updateUserNavigationPreferences(
            @RequestBody UpdateUserNavigationPreferencesRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.updateUserNavigationPreferences(request, authenticatedUser);
    }

    @GetMapping("/private/map/nearby-users")
    @MapControllerDoc.GetNearbyUsersDoc
    public ResponseEntity<GetNearbyUsersResponse> getNearbyUsers(
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.getNearbyUsers(latitude, longitude, authenticatedUser);
    }

    @PostMapping("/private/map/location/share")
    @MapControllerDoc.ShareLocationDoc
    public ResponseEntity<String> shareLocation(
            @RequestBody ShareLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.shareLocation(request, authenticatedUser);
    }

    @PostMapping("/private/map/route/share")
    @MapControllerDoc.ShareRouteDoc
    public ResponseEntity<String> shareRoute(
            @RequestBody ShareRouteRequest request,
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.shareRoute(request, authenticatedUser);
    }

    @PostMapping("/private/map/route-recalculation")
    @MapControllerDoc.SaveNewRouteRecalculationDoc
    public ResponseEntity<String> saveNewRouteRecalculation(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return mapService.saveNewRouteRecalculation(authenticatedUser);
    }

}
