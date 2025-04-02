package com.novus.api_gateway.dao;

import com.novus.database_utils.AdminDashboard.AdminDashboardDao;
import com.novus.shared_models.common.AdminDashboard.AdminDashboard;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminDashboardDaoUtils {

    private final AdminDashboardDao<AdminDashboard> adminDashboardDao;

    public AdminDashboardDaoUtils(MongoTemplate mongoTemplate) {
        this.adminDashboardDao = new AdminDashboardDao<>(mongoTemplate);
    }

    public Optional<AdminDashboard> find() {
        return adminDashboardDao.findMe(AdminDashboard.class);
    }

}
