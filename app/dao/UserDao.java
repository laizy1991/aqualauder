package dao;

import java.util.List;

import models.User;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class UserDao {

    public static boolean insert(User user) {
        if(user == null) {
            return false;
        }
        return user.create();
    }
    
    public static User get(int id) {
        return User.findById(id);
    }
    
    /**
     * 通过OpenId获取用户信息
     * @param openId
     * @return
     */
    public static User getyOpenId (String openId) {
    	if(StringUtils.isEmpty(openId)) {
    		return null;
    	}
        List<User> list = User.find("open_id", openId).fetch();
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        return list.get(0);
    }
    
    public static void update(User user) {
        if(user == null || user.getUserId() == 0) {
            return;
        }
        user.save();
    }

    public static void delete(User user) {
        if (user != null) {
            user.delete();
        }
    }
}
