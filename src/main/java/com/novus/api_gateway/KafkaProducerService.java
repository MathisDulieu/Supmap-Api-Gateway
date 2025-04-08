package com.novus.api_gateway;

import com.novus.shared_models.common.Kafka.KafkaMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendEvent(String topic, KafkaMessage event) {
        logger.info("Envoi d'un événement au topic {}: {}", topic, event);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic,  event.getIpAddress(), event);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Événement envoyé avec succès au topic {} [partition: {}, offset: {}]",
                        topic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                logger.error("Échec de l'envoi de l'événement au topic {}: {}", topic, ex.getMessage(), ex);
            }
        });
    }

}