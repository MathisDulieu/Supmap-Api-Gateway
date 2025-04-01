package com.novus.api_gateway.dao;

import com.novus.database_utils.User.UserDao;
import com.novus.shared_models.common.User.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDaoUtils {

    private final MongoTemplate mongoTemplate;
    private final UserDao<User> userDao = new UserDao<>(mongoTemplate);

    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email, User.class);
    }

    public Optional<User> findById(String id) {
        return userDao.findById(id, User.class);
    }

    public boolean isUsernameAlreadyUsed(String username) {
        return userDao.isUsernameAlreadyUsed(username);
    }

    public boolean isEmailAlreadyUsed(String email) {
        return userDao.isEmailAlreadyUsed(email);
    }

}
