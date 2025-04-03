package com.novus.api_gateway.controller;

import com.novus.api_gateway.prometheus.UserMetrics;
import com.novus.api_gateway.service.UserService;
import com.novus.api_gateway.swagger.UserControllerDocs;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.User.CreateAdminAccountRequest;
import com.novus.shared_models.request.User.RateApplicationRequest;
import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import com.novus.shared_models.request.User.UpdateUserLocationRequest;
import com.novus.shared_models.response.User.GetAllUsersResponse;
import com.novus.shared_models.response.User.GetAuthenticatedUserDetailsResponse;
import com.novus.shared_models.response.User.GetUserAdminDashboardDataResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMetrics metrics;


    @PostMapping("/private/user")
    @UserControllerDocs.GetAuthenticatedUserDetailsDoc
    public ResponseEntity<GetAuthenticatedUserDetailsResponse> getAuthenticatedUserDetails(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return metrics.recordTimedOperation(
                metrics.getGetUserDetailsTimer(),
                "get_user_details",
                () -> userService.getAuthenticatedUserDetails(authenticatedUser, httpRequest)
        );
    }

    @DeleteMapping("/private/user")
    @UserControllerDocs.DeleteAuthenticatedUserAccountDoc
    public ResponseEntity<String> deleteAuthenticatedUserAccount(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getDeleteUserTimer(),
                "delete_user_account",
                () -> userService.deleteAuthenticatedUserAccount(authenticatedUser, httpRequest)
        );
        metrics.recordProfileOperation("account_deletion");
        return response;
    }

    @PostMapping(value = "/private/user/profile-image", consumes = "multipart/form-data")
    @UserControllerDocs.SetAuthenticatedUserProfileImageDoc
    public ResponseEntity<String> setProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getUploadProfileImageTimer(),
                "upload_profile_image",
                () -> userService.setUserProfileImage(file, authenticatedUser, httpRequest)
        );
        metrics.recordProfileOperation("profile_image_update");
        return response;
    }

    @PutMapping("/private/user")
    @UserControllerDocs.UpdateAuthenticatedUserDetailsDoc
    public ResponseEntity<String> updateAuthenticatedUserDetails(
            @RequestBody UpdateAuthenticatedUserDetailsRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getUpdateUserDetailsTimer(),
                "update_user_details",
                () -> userService.updateAuthenticatedUserDetails(request, authenticatedUser, httpRequest)
        );
        metrics.recordProfileOperation("profile_update");
        return response;
    }

    @PostMapping("/protected/user/create-admin")
    @UserControllerDocs.CreateAdminAccountDoc
    public ResponseEntity<String> createAdminAccount(
            @RequestBody CreateAdminAccountRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getCreateAdminTimer(),
                "create_admin",
                () -> userService.createAdminAccount(request, authenticatedUser, httpRequest)
        );
        metrics.recordAdminOperation("admin_creation");
        return response;
    }

    @DeleteMapping("/protected/user/delete-admin/{id}")
    @UserControllerDocs.DeleteAdminAccountDoc
    public ResponseEntity<String> deleteAdminAccount(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getDeleteAdminTimer(),
                "delete_admin",
                () -> userService.deleteAdminAccount(id, authenticatedUser, httpRequest)
        );
        metrics.recordAdminOperation("admin_deletion");
        return response;
    }

    @GetMapping("/private/admin/users")
    @UserControllerDocs.GetAllUsersDoc
    public ResponseEntity<GetAllUsersResponse> getAllUsers(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<GetAllUsersResponse> response = metrics.recordTimedOperation(
                metrics.getGetAllUsersTimer(),
                "get_all_users",
                () -> userService.getAllUsers(keyword, page, authenticatedUser, httpRequest)
        );

        if (!isEmpty(response.getBody())) {
            metrics.recordUserSearch(keyword, response.getBody().getUsers().size());
        }

        metrics.recordAdminOperation("user_list_query");
        return response;
    }

    @GetMapping("/private/admin/user/dashboard-data")
    @UserControllerDocs.GetUserAdminDashboardDataDoc
    public ResponseEntity<GetUserAdminDashboardDataResponse> getUserAdminDashboardData(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<GetUserAdminDashboardDataResponse> response = metrics.recordTimedOperation(
                metrics.getGetDashboardDataTimer(),
                "get_dashboard_data",
                () -> userService.getUserAdminDashboardData(authenticatedUser, httpRequest)
        );
        metrics.recordAdminOperation("dashboard_query");
        return response;
    }

    @PostMapping("/private/user/app/rate")
    @UserControllerDocs.RateApplicationDoc
    public ResponseEntity<String> rateApplication(
            @RequestBody RateApplicationRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getRateApplicationTimer(),
                "rate_application",
                () -> userService.rateApplication(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordApplicationRating(request.getRate());
        }

        return response;
    }

    @PutMapping("/private/user/location")
    @UserControllerDocs.UpdateUserLocationDoc
    public ResponseEntity<String> updateUserLocation(
            @RequestBody UpdateUserLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.recordTimedOperation(
                metrics.getUpdateLocationTimer(),
                "update_location",
                () -> userService.updateUserLocation(request, authenticatedUser, httpRequest)
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordLocationUpdate();
        }

        return response;
    }
}