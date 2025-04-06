package com.novus.api_gateway.dao;

import com.novus.database_utils.User.UserDao;
import com.novus.shared_models.common.User.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserDaoUtils {

    private final UserDao<User> userDao;

    public UserDaoUtils(MongoTemplate mongoTemplate) {
        this.userDao = new UserDao<>(mongoTemplate);
    }

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

    public List<User> searchUsersByUsernamePrefix(String keyword, int page) {
        return userDao.searchUsersByUsernamePrefix(keyword, page, User.class);
    }

    public void save(User user) {
        userDao.save(user);
    }

    public List<User> findNearByUsers(Double latitude, Double longitude) {
        return userDao.findNearByUsers(latitude, longitude, User.class);
    }

    public void updateUserActivityStatus() {
        userDao.updateUserActivityStatus();
    }
}
