package com.novus.api_gateway.service;

import com.novus.api_gateway.Producer;
import com.novus.api_gateway.configuration.EnvConfiguration;
import com.novus.api_gateway.dao.AdminDashboardDaoUtils;
import com.novus.api_gateway.dao.AlertDaoUtils;
import com.novus.api_gateway.dao.LocationDaoUtils;
import com.novus.api_gateway.dao.RouteDaoUtils;
import com.novus.api_gateway.utils.MapUtils;
import com.novus.shared_models.common.AdminDashboard.AdminDashboard;
import com.novus.shared_models.common.Alert.Alert;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.Location.Location;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Map.*;
import com.novus.shared_models.response.Map.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MapService {

    private final AdminDashboardDaoUtils adminDashboardDaoUtils;
    private final AlertDaoUtils alertDaoUtils;
    private final Producer producer;
    private final LocationDaoUtils locationDaoUtils;
    private final MapUtils mapUtils;
    private final RouteDaoUtils routeDaoUtils;
    private final QRcodeService qRcodeService;
    private final EnvConfiguration envConfiguration;

    public ResponseEntity<GetMapAdminDashboardDataResponse> getMapAdminDashboardData(User authenticatedUser, HttpServletRequest httpRequest) {
        GetMapAdminDashboardDataResponse response = GetMapAdminDashboardDataResponse.builder().build();

        Optional<AdminDashboard> optionalAdminDashboard = adminDashboardDaoUtils.find();
        if (optionalAdminDashboard.isEmpty()) {
            response.setError("Dashboard data not found. The administrative statistics have not been generated yet.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        response.setIncidentsByType(optionalAdminDashboard.get().getIncidentsByType());
        response.setIncidentConfirmationRate(optionalAdminDashboard.get().getIncidentConfirmationRate());
        response.setRouteRecalculations(optionalAdminDashboard.get().getRouteRecalculations());
        response.setTotalRoutesProposed(optionalAdminDashboard.get().getTotalRoutesProposed());

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "map-service", "getMapAdminDashboardData");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> saveNewAlert(SaveNewAlertRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "alertType", request.getAlertType(),
                "latitude", String.valueOf(request.getLatitude()),
                "longitude", String.valueOf(request.getLongitude())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "saveNewAlert");

        return ResponseEntity.status(HttpStatus.OK).body("Alert successfully created! Other users in the area will now be notified about this incident.");
    }

    public ResponseEntity<GetAllAlertsByPositionResponse> getAllAlertsByPosition(GetAllAlertsByPositionRequest request, HttpServletRequest httpRequest) {
        GetAllAlertsByPositionResponse response = GetAllAlertsByPositionResponse.builder()
                .alerts(alertDaoUtils.findAlertsByPosition(request.getLatitude(), request.getLongitude()))
                .build();

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, null);

        producer.send(kafkaMessage, "map-service", "getAllAlertsByPosition");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<GetAllAlertsByRouteResponse> getAllAlertsByRoute(GetAllAlertsByRouteRequest request, HttpServletRequest httpRequest) {
        GetAllAlertsByRouteResponse response = GetAllAlertsByRouteResponse.builder()
                .alerts(alertDaoUtils.findAlertsByRoute(request.getRoutePoints()))
                .build();

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, null);

        producer.send(kafkaMessage, "map-service", "getAllAlertsByRoute");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<GetUserFavoriteLocationsResponse> getUserFavoriteLocations(User authenticatedUser, HttpServletRequest httpRequest) {
        GetUserFavoriteLocationsResponse response = GetUserFavoriteLocationsResponse.builder()
                .locations(locationDaoUtils.findByIds(authenticatedUser.getFavoriteLocationIds()))
                .build();

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "map-service", "getUserFavoriteLocations");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> saveNewUserFavoriteLocation(SaveNewUserFavoriteLocationRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "locationType", request.getLocationType(),
                "city", request.getCity(),
                "country", request.getCountry(),
                "name", request.getName(),
                "street", request.getStreet(),
                "formattedAddress", request.getFormattedAddress(),
                "postalCode", request.getPostalCode(),
                "coordinates", String.valueOf(request.getCoordinates())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "saveNewUserFavoriteLocation");

        return ResponseEntity.status(HttpStatus.OK).body("Location successfully saved to your favorites! You can now easily access it for future navigation.");
    }

    public ResponseEntity<String> deleteUserFavoriteLocation(String id, User authenticatedUser, HttpServletRequest httpRequest) {
        Optional<Location> optionalLocation = locationDaoUtils.findById(id);
        if (optionalLocation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite location not found. It may have been already deleted or the ID is invalid.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "locationId", id
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "deleteUserFavoriteLocation");

        return ResponseEntity.status(HttpStatus.OK).body("Favorite location has been successfully deleted.");
    }

    public ResponseEntity<String> updateUserFavoriteLocation(String id, UpdateUserFavoriteLocationRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        Optional<Location> optionalLocation = locationDaoUtils.findById(id);
        if (optionalLocation.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Favorite location not found. It may have been deleted or the ID is invalid.");
        }

        if (!Objects.equals(optionalLocation.get().getUserId(), authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update this favorite" +
                    " location. You can only modify locations that you have saved.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "locationId", id,
                "locationType", request.getLocationType(),
                "city", request.getCity(),
                "country", request.getCountry(),
                "name", request.getName(),
                "street", request.getStreet(),
                "formattedAddress", request.getFormattedAddress(),
                "postalCode", request.getPostalCode(),
                "coordinates", String.valueOf(request.getCoordinates())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "updateUserFavoriteLocation");

        return ResponseEntity.status(HttpStatus.OK).body("Favorite location has been successfully updated with the new information.");
    }

    public ResponseEntity<String> validateUserAlert(String id, User authenticatedUser, HttpServletRequest httpRequest) {
        Optional<Alert> optionalAlert = alertDaoUtils.findById(id);
        if (optionalAlert.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alert not found. It may have been removed or the ID is invalid.");
        }

        if (Objects.equals(optionalAlert.get().getReportedByUserId(), authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot validate your own alert. " +
                    "Validation requires confirmation from other users.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "alertId", id
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "validateUserAlert");

        return ResponseEntity.status(HttpStatus.OK).body("Thank you for validating this alert. Your confirmation helps improve accuracy for all users.");
    }

    public ResponseEntity<String> invalidateUserAlert(String id, User authenticatedUser, HttpServletRequest httpRequest) {
        Optional<Alert> optionalAlert = alertDaoUtils.findById(id);
        if (optionalAlert.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alert not found. It may have been removed or the ID is invalid.");
        }

        if (Objects.equals(optionalAlert.get().getReportedByUserId(), authenticatedUser.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You cannot invalidate your own alert. Feedback on alert accuracy must come from other users.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "alertId", id
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "invalidateUserAlert");

        return ResponseEntity.status(HttpStatus.OK).body("Thank you for providing feedback on this alert. Your report helps improve the system's accuracy for all users.");
    }

    public ResponseEntity<String> saveUserRoute(SaveUserRouteRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        List<String> errors = new ArrayList<>();

        mapUtils.validateSaveUserRouteRequest(errors, request);
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapUtils.getErrorsAsString(errors));
        }

        Map<String, String> kafkaRequest = Map.of(
                "endAddress", request.getEndAddress(),
                "startAddress", request.getStartAddress(),
                "endPoint", String.valueOf(request.getEndPoint()),
                "startPoint", String.valueOf(request.getStartPoint()),
                "kilometersDistance", String.valueOf(request.getKilometersDistance()),
                "estimatedDurationInSeconds", String.valueOf(request.getEstimatedDurationInSeconds())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "saveUserRoute");

        return ResponseEntity.status(HttpStatus.OK).body("Route successfully saved to your history. You can access it later from your route history.");
    }

    public ResponseEntity<GetUserRouteHistoryResponse> getUserRouteHistory(User authenticatedUser, HttpServletRequest httpRequest) {
        GetUserRouteHistoryResponse response = GetUserRouteHistoryResponse.builder().build();
        response.setRoutes(routeDaoUtils.findByIds(authenticatedUser.getRecentRouteIds()));

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "map-service", "getUserRouteHistory");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> updateUserNavigationPreferences(UpdateUserNavigationPreferencesRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "preferredTransportMode", request.getPreferredTransportMode(),
                "proximityAlertDistance", String.valueOf(request.getProximityAlertDistance())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "updateUserNavigationPreferences");

        return ResponseEntity.status(HttpStatus.OK).body("Navigation preferences successfully updated. Your routes will now be calculated based on these new settings.");
    }

    public ResponseEntity<GetNearbyUsersResponse> getNearbyUsers(double latitude, double longitude, User authenticatedUser, HttpServletRequest httpRequest) {
        GetNearbyUsersResponse response = GetNearbyUsersResponse.builder()
                .users(mapUtils.buildGetNearbyUsersUsersResponse(latitude, longitude))
                .build();

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "map-service", "getNearbyUsers");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> shareLocation(ShareLocationRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "latitude", String.valueOf(request.getLatitude()),
                "longitude", String.valueOf(request.getLongitude())
        );

        String qrCodeData = envConfiguration.getFrontendBaseUrl() + "location?lat=" + request.getLatitude() + "&lng=" + request.getLongitude();

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "shareLocation");

        return ResponseEntity.status(HttpStatus.OK).body(qRcodeService.buildQRCodeUrl(qrCodeData, 300));
    }

    public ResponseEntity<String> shareRoute(ShareRouteRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "startLatitude", String.valueOf(request.getStartLatitude()),
                "startLongitude", String.valueOf(request.getStartLongitude()),
                "endLatitude", String.valueOf(request.getEndLatitude()),
                "endLongitude", String.valueOf(request.getEndLongitude())
        );

        String qrCodeData = envConfiguration.getFrontendBaseUrl() + "route?" +
                "startLat=" + request.getStartLatitude() +
                "&startLng=" + request.getStartLongitude() +
                "&endLat=" + request.getEndLatitude() +
                "&endLng=" + request.getEndLongitude();

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "map-service", "shareRoute");

        return ResponseEntity.status(HttpStatus.OK).body(qRcodeService.buildQRCodeUrl(qrCodeData, 300));
    }

    public ResponseEntity<String> saveNewRouteRecalculation(User authenticatedUser, HttpServletRequest httpRequest) {
        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "map-service", "saveNewRouteRecalculation");

        return ResponseEntity.status(HttpStatus.OK).body("Route recalculation successfully logged. Your navigation data helps improve routing algorithms for all users.");
    }
}
