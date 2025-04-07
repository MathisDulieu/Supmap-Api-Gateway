package com.novus.api_gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.User.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

//@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;

    public void sendMessage(String message) {
        kafkaTemplate.send("authentication-service", message);
    }

    @Async("kafkaTaskExecutor")
    public void send(KafkaMessage kafkaMessage, String topic, String key) {
        try {
//            String json = objectMapper.writeValueAsString(kafkaMessage);
//
//            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, json);
//
//            future.whenComplete((result, exception) -> {
//                if (exception == null) {
//                    log.info("Message sent successfully to topic: {}, partition: {}, offset: {}, with key: {}",
//                            result.getRecordMetadata().topic(),
//                            result.getRecordMetadata().partition(),
//                            result.getRecordMetadata().offset(),
//                            key);
//                } else {
//                    log.error("Error while sending message asynchronously: {}", exception.getMessage(), exception);
//                }
//            });
        } catch (Exception exception) {
//            log.error("Error while preparing message: {}", exception.getMessage(), exception);
        }
    }

    public KafkaMessage buildKafkaMessage(User authenticatedUser, HttpServletRequest httpServletRequest, Map<String, String> request) {
        return KafkaMessage.builder()
                .authenticatedUser(authenticatedUser)
                .ipAddress(getClientIP(httpServletRequest))
                .request(request)
                .timeStamp(String.valueOf(System.currentTimeMillis()))
                .build();
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}