package com.novus.api_gateway.dao;

import com.novus.database_utils.Alert.AlertDao;
import com.novus.shared_models.GeoPoint;
import com.novus.shared_models.common.Alert.Alert;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class AlertDaoUtils {

    private final AlertDao<Alert> alertDao;

    public AlertDaoUtils(MongoTemplate mongoTemplate) {
        this.alertDao = new AlertDao<>(mongoTemplate);
    }

    public List<Alert> findAlertsByPosition(double latitude, double longitude) {
        return alertDao.findAlertsByPosition(latitude, longitude, Alert.class);
    }

    public List<Alert> findAlertsByRoute(List<GeoPoint> routePoints) {
        return alertDao.findAlertsByRoute(routePoints, Alert.class);
    }

    public Optional<Alert> findById(String id) {
        return alertDao.findById(id, Alert.class);
    }
}
