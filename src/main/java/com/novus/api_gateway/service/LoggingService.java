package com.novus.api_gateway.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.novus.api_gateway.configuration.DateConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LoggingService {

    private final ElasticsearchClient elasticsearchClient;
    private final DateConfiguration dateConfiguration;

    public void logApiRequest(String serviceName, String endpoint, String method, int statusCode, long responseTime) {
        try {
            Map<String, Object> document = new HashMap<>();
            document.put("timestamp", dateConfiguration.newDate());
            document.put("service", serviceName);
            document.put("endpoint", endpoint);
            document.put("method", method);
            document.put("status_code", statusCode);
            document.put("response_time_ms", responseTime);

            elasticsearchClient.index(i -> i
                    .index("api_logs")
                    .document(document)
            );

        } catch (IOException e) {
            System.err.println("Error logging to Elasticsearch: " + e.getMessage());
        }
    }

}
