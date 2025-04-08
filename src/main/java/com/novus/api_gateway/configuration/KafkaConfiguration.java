//package com.novus.api_gateway.configuration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.Executor;
//
//@Slf4j
//@Configuration
//@EnableAsync
//@RequiredArgsConstructor
//public class KafkaConfiguration {
//
//    private final EnvConfiguration envConfiguration;
//
//    @Bean
//    public ProducerFactory<String, String> producerFactory() {
//        String bootstrapServers = envConfiguration.getKafkaBootstrapServers();
//        log.info("Configuring Kafka producer with bootstrap servers: {}", bootstrapServers);
//
//        Map<String, Object> props = new HashMap<>();
//        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        props.put(ProducerConfig.ACKS_CONFIG, "all");
//        props.put(ProducerConfig.RETRIES_CONFIG, 3);
//        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 100);
//        props.put(ProducerConfig.LINGER_MS_CONFIG, 10);
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 32768);
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
//        props.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
//        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
//        props.put(ProducerConfig.CLIENT_ID_CONFIG, "api-gateway-producer");
//        return new DefaultKafkaProducerFactory<>(props);
//    }
//
//    @Bean
//    public KafkaTemplate<String, String> kafkaTemplate() {
//        return new KafkaTemplate<>(producerFactory());
//    }
//
//    @Bean
//    public Executor kafkaTaskExecutor() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(4);
//        executor.setMaxPoolSize(10);
//        executor.setQueueCapacity(500);
//        executor.setThreadNamePrefix("kafka-producer-");
//        executor.setWaitForTasksToCompleteOnShutdown(true);
//        executor.setAwaitTerminationSeconds(60);
//        executor.initialize();
//        return executor;
//    }
//
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        return mapper;
//    }
//}