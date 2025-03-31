package com.novus.api_gateway.service;

import com.novus.shared_models.request.Authentication.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    public ResponseEntity<String> register(RegisterRequest request) {
        return null;
    }

    public ResponseEntity<String> login(LoginRequest request) {
        return null;
    }

    public ResponseEntity<String> confirmEmail(String token) {
        return null;
    }

    public ResponseEntity<String> resendRegisterConfirmationEmail(ResendRegisterConfirmationEmailRequest request) {
        return null;
    }

    public ResponseEntity<String> sendForgotPasswordEmail(SendForgotPasswordRequest request) {
        return null;
    }

    public ResponseEntity<String> resetPassword(ResetPasswordRequest request) {
        return null;
    }

    public ResponseEntity<String> googleLogin(Authentication authentication) {
        return null;
    }
}
