package com.novus.api_gateway.dao;

import com.novus.database_utils.Location.LocationDao;
import com.novus.shared_models.common.Location.Location;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LocationDaoUtils {

    private final MongoTemplate mongoTemplate;
    private final LocationDao<Location> locationDao = new LocationDao<>(mongoTemplate);

    public Optional<Location> findById(String id) {
        return locationDao.findById(id, Location.class);
    }

}
