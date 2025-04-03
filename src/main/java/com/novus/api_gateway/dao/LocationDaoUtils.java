package com.novus.api_gateway.dao;

import com.novus.database_utils.Location.LocationDao;
import com.novus.shared_models.common.Location.Location;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LocationDaoUtils {

    private final LocationDao<Location> locationDao;

    public LocationDaoUtils(MongoTemplate mongoTemplate) {
        this.locationDao = new LocationDao<>(mongoTemplate);
    }

    public Optional<Location> findById(String id) {
        return locationDao.findById(id, Location.class);
    }

    public List<Location> findByIds(List<String> favoriteLocationIds) {
        return locationDao.findByIds(favoriteLocationIds, Location.class);
    }
}
