package com.novus.api_gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TestProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(String topic, String key, String message) {
        log.info("Sending message to topic: {}, key: {}", topic, key);

        kafkaTemplate.send(topic, key, message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent successfully to topic: {}, partition: {}, offset: {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Failed to send message to topic: {}", topic, ex);
                    }
                });
    }

    public void sendObject(String topic, String key, Object data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            sendMessage(topic, key, jsonData);
        } catch (Exception e) {
            log.error("Error serializing object to JSON", e);
        }
    }
}