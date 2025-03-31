package com.novus.api_gateway;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

    private static final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final KafkaProducer<String, String> kafkaProducer;
    private final ObjectMapper objectMapper;

    public void send(Object entity, String topic, String key) {
        try {
            String json = objectMapper.writeValueAsString(entity);

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

}
