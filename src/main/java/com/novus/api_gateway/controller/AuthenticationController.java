package com.novus.api_gateway.controller;

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

    @PostMapping("/auth/register")
    @AuthenticationControllerDoc.RegisterDoc
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.register(request, httpRequest);
    }

    @PostMapping("/auth/login")
    @AuthenticationControllerDoc.LoginDoc
    public ResponseEntity<String> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.login(request, httpRequest);
    }

    @PostMapping("/auth/confirm-email")
    @AuthenticationControllerDoc.ConfirmEmailDoc
    public ResponseEntity<String> confirmEmail(
            @RequestParam String token,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.confirmEmail(token, httpRequest);
    }

    @PostMapping("/auth/resend/register-confirmation-email")
    @AuthenticationControllerDoc.ResendRegisterConfirmationEmailDoc
    public ResponseEntity<String> resendRegisterConfirmationEmail(
            @RequestBody ResendRegisterConfirmationEmailRequest request,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.resendRegisterConfirmationEmail(request, httpRequest);
    }

    @PostMapping("/auth/forgot-password")
    @AuthenticationControllerDoc.SendForgotPasswordEmailDoc
    public ResponseEntity<String> sendForgotPasswordEmail(
            @RequestBody SendForgotPasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.sendForgotPasswordEmail(request, httpRequest);
    }

    @PostMapping("/auth/reset-password")
    @AuthenticationControllerDoc.ResetPasswordDoc
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequest request,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.resetPassword(request, httpRequest);
    }

    @GetMapping("/oauth/google-login")
    @AuthenticationControllerDoc.GoogleLoginDoc
    public ResponseEntity<String> googleLogin(
            Authentication authentication,
            HttpServletRequest httpRequest
    ) {
        return authenticationService.googleLogin(authentication, httpRequest);
    }

}
