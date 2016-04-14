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
    
    public static AuditInfo get(long id) {
        return AuditInfo.findById(id);
    }
    
    public static List<AuditInfo> getByStatus(int status) {
        return AuditInfo.find("audit_status", status).fetch();
    }
    
    public static boolean update(AuditInfo info) {
        if(info == null) {
            return false;
        }
        info.setUpdateTime(System.currentTimeMillis());
        return info.save() != null;
    }
}
