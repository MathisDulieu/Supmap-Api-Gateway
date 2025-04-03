package com.novus.api_gateway.controller;

import com.novus.api_gateway.prometheus.AuthenticationMetrics;
import com.novus.api_gateway.service.AuthenticationService;
import com.novus.api_gateway.swagger.AuthenticationControllerDoc;
import com.novus.shared_models.request.Authentication.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final AuthenticationMetrics metrics;

    @PostMapping("/auth/register")
    @AuthenticationControllerDoc.RegisterDoc
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.getRegisterTimer().record(() -> authenticationService.register(request, httpRequest));

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordSuccessfulAuth();
        } else {
            metrics.recordFailedAuth();
        }

        metrics.recordOperation("register", response.getStatusCode().value());
        return response;
    }

    @PostMapping("/auth/login")
    @AuthenticationControllerDoc.LoginDoc
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.getLoginTimer().record(() -> authenticationService.login(request, httpRequest));

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordSuccessfulAuth();
            metrics.recordAuthenticationType("password");
        } else {
            metrics.recordFailedAuth();
        }

        metrics.recordOperation("login", response.getStatusCode().value());
        return response;
    }

    @PostMapping("/auth/confirm-email")
    @AuthenticationControllerDoc.ConfirmEmailDoc
    public ResponseEntity<String> confirmEmail(
            @RequestParam String token,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.getConfirmEmailTimer().record(() -> authenticationService.confirmEmail(token, httpRequest));
        metrics.recordOperation("confirm_email", response.getStatusCode().value());
        return response;
    }

    @PostMapping("/auth/resend/register-confirmation-email")
    @AuthenticationControllerDoc.ResendRegisterConfirmationEmailDoc
    public ResponseEntity<String> resendRegisterConfirmationEmail(
            @RequestBody ResendRegisterConfirmationEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = authenticationService.resendRegisterConfirmationEmail(request, httpRequest);
        metrics.recordOperation("resend_confirmation", response.getStatusCode().value());
        return response;
    }

    @PostMapping("/auth/forgot-password")
    @AuthenticationControllerDoc.SendForgotPasswordEmailDoc
    public ResponseEntity<String> sendForgotPasswordEmail(
            @RequestBody SendForgotPasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.getForgotPasswordTimer().record(() -> authenticationService.sendForgotPasswordEmail(request, httpRequest));
        metrics.recordOperation("forgot_password", response.getStatusCode().value());
        return response;
    }

    @PostMapping("/auth/reset-password")
    @AuthenticationControllerDoc.ResetPasswordDoc
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.getResetPasswordTimer().record(() -> authenticationService.resetPassword(request, httpRequest));
        metrics.recordOperation("reset_password", response.getStatusCode().value());
        return response;
    }

    @GetMapping("/oauth/google-login")
    @AuthenticationControllerDoc.GoogleLoginDoc
    public ResponseEntity<String> googleLogin(
            Authentication authentication,
            HttpServletRequest httpRequest
    ) {
        ResponseEntity<String> response = metrics.getOauthLoginTimer().record(() -> authenticationService.googleLogin(authentication, httpRequest));

        if (response.getStatusCode().is2xxSuccessful()) {
            metrics.recordSuccessfulAuth();
            metrics.recordAuthenticationType("oauth_google");
        } else {
            metrics.recordFailedAuth();
        }

        metrics.recordOperation("oauth_login", response.getStatusCode().value());
        return response;
    }

}
