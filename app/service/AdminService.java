package service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import models.Administrator;
import play.Play;
import utils.EncryptionUtil;
import utils.IdGenerator;
import dao.AdminDao;
import dto.SessionInfo;
import exception.BusinessException;
import utils.StringUtil;

public class AdminService {
    //session信息目前放内存，之后保存到缓存中
    private static Map<String, SessionInfo> sessions = new ConcurrentHashMap<String, SessionInfo>();
    //加密盐
    private static final String SLAT = Play.configuration.getProperty("passwordSlat", "passwordSlat2016");


    public static SessionInfo getSessionInfo(String sessionId) {
        if(sessionId == null) {
            return null;
        }
        return sessions.get(sessionId);
    }
    

    public static SessionInfo login(String username, String password) throws BusinessException {
        Administrator admin = AdminDao.getByName(username);
        if (admin == null || new Integer(1).equals(admin.getDeleted())) {
            throw new BusinessException("User not exists");
        }
        
        String md5 = EncryptionUtil.md5(password + SLAT);
        if (!admin.getPassword().equals(md5)) {
            throw new BusinessException("Password incorrect");
        }
        
        SessionInfo sessionInfo = new SessionInfo();
        sessionInfo.setSessionId(IdGenerator.getUUID());
        sessionInfo.setAdmin(admin);
    
        //暂时放内存，之后放缓存
        sessions.put(sessionInfo.getSessionId(), sessionInfo);
        return sessionInfo;
    }

    public static void add(Administrator admin) {
        admin.setCreateTime(System.currentTimeMillis());
        admin.setUpdateTime(System.currentTimeMillis());
        admin.setPassword(EncryptionUtil.md5(admin.getPassword() + SLAT));
        AdminDao.insert(admin);
    }

    public static void delete(Administrator admin) {
        admin.setUpdateTime(System.currentTimeMillis());
        admin.setDeleted(1);
        AdminDao.update(admin);
    }

    public static void update(Administrator admin) {
        admin.setUpdateTime(System.currentTimeMillis());
        if (!StringUtil.isNullOrEmpty(admin.getPassword())) {
            admin.setPassword(EncryptionUtil.md5(admin.getPassword() + SLAT));
        }
        AdminDao.update(admin);
    }

    public static Administrator getByName(String username) {
        return AdminDao.getByName(username);
    }

    public static void logout(String sessionId) {
        sessions.remove(sessionId);
    }
}
