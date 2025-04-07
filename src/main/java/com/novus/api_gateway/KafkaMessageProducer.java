package com.novus.api_gateway;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaMessageProducer {

    private final KafkaProducer<String, String> producer;
    private final String topic;

    public KafkaMessageProducer(String bootstrapServers, String topic) {
        this.topic = topic;

        // Configuration du producer
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Configurations additionnelles pour la fiabilité
        properties.put(ProducerConfig.ACKS_CONFIG, "all"); // Attendre confirmation de tous les replicas
        properties.put(ProducerConfig.RETRIES_CONFIG, 3);  // Nombre de tentatives en cas d'échec
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1); // Petit délai pour grouper les messages
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Éviter les doublons

        this.producer = new KafkaProducer<>(properties);
    }

    public void sendMessage(String key, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

        try {
            // Envoi synchrone (pour simplifier cet exemple)
            producer.send(record).get();
            System.out.println("Message envoyé avec succès - Clé: " + key + ", Valeur: " + message);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Erreur lors de l'envoi du message: " + e.getMessage());
        }
    }

    // Version asynchrone avec callback
    public void sendMessageAsync(String key, String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Message envoyé avec succès - Topic: " + metadata.topic() +
                        ", Partition: " + metadata.partition() +
                        ", Offset: " + metadata.offset() +
                        ", Clé: " + key +
                        ", Valeur: " + message);
            } else {
                System.err.println("Erreur lors de l'envoi du message: " + exception.getMessage());
            }
        });
    }

    public void close() {
        producer.close();
    }

    // Exemple d'utilisation
    public static void main(String[] args) {
        String bootstrapServers = "localhost:9092";
        String topic = "mon-topic";

        KafkaMessageProducer producer = new KafkaMessageProducer(bootstrapServers, topic);

        try {
            // Envoi de quelques messages
            for (int i = 0; i < 10; i++) {
                String key = "key-" + i;
                String message = "Message " + i + " envoyé à " + System.currentTimeMillis();

                // Version synchrone
                producer.sendMessage(key, message);

                // Ou version asynchrone
                // producer.sendMessageAsync(key, message);

                Thread.sleep(1000); // Pause d'une seconde entre les messages
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }
}