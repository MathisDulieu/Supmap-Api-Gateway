package com.novus.api_gateway.dao;

import com.novus.database_utils.AdminDashboard.AdminDashboardDao;
import com.novus.database_utils.User.UserDao;
import com.novus.shared_models.common.AdminDashboard.AdminDashboard;
import com.novus.shared_models.common.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminDashboardDaoUtils {

    private final MongoTemplate mongoTemplate;
    private final AdminDashboardDao<AdminDashboard> adminDashboardDao = new AdminDashboardDao<>(mongoTemplate);

    public Optional<AdminDashboard> find() {
        return adminDashboardDao.findMe(AdminDashboard.class);
    }

}
