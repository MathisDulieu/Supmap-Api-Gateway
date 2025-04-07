package com.novus.api_gateway.configuration;

import com.novus.api_gateway.KafkaMessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    private static String bootstrapServers = "kafka.railway.internal:29092";
    private static String topic = "authentication-service";

    @Bean
    public KafkaMessageProducer kafkaMessageProducer() {
        return new KafkaMessageProducer(bootstrapServers, topic);
    }

}
