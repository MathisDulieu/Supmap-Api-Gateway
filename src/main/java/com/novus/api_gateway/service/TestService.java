package com.novus.api_gateway.service;

import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.common.User.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserDaoUtils userDaoUtils;

    public ResponseEntity<String> setUserAsSuperAdmin(User authenticatedUser) {
        authenticatedUser.setRole(UserRole.SUPER_ADMIN);

        userDaoUtils.save(authenticatedUser);

        return ResponseEntity.ok("Ok");
    }
}
