package service;

import org.apache.commons.lang.StringUtils;

import dao.UserDao;
import models.User;

public class UserService {

    public static User get(int id) {
        return UserDao.get(id);
    }
    
    /**
     * 通过OpenId获取用户信息
     * @param openId
     * @return
     */
    public static User getByOpenId(String openId) {
    	if(StringUtils.isEmpty(openId)) {
    		return null;
    	}
    	return UserDao.getyOpenId(openId);
    }

    public static boolean add(User user) {
        return UserDao.insert(user);
    }

    public static void delete(User user) {
        UserDao.delete(user);
    }

    public static void update(User user) {
        user.setUpdateTime(System.currentTimeMillis());
        UserDao.update(user);
    }
    
    
    public static void setWeixin(int userId, String weixin) {
        User user = UserDao.get(userId);
        if(user == null) {
            return;
        }
        
        user.setWeixin(weixin);
        user.setUpdateTime(System.currentTimeMillis());
        UserDao.update(user);
    }
}
