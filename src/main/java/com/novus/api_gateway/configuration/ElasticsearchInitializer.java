package com.novus.api_gateway.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.StringReader;

@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchInitializer implements CommandLineRunner {

    private final ElasticsearchClient elasticsearchClient;

    @Override
    public void run(String... args) {
        createIndexIfNotExists(createApiLogsMapping());
    }

    private void createIndexIfNotExists(String mapping) {
        try {
            boolean indexExists = elasticsearchClient.indices().exists(e -> e
                    .index("api_logs")
            ).value();

            if (!indexExists) {
                CreateIndexResponse createResponse = elasticsearchClient.indices().create(c -> c
                        .index("api_logs")
                        .withJson(new StringReader(mapping))
                );

                log.info("Index api_logs created: " + createResponse.acknowledged());
            } else {
                log.warn("Index api_logs already exists");
            }
        } catch (Exception e) {
            log.error("Error creating index api_logs: " + e.getMessage());
        }
    }

    private String createApiLogsMapping() {
        return """
                {
                  "mappings": {
                    "properties": {
                      "timestamp": { "type": "date" },
                      "service": { "type": "keyword" },
                      "endpoint": { "type": "keyword" },
                      "method": { "type": "keyword" },
                      "status_code": { "type": "integer" },
                      "response_time_ms": { "type": "long" }
                    }
                  }
                }
                """;
    }

}
