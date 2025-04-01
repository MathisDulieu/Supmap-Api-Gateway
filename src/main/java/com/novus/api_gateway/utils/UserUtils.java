package com.novus.api_gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novus.api_gateway.dao.LocationDaoUtils;
import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.common.User.UserStats;
import com.novus.shared_models.request.Authentication.RegisterRequest;
import com.novus.shared_models.request.User.CreateAdminAccountRequest;
import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import com.novus.shared_models.response.User.GetAuthenticatedUserDetailsResponse;
import com.novus.shared_models.response.User.UserDetailsLocationResponse;
import com.novus.shared_models.response.User.UserStatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;


@Component
@RequiredArgsConstructor
public class UserUtils {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}';:\",.<>?|`~])[A-Za-z\\d!@#$%^&*()_+\\-={}';:\",.<>?|`~]{8,}$";

    private final UserDaoUtils userDaoUtils;
    private final LocationDaoUtils locationDaoUtils;
    private final PasswordEncoder passwordEncoder;

    public String getRegisterValidationError(RegisterRequest request) {
        if (isInvalidEmail(request.getEmail())) {
            return "error";
        }
        if (isInvalidUsername(request.getUsername())) {
            return "error";
        }
        if (isInvalidPassword(request.getPassword())) {
            return "error";
        }
        if (userDaoUtils.isUsernameAlreadyUsed(request.getUsername())) {
            return "error";
        }
        if (userDaoUtils.isEmailAlreadyUsed(request.getEmail())) {
            return "error";
        }

        return null;
    }

    public boolean isInvalidEmail(String email) {
        return isNull(email) || !Pattern.compile(EMAIL_REGEX).matcher(email).matches();
    }

    public boolean isInvalidUsername(String username) {
        return isNull(username) || username.length() < 3 || username.length() > 11 || username.contains(" ");
    }

    public boolean isInvalidPassword(String password) {
        return isNull(password) || !password.matches(PASSWORD_REGEX);
    }

    public boolean isNotStrongPassword(String password) {
        return !password.matches(PASSWORD_REGEX);
    }

    public String getFileAsBase64(MultipartFile file) {
        try {
            byte[] fileContent = file.getBytes();
            return Base64.getEncoder().encodeToString(fileContent);
        } catch(IOException exception) {
            return null;
        }
    }

    public String getErrorsAsString(List<String> errors) {
        return String.join(" | ", errors);
    }

    public void validateUpdateAuthenticatedUserDetailsRequest(List<String> errors, UpdateAuthenticatedUserDetailsRequest request) {
        if (isNull(request.getEmail()) && isNull(request.getUsername()) && isNull(request.getNewPassword())) {
            errors.add("error");
        }
    }

    public void validateNewUsername(List<String> errors, String username, User userToUpdate) {
        if (!isNull(username)) {
            if (isInvalidUsername(username)) {
                errors.add("error");
            }

            if (userDaoUtils.isUsernameAlreadyUsed(username)) {
                errors.add("error");
            }

            if (username.equals(userToUpdate.getUsername())) {
                errors.add("error");
            }

            userToUpdate.setUsername(username);
        }
    }

    public void validateNewEmail(List<String> errors, String email, User userToUpdate) {
        if (!isNull(email)) {
            if (isInvalidEmail(email)) {
                errors.add("error");
            }

            if (userDaoUtils.isEmailAlreadyUsed(email)) {
                errors.add("error");
            }

            if (email.equals(userToUpdate.getEmail())) {
                errors.add("error");
            }

            userToUpdate.setValidEmail(false);
            userToUpdate.setEmail(email);
        }
    }

    public void validateNewPassword(List<String> errors, String oldPassword, String newPassword, User userToUpdate) {
        if (!isNull(oldPassword) || !isNull(newPassword)) {
            if (isInvalidPassword(newPassword)) {
                errors.add("error");
            }

            if (passwordEncoder.matches(newPassword, userToUpdate.getPassword())) {
                errors.add("New password must be different from the old password.");
            }

            if (!passwordEncoder.matches(oldPassword, userToUpdate.getPassword())) {
                errors.add("Old password is incorrect.");
            }

            userToUpdate.setPassword(passwordEncoder.encode(newPassword));
        }
    }

    public GetAuthenticatedUserDetailsResponse buildGetAuthenticatedUserDetailsResponse(User authenticatedUser) {
        return GetAuthenticatedUserDetailsResponse.builder()
                .username(authenticatedUser.getUsername())
                .email(authenticatedUser.getEmail())
                .isValidEmail(authenticatedUser.isValidEmail())
                .profileImage(authenticatedUser.getProfileImage())
                .favoriteLocations(getLocations(authenticatedUser.getFavoriteLocationIds()))
                .lastKnownLocation(authenticatedUser.getLastKnownLocation())
                .navigationPreferences(authenticatedUser.getNavigationPreferences())
                .notificationSettings(authenticatedUser.getNotificationSettings())
                .stats(getStats(authenticatedUser.getStats()))
                .role(String.valueOf(authenticatedUser.getRole()))
                .build();
    }

    private UserStatsResponse getStats(UserStats stats) {
        return UserStatsResponse.builder()
                .rank(String.valueOf(stats.getRank()))
                .rankImage(stats.getRankImage())
                .reportsValidatedByOthers(stats.getReportsValidatedByOthers())
                .totalDistanceTraveled(stats.getTotalDistanceTraveled())
                .totalReportsSubmitted(stats.getTotalReportsSubmitted())
                .totalRoutesCompleted(stats.getTotalRoutesCompleted())
                .totalTimeSaved(stats.getTotalTimeSaved())
                .validatedReports(stats.getValidatedReports())
                .build();
    }

    private List<UserDetailsLocationResponse> getLocations(List<String> favoriteLocationIds) {
        return favoriteLocationIds.stream()
                .map(locationDaoUtils::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(location -> UserDetailsLocationResponse.builder()
                        .city(location.getCity())
                        .name(location.getName())
                        .coordinates(location.getCoordinates())
                        .country(location.getCountry())
                        .formattedAddress(location.getFormattedAddress())
                        .locationType(location.getLocationType())
                        .placeId(location.getPlaceId())
                        .postalCode(location.getPostalCode())
                        .street(location.getStreet())
                        .build())
                .toList();
    }

    public void validateUsername(List<String> errors, String username) {
        if (isInvalidUsername(username)) {
            errors.add("error");
        }

        if (userDaoUtils.isUsernameAlreadyUsed(username)) {
            errors.add("error");
        }
    }

    public void validateEmail(List<String> errors, String email) {
        if (isInvalidEmail(email)) {
            errors.add("error");
        }

        if (userDaoUtils.isEmailAlreadyUsed(email)) {
            errors.add("error");
        }
    }

    public void validatePassword(List<String> errors, String password) {
        if (isInvalidPassword(password)) {
            errors.add("error");
        }
    }
}
