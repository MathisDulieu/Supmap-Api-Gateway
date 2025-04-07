package com.novus.api_gateway.service;

import com.novus.api_gateway.Producer;
import com.novus.api_gateway.dao.UserDaoUtils;
import com.novus.api_gateway.utils.UserUtils;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Authentication.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final Producer producer;
    private final UserDaoUtils userDaoUtils;
    private final UserUtils userUtils;
    private final JwtTokenService jwtTokenService;
    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<String> register(RegisterRequest request, HttpServletRequest httpRequest) {
        String error = userUtils.getRegisterValidationError(request);
        if (!isNull(error)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        Map<String, String> kafkaRequest = Map.of(
                "username", request.getUsername(),
                "email", request.getEmail(),
                "password", passwordEncoder.encode(request.getPassword())
                );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "register");

        return ResponseEntity.status(HttpStatus.OK).body("Registration successful! A verification email has been sent" +
                " to your address. Please check your inbox and follow the validation link before attempting to log in.");
    }

    public ResponseEntity<String> login(LoginRequest request, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userDaoUtils.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Please check your email address or register for a new account.");
        }

        if(!optionalUser.get().isValidEmail()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email not verified. Please check your inbox and follow the validation link sent during registration.");
        }

        if (!passwordEncoder.matches(request.getPassword(), optionalUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password. Please try again.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "email", request.getEmail(),
                "userId", optionalUser.get().getId()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "login");

        return ResponseEntity.status(HttpStatus.OK).body(jwtTokenService.generateToken(optionalUser.get().getId()));
    }

    public ResponseEntity<String> confirmEmail(String token, HttpServletRequest httpRequest) {
        if (!jwtTokenService.isEmailTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired verification token. Please request a new verification email.");
        }

        Optional<User> optionalUser = userDaoUtils.findById(jwtTokenService.resolveUserIdFromToken(token));
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User account not found. Please register again.");
        }

        if (optionalUser.get().isValidEmail()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already verified. You can log in to your account.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId(),
                "email", optionalUser.get().getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "confirmEmail");

        return ResponseEntity.status(HttpStatus.OK).body("Email successfully verified! You can now log in to your account.");
    }

    public ResponseEntity<String> resendRegisterConfirmationEmail(ResendRegisterConfirmationEmailRequest request, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userDaoUtils.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Please check your email address or register for a new account.");
        }

        if (optionalUser.get().isValidEmail()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already verified. You can log in to your account.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId(),
                "email", request.getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "resendRegisterConfirmationEmail");

        return ResponseEntity.status(HttpStatus.OK).body("Verification email has been resent. Please check your inbox" +
                " and follow the validation link to activate your account.");
    }

    public ResponseEntity<String> sendForgotPasswordEmail(SendForgotPasswordRequest request, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userDaoUtils.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found. Please check your email address or register for a new account.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId(),
                "email", optionalUser.get().getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "sendForgotPasswordEmail");

        return ResponseEntity.status(HttpStatus.OK).body("Password reset email has been sent. Please check your inbox" +
                " and follow the instructions to reset your password.");
    }

    public ResponseEntity<String> resetPassword(ResetPasswordRequest request, HttpServletRequest httpRequest) {
        if (userUtils.isNotStrongPassword(request.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not strong enough. Please use at" +
                    " least 8 characters including uppercase, lowercase, numbers, and special characters.");
        }

        if (!jwtTokenService.isPasswordResetTokenValid(request.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired password reset token. Please request a new password reset link.");
        }

        String userId = jwtTokenService.resolveUserIdFromToken(request.getToken());
        if (isNull(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access. User information could not be verified from the token.");
        }

        Optional<User> optionalUser = userDaoUtils.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User account not found. Please contact support for assistance.");
        }

        optionalUser.get().setPassword(request.getNewPassword());

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "resetPassword");

        return ResponseEntity.status(HttpStatus.OK).body("Password has been successfully reset. You can now log in with your new password.");
    }

    public ResponseEntity<String> googleLogin(Authentication authentication, HttpServletRequest httpRequest) {
        if (!(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access. Google OAuth authentication failed.");
        }

        String email = oAuth2User.getAttribute("email");
        if (isNull(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not provided by Google authentication." +
                    " Please ensure your Google account has a verified email address.");
        }

        Optional<User> optionalUser = userDaoUtils.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No account found with this Google email. Please register first.");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "googleLogin");

        return optionalUser.map(value -> ResponseEntity.status(HttpStatus.OK).body(jwtTokenService.generateToken(value.getId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User account not found. Please register with Google first."));
    }

}
