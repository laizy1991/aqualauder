package service;

import dao.UserDao;
import models.User;

public class UserService {

    public static User get(int id) {
        return UserDao.get(id);
    }

    public static void add(User user) {
        user.setCreateTime(System.currentTimeMillis());
        user.setUpdateTime(System.currentTimeMillis());
        UserDao.insert(user);
    }

    public static void delete(User user) {
        UserDao.delete(user);
    }

    public static void update(User user) {
        user.setUpdateTime(System.currentTimeMillis());
        UserDao.update(user);
    }
}
