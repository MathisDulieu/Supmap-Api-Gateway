package com.novus.api_gateway.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "supmap.properties")
public class EnvConfiguration {
    private String allowedOrigins;
    private String databaseName;
    private String elasticsearchPassword;
    private String elasticsearchUrl;
    private String elasticsearchUsername;
    private String jwtSecret;
    private String kafkaBootstrapServers;
    private String mongoUri;
    private String frontendBaseUrl;
    private String cloudinaryApiKey;
    private String cloudinaryCloudName;
    private String cloudinaryApiSecret;
    private String qrCodeApiUrl;
    public Map<String, String> getCloudinaryConfig() {
        return Map.of(
                "cloud_name", cloudinaryCloudName,
                "api_key", cloudinaryApiKey,
                "api_secret", cloudinaryApiSecret
        );
    }
}
