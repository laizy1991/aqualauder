package dao;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import models.CashInfo;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
import play.db.jpa.Model;

public class CashInfoDao {

    public static List<CashInfo> getByStatus(int userId, List<Integer> statusList) {
        if(statusList == null || statusList.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        String statusArr = "";
        String split = "";
        for(Integer status : statusList) {
            statusArr = statusArr + split + status;
            split = ",";
        }
        String sql = "SELECT * from cash_info where user_id=%s and cash_status in (%s);";  
        Query query = Model.em().createNativeQuery(String.format(sql, userId, statusArr),CashInfo.class);  
        return query.getResultList();  
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
    public static void update(CashInfo info) {
        if(info == null) {
            return;
        }
        
        info.save();
    }
}
