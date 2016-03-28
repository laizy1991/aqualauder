package dao;

import java.util.List;

import models.CashInfo;

public class CashInfoDao {

    public static List<CashInfo> getByTypes(int userId, List<Integer> types) {
        return CashInfo.find("userId = ? and cashStatus in ?", userId, types).fetch();
    }
    
}
