package com.novus.api_gateway.utils;

import com.novus.database_utils.User.UserDao;
import com.novus.shared_models.common.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class UserUtils {

    private final MongoTemplate mongoTemplate;
    private final UserDao<User> userDao = new UserDao<>(mongoTemplate);

    public void save(User user) {
        userDao.save(user);
    }

    public Optional<User> findById(String id) {
        return userDao.findById(id, User.class);
    }

}
