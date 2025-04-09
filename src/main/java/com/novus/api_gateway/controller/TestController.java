package com.novus.api_gateway.controller;

import com.novus.api_gateway.service.TestService;
import com.novus.api_gateway.swagger.UserControllerDocs;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.response.User.GetAuthenticatedUserDetailsResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    @PostMapping("/set-admin")
    @Operation(
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> setUserAsAdmin(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return testService.setUserAsAdmin(authenticatedUser);
    }

    @PostMapping("/set-super-admin")
    @Operation(
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<String> setUserAsSuperAdmin(
            @AuthenticationPrincipal User authenticatedUser
    ) {
        return testService.setUserAsSuperAdmin(authenticatedUser);
    }

}
