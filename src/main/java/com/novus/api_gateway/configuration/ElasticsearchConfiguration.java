package com.novus.api_gateway.configuration;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@RequiredArgsConstructor
public class ElasticsearchConfiguration {

    private final EnvConfiguration envConfiguration;

    private static final int CONNECT_TIMEOUT = 5000;
    private static final int SOCKET_TIMEOUT = 60000;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        try {
            URI uri = new URI(envConfiguration.getElasticsearchUrl());
            String host = uri.getHost();
            int port = uri.getPort() != -1 ? uri.getPort() : (uri.getScheme().equals("https") ? 443 : 80);
            String scheme = uri.getScheme();

            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(envConfiguration.getElasticsearchUsername(), envConfiguration.getElasticsearchPassword())
            );

            RestClientBuilder restClientBuilder = RestClient.builder(
                            new HttpHost(host, port, scheme))
                    .setHttpClientConfigCallback(httpClientBuilder ->
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                    .setRequestConfigCallback(requestConfigBuilder ->
                            requestConfigBuilder
                                    .setConnectTimeout(CONNECT_TIMEOUT)
                                    .setSocketTimeout(SOCKET_TIMEOUT));

            RestClient restClient = restClientBuilder.build();

            ElasticsearchTransport transport = new RestClientTransport(
                    restClient,
                    new JacksonJsonpMapper()
            );

            return new ElasticsearchClient(transport);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid Elasticsearch URL", e);
        }
    }

}
