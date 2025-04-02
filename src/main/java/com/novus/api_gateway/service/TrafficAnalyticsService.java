package com.novus.api_gateway.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrafficAnalyticsService {

    private final ElasticsearchClient elasticsearchClient;



}
