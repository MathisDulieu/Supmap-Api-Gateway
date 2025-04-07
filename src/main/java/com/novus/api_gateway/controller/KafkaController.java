package com.novus.api_gateway.controller;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;

@RestController
@RequestMapping("/api/kafka-diagnostic")
public class KafkaController {

    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    private static final String bootstrapServers = "kafka.railway.internal:29092";

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getKafkaInfo() {
        Map<String, Object> result = new HashMap<>();
        result.put("bootstrapServers", bootstrapServers);

        // Essayer de se connecter à Kafka
        Properties adminProps = new Properties();
        adminProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        try (AdminClient adminClient = AdminClient.create(adminProps)) {
            // Liste des topics
            try {
                ListTopicsResult topics = adminClient.listTopics();
                Set<String> topicNames = topics.names().get();
                result.put("topics", topicNames);

                // Détails du topic authentication-service
                if (topicNames.contains("authentication-service")) {
                    Map<String, KafkaFuture<TopicDescription>> topicInfo =
                            adminClient.describeTopics(Collections.singleton("authentication-service")).topicNameValues();
                    TopicDescription desc = topicInfo.get("authentication-service").get();
                    result.put("authentication-service-info", desc.toString());
                }
            } catch (Exception e) {
                result.put("topics_error", e.getMessage());
                logger.error("Erreur lors de la récupération des topics", e);
            }

            // Liste des groupes de consommateurs
            try {
                ListConsumerGroupsResult groups = adminClient.listConsumerGroups();
                Collection<ConsumerGroupListing> groupsList = groups.all().get();
                List<String> groupIds = new ArrayList<>();
                for (ConsumerGroupListing group : groupsList) {
                    groupIds.add(group.groupId());
                }
                result.put("consumer_groups", groupIds);
            } catch (Exception e) {
                result.put("consumer_groups_error", e.getMessage());
                logger.error("Erreur lors de la récupération des groupes de consommateurs", e);
            }

            result.put("status", "connected");
        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
            logger.error("Erreur de connexion à Kafka", e);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/messages/{topic}")
    public ResponseEntity<List<Map<String, Object>>> getTopicMessages(
            @PathVariable String topic) {
        List<Map<String, Object>> messages = new ArrayList<>();

        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "diagnostic-" + UUID.randomUUID());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 100);

        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList(topic));

            // Essayer de récupérer les messages avec un timeout
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));

            if (records.isEmpty()) {
                // Si aucun message n'est disponible avec l'approche subscribe, essayer avec assign
                Set<TopicPartition> partitions = new java.util.HashSet<>();
                for (org.apache.kafka.common.PartitionInfo partitionInfo : consumer.partitionsFor(topic)) {
                    partitions.add(new TopicPartition(topic, partitionInfo.partition()));
                }

                consumer.assign(partitions);
                consumer.seekToBeginning(partitions);
                records = consumer.poll(Duration.ofSeconds(10));
            }

            for (ConsumerRecord<String, String> record : records) {
                Map<String, Object> messageMap = new HashMap<>();
                messageMap.put("offset", record.offset());
                messageMap.put("partition", record.partition());
                messageMap.put("timestamp", record.timestamp());
                messageMap.put("key", record.key());
                messageMap.put("value", record.value());
                messages.add(messageMap);
            }

            logger.info("Récupéré {} messages du topic {}", messages.size(), topic);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des messages du topic {}", topic, e);
            return ResponseEntity.status(500).body(Collections.singletonList(
                    Collections.singletonMap("error", e.getMessage())
            ));
        }

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/send-test/{topic}")
    public ResponseEntity<Map<String, String>> sendTestMessage(@PathVariable String topic) {
        Map<String, String> result = new HashMap<>();

        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        try (org.apache.kafka.clients.producer.KafkaProducer<String, String> producer =
                     new org.apache.kafka.clients.producer.KafkaProducer<>(props)) {

            String key = "test-key-" + System.currentTimeMillis();
            String value = "Message de test envoyé le " + new java.util.Date();

            org.apache.kafka.clients.producer.ProducerRecord<String, String> record =
                    new org.apache.kafka.clients.producer.ProducerRecord<>(topic, key, value);

            producer.send(record).get();

            result.put("status", "success");
            result.put("message", "Message envoyé: " + value);
            logger.info("Message de test envoyé au topic {}: {}", topic, value);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("error", e.getMessage());
            logger.error("Erreur lors de l'envoi du message de test au topic {}", topic, e);
        }

        return ResponseEntity.ok(result);
    }
}
