package com.novus.api_gateway.service;

import com.novus.api_gateway.Producer;
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
                "email", request.getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "register");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> login(LoginRequest request, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userUtils.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        }

        if(!optionalUser.get().isValidEmail()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("error");
        }

        if (!passwordEncoder.matches(request.getPassword(), optionalUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        Optional<User> optionalUser = userUtils.findById(jwtTokenService.resolveUserIdFromToken(token));
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        }

        if (optionalUser.get().isValidEmail()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId(),
                "email", optionalUser.get().getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "confirmEmail");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> resendRegisterConfirmationEmail(ResendRegisterConfirmationEmailRequest request, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userUtils.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        }

        if (optionalUser.get().isValidEmail()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId(),
                "email", request.getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "resendRegisterConfirmationEmail");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> sendForgotPasswordEmail(SendForgotPasswordRequest request, HttpServletRequest httpRequest) {
        Optional<User> optionalUser = userUtils.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId(),
                "email", optionalUser.get().getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "sendForgotPasswordEmail");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> resetPassword(ResetPasswordRequest request, HttpServletRequest httpRequest) {
        if (userUtils.isNotStrongPassword(request.getNewPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        if (!jwtTokenService.isPasswordResetTokenValid(request.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        String userId = jwtTokenService.resolveUserIdFromToken(request.getToken());
        if (isNull(userId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
        }

        Optional<User> optionalUser = userUtils.findById(userId);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        }

        optionalUser.get().setPassword(request.getNewPassword());

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "resetPassword");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> googleLogin(Authentication authentication, HttpServletRequest httpRequest) {
        if (!(authentication.getPrincipal() instanceof OAuth2User oAuth2User)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("error");
        }

        String email = oAuth2User.getAttribute("email");
        if (isNull(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        Optional<User> optionalUser = userUtils.findByEmail(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("error");
        }

        Map<String, String> kafkaRequest = Map.of(
                "userId", optionalUser.get().getId()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "authentication-service", "googleLogin");

        return optionalUser.map(value -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(jwtTokenService.generateToken(value.getId())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("error"));
    }
}
