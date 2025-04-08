package com.novus.api_gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String data) {
        kafkaTemplate.send("authenticationTopic", data);
    }

}
