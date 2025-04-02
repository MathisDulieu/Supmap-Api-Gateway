package com.novus.api_gateway.service;

import com.novus.api_gateway.Producer;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Notification.UpdateAuthenticatedUserNotificationPreferencesRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final Producer producer;

    public ResponseEntity<String> updateAuthenticatedUserNotificationPreferences(UpdateAuthenticatedUserNotificationPreferencesRequest request,
                                                                                 User authenticatedUser, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "isEmail", String.valueOf(request.isEmail())
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "notification-service", "updateAuthenticatedUserNotificationPreferences");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

}
