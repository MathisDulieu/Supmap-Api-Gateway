package com.novus.api_gateway.service;

import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Map.*;
import com.novus.shared_models.response.Map.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MapService {
    public ResponseEntity<GetMapAdminDashboardDataResponse> getMapAdminDashboardData(User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> saveNewAlert(SaveNewAlertRequest request, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<GetAllAlertsByPositionResponse> getAllAlertsByPosition(GetAllAlertsByPositionRequest request) {
        return null;
    }

    public ResponseEntity<GetAllAlertsByRouteResponse> getAllAlertsByRoute(GetAllAlertsByRouteRequest request) {
        return null;
    }

    public ResponseEntity<GetUserFavoriteLocationsResponse> getUserFavoriteLocations(User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> saveNewUserFavoriteLocation(SaveNewUserFavoriteLocationRequest request, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> deleteUserFavoriteLocation(String id, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> updateUserFavoriteLocation(String id, UpdateUserFavoriteLocationRequest request, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> validateUserAlert(String id, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> invalidateUserAlert(String id, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> saveUserRoute(SaveUserRouteRequest request, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<GetUserRouteHistoryResponse> getUserRouteHistory(User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> updateUserNavigationPreferences(UpdateUserNavigationPreferencesRequest request, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<GetNearbyUsersResponse> getNearbyUsers(Double latitude, Double longitude, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> shareLocation(ShareLocationRequest request, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> shareRoute(ShareRouteRequest request, User authenticatedUser) {
        return null;
    }

    public ResponseEntity<String> saveNewRouteRecalculation(User authenticatedUser) {
        return null;
    }
}
