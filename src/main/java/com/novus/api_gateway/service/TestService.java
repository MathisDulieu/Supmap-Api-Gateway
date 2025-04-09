package com.novus.api_gateway.service;

import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.common.User.UserRole;
import com.novus.shared_models.response.User.GetAuthenticatedUserDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableScheduling
@RequiredArgsConstructor
public class TestService {

    private final UserDaoUtils userDaoUtils;

    public ResponseEntity<String> setUserAsAdmin(User authenticatedUser) {
        authenticatedUser.setRole(UserRole.ADMIN);

        userDaoUtils.save(authenticatedUser);

        return ResponseEntity.ok("Ok");
    }

    public ResponseEntity<String> setUserAsSuperAdmin(User authenticatedUser) {
        authenticatedUser.setRole(UserRole.SUPER_ADMIN);

        userDaoUtils.save(authenticatedUser);

        return ResponseEntity.ok("Ok");
    }
}
