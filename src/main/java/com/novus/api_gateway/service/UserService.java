package com.novus.api_gateway.service;

import com.novus.api_gateway.Producer;
import com.novus.api_gateway.dao.AdminDashboardDaoUtils;
import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.api_gateway.utils.ImageUtils;
import com.novus.api_gateway.utils.UserUtils;
import com.novus.shared_models.common.AdminDashboard.AdminDashboard;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.common.User.UserRole;
import com.novus.shared_models.common.User.UserStats;
import com.novus.shared_models.request.User.CreateAdminAccountRequest;
import com.novus.shared_models.request.User.RateApplicationRequest;
import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import com.novus.shared_models.request.User.UpdateUserLocationRequest;
import com.novus.shared_models.response.User.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class UserService {

    private final Producer producer;
    private final UserUtils userUtils;
    private final UserDaoUtils userDaoUtils;
    private final AdminDashboardDaoUtils adminDashboardDaoUtils;
    private final ImageUtils imageUtils;

    @Scheduled(fixedRate = 120000)
    public void RefreshUserActivity() {
        log.info("🔄 Refreshing user activity status");
        userDaoUtils.updateUserActivityStatus();
    }

    public ResponseEntity<GetAuthenticatedUserDetailsResponse> getAuthenticatedUserDetails(User authenticatedUser, HttpServletRequest httpRequest) {
        GetAuthenticatedUserDetailsResponse response = userUtils.buildGetAuthenticatedUserDetailsResponse(authenticatedUser);

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-service", "getAuthenticatedUserDetails");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> deleteAuthenticatedUserAccount(User authenticatedUser, HttpServletRequest httpRequest) {
        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-service", "deleteAuthenticatedUserAccount");

        return ResponseEntity.status(HttpStatus.OK).body("Your account has been successfully deleted. All personal " +
                "data has been removed from our system. We're sorry to see you go!");
    }

    public ResponseEntity<String> setUserProfileImage(MultipartFile file, User authenticatedUser, HttpServletRequest httpRequest) {
        if (isNull(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image file provided. Please select an image to upload.");
        }

        String imageUrl;

        try {
            imageUrl = imageUtils.uploadImage(file);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the image. Please " +
                    "try again with a different image or contact support if the issue persists.");
        }

        if (isEmpty(imageUrl)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to generate image URL. The image could" +
                    " not be processed properly. Please try again with a different image or contact support.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "imageUrl", imageUrl
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-service", "setUserProfileImage");

        return ResponseEntity.status(HttpStatus.OK).body("Profile image successfully updated! Your new profile picture is now visible to other users.");
    }

    public ResponseEntity<String> updateAuthenticatedUserDetails(UpdateAuthenticatedUserDetailsRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        List<String> errors = new ArrayList<>();
        boolean hasUpdatedEmail = !isNull(request.getEmail());

        userUtils.validateUpdateAuthenticatedUserDetailsRequest(errors, request);
        if (errors.isEmpty()) {
            userUtils.validateNewUsername(errors, request.getUsername(), authenticatedUser);
            userUtils.validateNewEmail(errors, request.getEmail(), authenticatedUser);
            userUtils.validateNewPassword(errors, request.getOldPassword(), request.getNewPassword(), authenticatedUser);
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userUtils.getErrorsAsString(errors));
        }

        Map<String, String> kafkaRequest = Map.of(
                "hasUpdatedEmail", String.valueOf(hasUpdatedEmail)
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-service", "setUserProfileImage");

        return ResponseEntity.status(HttpStatus.OK).body("Your profile information has been successfully updated. The changes are now visible in your account.");
    }

    public ResponseEntity<String> createAdminAccount(CreateAdminAccountRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        List<String> errors = new ArrayList<>();

        userUtils.validateUsername(errors, request.getUsername());
        userUtils.validateEmail(errors, request.getEmail());
        userUtils.validatePassword(errors, request.getPassword());

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userUtils.getErrorsAsString(errors));
        }

        Map<String, String> kafkaRequest = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "password", request.getPassword(),
                "isValidEmail", String.valueOf(request.isValidEmail()),
                "profileImage", request.getProfileImage()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-service", "createAdminAccount");

        return ResponseEntity.status(HttpStatus.OK).body("Admin account successfully created. The new administrator can now log in with the provided credentials.");
    }

    public ResponseEntity<String> deleteAdminAccount(String id, User authenticatedUser, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userDaoUtils.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. The specified admin account does not exist or has already been deleted.");
        }

        if (!optionalUser.get().getRole().equals(UserRole.ADMIN)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This operation can only be performed on admin accounts. The specified user is not an administrator.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-service", "deleteAdminAccount");

        return ResponseEntity.status(HttpStatus.OK).body("Admin account successfully deleted. The user will no longer have administrator privileges.");
    }

    public ResponseEntity<GetAllUsersResponse> getAllUsers(String keyword, int page, User authenticatedUser, HttpServletRequest httpRequest) {
        GetAllUsersResponse response = GetAllUsersResponse.builder().build();

        String error = userUtils.validateGetAllUsersRequest(keyword, page);
        if (!isNull(error)) {
            response.setError(error);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        List<User> users = userDaoUtils.searchUsersByUsernamePrefix(keyword, page);
        int totalPages = userUtils.getTotalPages(users.size(), 10);

        response.setUsers(users);
        response.setTotalPages(totalPages);

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-service", "getAllUsers");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<GetUserAdminDashboardDataResponse> getUserAdminDashboardData(User authenticatedUser, HttpServletRequest httpRequest) {
        GetUserAdminDashboardDataResponse response = GetUserAdminDashboardDataResponse.builder().build();

        Optional<AdminDashboard> optionalAdminDashboard = adminDashboardDaoUtils.find();
        if (optionalAdminDashboard.isEmpty()) {
            response.setError("Dashboard data not found. The user administration statistics have not been generated yet.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        int dailyActiveUsers = userDaoUtils.countDailyActiveUsers();
        int monthlyActiveUsers = userDaoUtils.countMonthlyActiveUsers();
        double retentionRate = userDaoUtils.calculateRetentionRate();
        UserActivityMetricsResponse userActivityMetricsResponse = buildUserActivityMetricsResponse(dailyActiveUsers, monthlyActiveUsers, retentionRate);
        response.setUserActivityMetrics(userActivityMetricsResponse);

        response.setUserGrowthStats(optionalAdminDashboard.get().getUserGrowthStats());

        List<User> topContributorsUsers = adminDashboardDaoUtils.findTopContributors();
        List<UserContributionResponse> userContributionResponses = getUserContributionResponse(topContributorsUsers);
        response.setTopContributors(userContributionResponses);

        response.setAppRatingByNumberOfRate(optionalAdminDashboard.get().getAppRatingByNumberOfRate());

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-service", "getUserAdminDashboardData");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    private UserActivityMetricsResponse buildUserActivityMetricsResponse(int dailyActiveUsers, int monthlyActiveUsers, double retentionRate) {
        return UserActivityMetricsResponse.builder()
                .dailyActiveUsers(dailyActiveUsers)
                .monthlyActiveUsers(monthlyActiveUsers)
                .retentionRate(retentionRate)
                .build();
    }

    private List<UserContributionResponse> getUserContributionResponse(List<User> topContributorsUsers) {
        return topContributorsUsers.stream().map(user -> UserContributionResponse.builder()
                .userId(user.getId())
                .contributionCount(getUserContributionCount(user.getStats()))
                .rank(String.valueOf(user.getStats().getRank()))
                .rankImage(user.getStats().getRankImage())
                .trustScore(user.getStats().getTrustScore())
                .username(user.getUsername())
                .build()).toList();
    }

    private Integer getUserContributionCount(UserStats stats) {
        return stats.getValidatedReports() + stats.getInvalidatedReports() + stats.getTotalReportsSubmitted();
    }

    public ResponseEntity<String> rateApplication(RateApplicationRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        if (request.getRate() < 0 || request.getRate() > 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid rating value. Rating must be between 0 and 5.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "rate", String.valueOf(request.getRate())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-service", "rateApplication");

        return ResponseEntity.status(HttpStatus.OK).body("Thank you for rating our application! Your feedback helps us improve our service.");
    }

    public ResponseEntity<String> updateUserLocation(UpdateUserLocationRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "latitude", String.valueOf(request.getLocation().getLatitude()),
                "longitude", String.valueOf(request.getLocation().getLongitude())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-service", "updateUserLocation");

        return ResponseEntity.status(HttpStatus.OK).body("Your location has been successfully updated. This will help provide more accurate navigation and alerts.");
    }

}
