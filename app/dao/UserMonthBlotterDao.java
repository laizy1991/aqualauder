package dao;

import java.util.List;

import javax.persistence.Query;

import play.db.jpa.Model;
import models.DistributorSuperior;
import models.UserMonthBlotter;

public class UserMonthBlotterDao {

    public static List<UserMonthBlotter> getByUserIds(List<Integer> userIds) {
        String ids = "";
        String split = "";
        for(Integer id : userIds) {
            ids = ids + split + id;
            split = ",";
        }
        String sql = "SELECT * FROM user_month_blotters where user_id in (%s);";  
        Query query = Model.em().createNativeQuery(String.format(sql, ids),UserMonthBlotter.class);  
        return query.getResultList();  
    }
    
    
    public static UserMonthBlotter get(int month, int userId) {
        return UserMonthBlotter.find("blotterMonth = ? and userId = ?", month, userId).first();
    }
    
    public static void save(UserMonthBlotter item) {
        item.save();
    }
}
