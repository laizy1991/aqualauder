package dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import models.UserMonthBlotter;

import org.apache.commons.collections.CollectionUtils;

import play.db.jpa.Model;

import com.google.gson.Gson;

public class UserMonthBlotterDao {

    public static List<UserMonthBlotter> getByUserIds(List<Integer> userIds) {
        if(userIds == null || userIds.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
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
    
    public static long getSaleAmount(int userId) {
        String sql = "SELECT * FROM user_month_blotters where user_id = %s;";  
        Query query = Model.em().createNativeQuery(String.format(sql, userId),UserMonthBlotter.class);
        List<UserMonthBlotter> list = query.getResultList();
        if(list == null || list.isEmpty()) {
            return 0;
        }
        System.err.println(new Gson().toJson(list.get(0)));
        return list.get(0).getMonthBlotters(); 
    }
    
    public static UserMonthBlotter get(int month, int userId) {
        return UserMonthBlotter.find("blotterMonth = ? and userId = ?", month, userId).first();
    }
    
    public static void save(UserMonthBlotter item) {
        item.save();
    }
}
