package com.novus.api_gateway.controller;

import com.novus.api_gateway.service.AuthenticationService;
import com.novus.api_gateway.swagger.AuthenticationControllerDoc;
import com.novus.shared_models.request.Authentication.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/register")
    @AuthenticationControllerDoc.RegisterDoc
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {
        return authenticationService.register(request);
    }

    @PostMapping("/auth/login")
    @AuthenticationControllerDoc.LoginDoc
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request) {
        return authenticationService.login(request);
    }

    @PostMapping("/auth/confirm-email")
    @AuthenticationControllerDoc.ConfirmEmailDoc
    public ResponseEntity<String> confirmEmail(@RequestParam String token) {
        return authenticationService.confirmEmail(token);
    }

    @PostMapping("/auth/resend/register-confirmation-email")
    @AuthenticationControllerDoc.ResendRegisterConfirmationEmailDoc
    public ResponseEntity<String> resendRegisterConfirmationEmail(
            @RequestBody ResendRegisterConfirmationEmailRequest request
    ) {
        return authenticationService.resendRegisterConfirmationEmail(request);
    }

    @PostMapping("/auth/forgot-password")
    @AuthenticationControllerDoc.SendForgotPasswordEmailDoc
    public ResponseEntity<String> sendForgotPasswordEmail(
            @RequestBody SendForgotPasswordRequest request
    ) {
        return authenticationService.sendForgotPasswordEmail(request);
    }

    @PostMapping("/auth/reset-password")
    @AuthenticationControllerDoc.ResetPasswordDoc
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        return authenticationService.resetPassword(request);
    }

    @GetMapping("/oauth/google-login")
    @AuthenticationControllerDoc.GoogleLoginDoc
    public ResponseEntity<String> googleLogin(Authentication authentication) {
        return authenticationService.googleLogin(authentication);
    }

}
