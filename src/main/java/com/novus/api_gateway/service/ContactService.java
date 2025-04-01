package com.novus.api_gateway.service;

import com.novus.api_gateway.Producer;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.User.User;
import com.novus.shared_models.request.Contact.SendNewsletterRequest;
import com.novus.shared_models.request.Contact.SendSupportEmailRequest;
import com.novus.shared_models.request.Contact.SubscribeToNewsletterRequest;
import com.novus.shared_models.request.Contact.UnsubscribeFromNewsletterRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

import static java.util.Objects.isNull;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final Producer producer;

    public ResponseEntity<String> sendSupportEmail(SendSupportEmailRequest request, HttpServletRequest httpRequest) {
        if (isNull(request.getEmail()) || isEmpty(request.getEmail()) || isNull(request.getSubject()) || isEmpty(request.getSubject())
                || isNull(request.getContent()) || isEmpty(request.getContent())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        Map<String, String> kafkaRequest = Map.of(
                "subject", request.getSubject(),
                "content", request.getContent(),
                "email", request.getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "contact-service", "sendSupportEmail");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> subscribeToNewsletter(SubscribeToNewsletterRequest request, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "email", request.getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "contact-service", "subscribeToNewsletter");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> unsubscribeFromNewsletter(UnsubscribeFromNewsletterRequest request, HttpServletRequest httpRequest) {
        Map<String, String> kafkaRequest = Map.of(
                "email", request.getEmail()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(null, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "contact-service", "unsubscribeFromNewsletter");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

    public ResponseEntity<String> sendNewsletter(SendNewsletterRequest request, User authenticatedUser, HttpServletRequest httpRequest) {
        if (isNull(request.getSubject()) || isEmpty(request.getSubject()) || isNull(request.getContent()) || isEmpty(request.getContent())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }

        Map<String, String> kafkaRequest = Map.of(
                "subject", request.getSubject(),
                "content", request.getContent()
        );

        KafkaMessage kafkaMessage = producer.buildKafkaMessage(authenticatedUser, httpRequest, kafkaRequest);

        producer.send(kafkaMessage, "contact-service", "sendNewsletter");

        return ResponseEntity.status(HttpStatus.OK).body("message");
    }

}
