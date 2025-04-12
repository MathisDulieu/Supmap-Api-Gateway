package com.novus.api_gateway.utils;

import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.shared_models.common.Location.LocationType;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Map.SaveUserRouteRequest;
import com.novus.shared_models.response.Map.GetNearbyUsersUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class MapUtils {

    private final UserDaoUtils userDaoUtils;

    public void validateSaveUserRouteRequest(List<String> errors, SaveUserRouteRequest request) {
        if (isNull(request.getStartPoint()) || isNull(request.getEndPoint()) ||
                isNull(request.getStartAddress()) || isNull(request.getEndAddress()) ||
                isNull(request.getEstimatedDurationInSeconds()) || isNull(request.getKilometersDistance())) {
            errors.add("All route information is required. Please ensure start point, end point, addresses, duration, and distance are all provided.");
            return;
        }

        if (request.getEstimatedDurationInSeconds() < 1 || request.getKilometersDistance() < 1) {
            errors.add("Invalid route details. Both duration and distance must be positive values.");
        }

        if (isNull(request.getStartPoint().getLatitude()) || isNull(request.getStartPoint().getLongitude()) ||
                isNull(request.getEndPoint().getLatitude()) || isNull(request.getEndPoint().getLongitude())) {
            errors.add("Invalid coordinates. Both start and end points must have valid latitude and longitude values.");
        }
    }

    public String getErrorsAsString(List<String> errors) {
        return String.join(" | ", errors);
    }

    public List<GetNearbyUsersUserResponse> buildGetNearbyUsersUsersResponse(Double latitude, Double longitude) {
        List<User> nearbyUsers = userDaoUtils.findNearByUsers(latitude, longitude);
        List<GetNearbyUsersUserResponse> response = new ArrayList<>();

        for (User user : nearbyUsers) {
            response.add(GetNearbyUsersUserResponse.builder()
                    .username(user.getUsername())
                    .profileImage(user.getProfileImage())
                    .lastKnownLocation(user.getLastKnownLocation())
                    .rankImage(user.getStats().getRankImage())
                    .userRank(String.valueOf(user.getStats().getRank()))
                    .build());
        }

        return response;
    }

    public boolean isValidLocationType(String locationType) {
        try {
            LocationType.valueOf(locationType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
