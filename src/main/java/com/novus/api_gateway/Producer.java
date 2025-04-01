package com.novus.api_gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.novus.shared_models.common.Kafka.KafkaMessage;
import com.novus.shared_models.common.User.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final KafkaProducer<String, String> kafkaProducer;
    private final ObjectMapper objectMapper;

    public void send(KafkaMessage kafkaMessage, String topic, String key) {
        try {
            String json = objectMapper.writeValueAsString(kafkaMessage);

            ProducerRecord<String, String> record = new ProducerRecord<>(
                    topic,
                    key,
                    json
            );

            kafkaProducer.send(record);
            logger.info("Message sent to topic: {}, with key: {}", topic, key);
        } catch (Exception exception) {
            logger.error("Error while sending message: {}", exception.getMessage());
            throw new RuntimeException(exception);
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
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

}
