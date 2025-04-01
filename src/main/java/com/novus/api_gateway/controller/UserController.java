package com.novus.api_gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/private/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @UserControllerDocs.GetAuthenticatedUserDetailsDoc
    public ResponseEntity<GetAuthenticatedUserDetailsResponse> getAuthenticatedUserDetails(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.getAuthenticatedUserDetails(authenticatedUser, httpRequest);
    }

    @DeleteMapping
    @UserControllerDocs.DeleteAuthenticatedUserAccountDoc
    public ResponseEntity<String> deleteAuthenticatedUserAccount(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.deleteAuthenticatedUserAccount(authenticatedUser, httpRequest);
    }

    @PostMapping(value = "/profile-image", consumes = "multipart/form-data")
    @UserControllerDocs.SetAuthenticatedUserProfileImageDoc
    public ResponseEntity<String> setProfileImage(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) throws JsonProcessingException {
        return userService.setUserProfileImage(file, authenticatedUser, httpRequest);
    }

    @PutMapping
    @UserControllerDocs.UpdateAuthenticatedUserDetailsDoc
    public ResponseEntity<String> updateAuthenticatedUserDetails(
            @RequestBody UpdateAuthenticatedUserDetailsRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.updateAuthenticatedUserDetails(request, authenticatedUser, httpRequest);
    }

    @PostMapping("/protected/create-admin")
    @UserControllerDocs.CreateAdminAccountDoc
    public ResponseEntity<String> createAdminAccount(
            @RequestBody CreateAdminAccountRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.createAdminAccount(request, authenticatedUser, httpRequest);
    }

    @DeleteMapping("/protected/delete-admin/{id}")
    @UserControllerDocs.DeleteAdminAccountDoc
    public ResponseEntity<String> deleteAdminAccount(
            @PathVariable String id,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.deleteAdminAccount(id, authenticatedUser, httpRequest);
    }

    @GetMapping("/private/admin/users")
    @UserControllerDocs.GetAllUsersDoc
    public ResponseEntity<GetAllUsersResponse> getAllUsers(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "filter", defaultValue = "none") String filter,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.getAllUsers(keyword, page, filter, authenticatedUser, httpRequest);
    }

    @GetMapping("/private/admin/user/dashboard-data")
    @UserControllerDocs.GetUserAdminDashboardDataDoc
    public ResponseEntity<GetUserAdminDashboardDataResponse> getUserAdminDashboardData(
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.getUserAdminDashboardData(authenticatedUser, httpRequest);
    }

    @PostMapping("/private/app/rate")
    @UserControllerDocs.RateApplicationDoc
    public ResponseEntity<String> rateApplication(
            @RequestBody RateApplicationRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.rateApplication(request, authenticatedUser, httpRequest);
    }

    @PutMapping("/private/user/location")
    @UserControllerDocs.UpdateUserLocationDoc
    public ResponseEntity<String> updateUserLocation(
            @RequestBody UpdateUserLocationRequest request,
            @AuthenticationPrincipal User authenticatedUser,
            HttpServletRequest httpRequest
    ) {
        return userService.updateUserLocation(request, authenticatedUser, httpRequest);
    }

}
