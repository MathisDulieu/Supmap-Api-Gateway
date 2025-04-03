package com.novus.api_gateway.dao;

import com.novus.database_utils.Route.RouteDao;
import com.novus.shared_models.common.Route.Route;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteDaoUtils {

    private final RouteDao<Route> routeDao;

    public RouteDaoUtils(MongoTemplate mongoTemplate) {
        this.routeDao = new RouteDao<>(mongoTemplate);
    }

    public List<Route> findByIds(List<String> routesIds) {
        return routeDao.findByIds(routesIds, Route.class);
    }

}
