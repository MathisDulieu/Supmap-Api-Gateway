package com.novus.api_gateway.service;

import com.novus.shared_models.request.User.UpdateAuthenticatedUserDetailsRequest;
import com.novus.shared_models.response.User.GetAuthenticatedUserDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static java.util.Collections.emptyMap;

@Service
@RequiredArgsConstructor
public class UserService {
    public ResponseEntity<GetAuthenticatedUserDetailsResponse> getAuthenticatedUserDetails() {
        return null;
    }

    public ResponseEntity<String> deleteAuthenticatedUserAccount() {
        return null;
    }

    public ResponseEntity<String> setUserProfileImage(MultipartFile file) {
        return null;
    }

    public ResponseEntity<String> updateAuthenticatedUserDetails(UpdateAuthenticatedUserDetailsRequest request) {
        return null;
    }

    public ResponseEntity<String> createAdminAccount(CreateAdminAccountRequest request) {
        return null;
    }

    public ResponseEntity<String> deleteAdminAccount(String id) {
        return null;
    }

    public ResponseEntity<GetAllUsersResponse> getAllUsers(String keyword, int page) {
        return null;
    }

}
