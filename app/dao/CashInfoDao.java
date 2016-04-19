package dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
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
    
    public static CashInfo get(long id) {
    	if(id <= 0)
    		return null;
    	
    	List<CashInfo> list = CashInfo.find("id", id).fetch();
    	if(CollectionUtils.isEmpty(list)) {
            return null;
        }
    	
    	if(list.size() > 1) {
            Logger.error("too many CashInfo find. id:" + id);
        }
    	
    	return list.get(0);
    }
}
