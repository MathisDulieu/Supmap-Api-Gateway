package com.novus.api_gateway.configuration;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Component
public class KafkaTopicChecker implements CommandLineRunner {

    @Autowired
    private AdminClient adminClient;

    @Bean
    public AdminClient adminClient() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka.railway.internal:29092");
        return AdminClient.create(configs);
    }

    @Override
    public void run(String... args) throws Exception {
        ListTopicsResult topics = adminClient.listTopics();
        Set<String> topicNames = topics.names().get();
        System.out.println("TOPICS DISPONIBLES:");
        for (String name : topicNames) {
            System.out.println(" - " + name);
        }
    }

    public void getTopicInfo(String topicName) throws Exception {
        DescribeTopicsResult topicInfo = adminClient.describeTopics(Collections.singletonList(topicName));
        Map<String, TopicDescription> descriptions = topicInfo.allTopicNames().get();

        for (Map.Entry<String, TopicDescription> entry : descriptions.entrySet()) {
            TopicDescription desc = entry.getValue();
            System.out.println("Topic: " + desc.name());
            System.out.println("Partitions: " + desc.partitions().size());
            for (TopicPartitionInfo partition : desc.partitions()) {
                System.out.println("  Partition: " + partition.partition());
                System.out.println("  Leader: " + partition.leader().id());
                System.out.println("  Replicas: " +
                        partition.replicas().stream().map(Node::id).toList());
            }
        }
    }

}
