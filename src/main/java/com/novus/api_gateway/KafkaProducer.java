package com.novus.api_gateway;

import com.novus.api_gateway.configuration.KafkaTopicChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicChecker kafkaTopicChecker;

    public void send(String data) throws Exception {
        kafkaTemplate.send("authenticationTopic", data);
        kafkaTopicChecker.getTopicInfo("authenticationTopic");
    }
}