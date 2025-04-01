package com.novus.api_gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novus.api_gateway.Producer;
import com.novus.api_gateway.dao.LocationDaoUtils;
import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.api_gateway.utils.UserUtils;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.Location.Location;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.common.User.UserStats;
import com.novus.shared_models.request.User.CreateAdminAccountRequest;
import com.novus.shared_models.request.User.RateApplicationRequest;
import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import com.novus.shared_models.request.User.UpdateUserLocationRequest;
import com.novus.shared_models.response.User.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonMap;
import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Producer producer;
    private final UserUtils userUtils;

    public ResponseEntity<GetAuthenticatedUserDetailsResponse> getAuthenticatedUserDetails(User authenticatedUser, HttpServletRequest httpRequest) {
        GetAuthenticatedUserDetailsResponse response = userUtils.buildGetAuthenticatedUserDetailsResponse(authenticatedUser);

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-service", "getAuthenticatedUserDetails");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<String> deleteAuthenticatedUserAccount(User authenticatedUser, HttpServletRequest httpRequest) {
        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-service", "deleteAuthenticatedUserAccount");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> setUserProfileImage(MultipartFile file, User authenticatedUser, HttpServletRequest httpRequest) {
        if (isNull(file)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        String fileAsBase64 = userUtils.getFileAsBase64(file);
        if (isNull(fileAsBase64)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }

        Map<String, String> kafkaRequest = Map.of(
                "file", fileAsBase64
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "user-service", "setUserProfileImage");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> updateAuthenticatedUserDetails(UpdateAuthenticatedUserDetailsRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        List<String> errors = new ArrayList<>();

        userUtils.validateUpdateAuthenticatedUserDetailsRequest(errors, request);
        if (errors.isEmpty()) {
            userUtils.validateNewUsername(errors, request.getUsername(), authenticatedUser);
            userUtils.validateNewEmail(errors, request.getEmail(), authenticatedUser);
            userUtils.validateNewPassword(errors, request.getOldPassword(), request.getNewPassword(), authenticatedUser);
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userUtils.getErrorsAsString(errors));
        }

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, null);

        producer.send(kafkaMessage, "user-service", "setUserProfileImage");

        return ResponseEntity.status(HttpStatus.OK).body("message");
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

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> deleteAdminAccount(String id, User authenticatedUser, HttpServletRequest httpRequest) {
        return null;
    }

    public ResponseEntity<GetAllUsersResponse> getAllUsers(String keyword, int page, String filter, User authenticatedUser, HttpServletRequest httpRequest) {
        return null;
    }

    public ResponseEntity<GetUserAdminDashboardDataResponse> getUserAdminDashboardData(User authenticatedUser, HttpServletRequest httpRequest) {
        return null;
    }

    public ResponseEntity<String> rateApplication(RateApplicationRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        return null;
    }

    public ResponseEntity<String> updateUserLocation(UpdateUserLocationRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        return null;
    }

}
