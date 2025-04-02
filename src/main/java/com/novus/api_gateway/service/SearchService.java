package com.novus.api_gateway.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ElasticsearchClient elasticsearchClient;

    public List<Map<String, Object>> searchRoutes(String startPoint, String endPoint) {
        try {
            SearchResponse<Map> response = elasticsearchClient.search(s -> s
                            .index("routes")
                            .query(q -> q
                                    .bool(b -> b
                                            .must(m -> m
                                                    .match(t -> t
                                                            .field("start_point")
                                                            .query(startPoint)
                                                    )
                                            )
                                            .must(m -> m
                                                    .match(t -> t
                                                            .field("end_point")
                                                            .query(endPoint)
                                                    )
                                            )
                                    )
                            ),
                    Map.class
            );

            List<Map<String, Object>> results = new ArrayList<>();
            for (Hit<Map> hit : response.hits().hits()) {
                results.add(hit.source());
            }

            return results;
        } catch (IOException e) {
            throw new RuntimeException("Error searching routes", e);
        }
    }

}
