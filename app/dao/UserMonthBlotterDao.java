package dao;

import java.util.List;

import models.UserMonthBlotter;

public class UserMonthBlotterDao {

    public static List<UserMonthBlotter> getByIds(List<Integer> userIds) {
        return UserMonthBlotter.find("userId in ?", userIds).fetch();
    }
    
    
    public static UserMonthBlotter get(int month, int userId) {
        return UserMonthBlotter.find("blotterMonth = ? and userId = ?", month, userId).first();
    }
    
    public static void save(UserMonthBlotter item) {
        item.save();
    }
}
