package com.novus.api_gateway.controller;

import com.novus.api_gateway.service.UserService;
import com.novus.api_gateway.swagger.UserControllerDocs;
import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import com.novus.shared_models.response.User.GetAuthenticatedUserDetailsResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/private/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @UserControllerDocs.GetAuthenticatedUserDetailsDoc
    public ResponseEntity<GetAuthenticatedUserDetailsResponse> getAuthenticatedUserDetails() {
        return userService.getAuthenticatedUserDetails();
    }

    @DeleteMapping
    @UserControllerDocs.DeleteAuthenticatedUserAccountDoc
    public ResponseEntity<String> deleteAuthenticatedUserAccount() {
        return userService.deleteAuthenticatedUserAccount();
    }

    @PostMapping(value = "/profile-image", consumes = "multipart/form-data")
    @UserControllerDocs.SetAuthenticatedUserProfileImageDoc
    public ResponseEntity<String> setProfileImage(
            @Parameter(
                    description = "The media file to upload. Supported formats: JPEG, PNG, etc.",
                    required = true
            )
            @RequestParam("file") MultipartFile file) {
        return userService.setUserProfileImage(file);
    }

    @PutMapping
    @UserControllerDocs.UpdateAuthenticatedUserDetailsDoc
    public ResponseEntity<String> updateAuthenticatedUserDetails(
            @RequestBody UpdateAuthenticatedUserDetailsRequest request
    ) {
        return userService.updateAuthenticatedUserDetails(request);
    }

    @PostMapping("/protected/create-admin")
    @UserControllerDocs.CreateAdminAccountDoc
    public ResponseEntity<String> createAdminAccount(
            @RequestBody CreateAdminAccountRequest request
    ) {
        return userService.createAdminAccount(request);
    }

    @DeleteMapping("/protected/delete-admin/{id}")
    @UserControllerDocs.DeleteAdminAccountDoc
    public ResponseEntity<String> deleteAdminAccount(@PathVariable String id) {
        return userService.deleteAdminAccount(id);
    }

    @GetMapping("/private/admin/users")
    @UserControllerDocs.GetAllUsersDoc
    public ResponseEntity<GetAllUsersResponse> getAllUsers(
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page
    ) {
        return userService.getAllUsers(keyword, page);
    }

    @GetMapping("/private/admin/dashboard-data")
    @UserControllerDocs.GetAdminDashboardDataDoc
    public ResponseEntity<GetAdminDashboardDataResponse> getAdminDashboardData() {
        return userService.getAdminDashboardData();
    }

    //Séparer cette méthode en plusieurs méthodes dans les diffèrents services

}
