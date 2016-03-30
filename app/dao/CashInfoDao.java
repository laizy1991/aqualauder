package dao;

import java.util.List;

import models.CashInfo;

public class CashInfoDao {

    public static List<CashInfo> getByTypes(int userId, List<Integer> types) {
        return CashInfo.find("userId = ? and cashStatus in ?", userId, types).fetch();
    }
    
    public static boolean insert(CashInfo info) {
        if(info == null) {
            return false;
        }
        info.setCreateTime(System.currentTimeMillis());
        info.setUpdateTime(System.currentTimeMillis());
        return info.create();
    }
}
