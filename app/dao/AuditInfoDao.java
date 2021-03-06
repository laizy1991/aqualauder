package dao;

import java.util.List;

import models.AuditInfo;

public class AuditInfoDao {

    public static boolean insert(AuditInfo info) {
        if(info == null) {
            return false;
        }
        info.setCreateTime(System.currentTimeMillis());
        info.setUpdateTime(System.currentTimeMillis());
        return info.create();
    }
    
    public static boolean isExist(int userId, int type) {
        return AuditInfo.count("userId=? and auditType=? and auditStatus!=-1", userId, type) > 0;
    }
    
    public static AuditInfo get(long id) {
        return AuditInfo.findById(id);
    }
    
    public static List<AuditInfo> getByStatus(int status) {
        return AuditInfo.find("auditStatus", status).fetch();
    }
    
    public static boolean update(AuditInfo info) {
        if(info == null) {
            return false;
        }
        info.setUpdateTime(System.currentTimeMillis());
        return info.save() != null;
    }
}
