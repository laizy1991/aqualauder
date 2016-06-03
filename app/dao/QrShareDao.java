package dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import models.QrShare;

import org.apache.commons.lang.StringUtils;

import play.db.jpa.JPA;
import play.db.jpa.Model;

public class QrShareDao {
    public static QrShare get(long id) {
        return QrShare.findById(id);
    }

    public static boolean update(QrShare qrShare) {
    	if(null == qrShare)
    		return false;
    	
    	qrShare.save();
    	return true;
    }

    public static void insert(QrShare qrShare) {
    	qrShare.save();
    }
    
    public static void delete(QrShare qrShare) {
    	qrShare.delete();
    }
    
    public static int countIsEnabledRecsButId(long id, int isEnabled) {
    	return new Long(QrShare.count("is_enabled = ? and id != ?", isEnabled, id)).intValue();
    }
    
    public static QrShare getLastIsEnabledRec(int isEnabled) {
    	String sql = "SELECT * FROM `qrcode_share` where is_enabled=%d ORDER BY id DESC LIMIT 1; ";
    	Query query = Model.em().createNativeQuery(String.format(sql, isEnabled), QrShare.class);
    	List<QrShare> list = query.getResultList();  
    	if(null == list || list.size() <= 0) {
    		return null;
    	}
    	return list.get(0);  
    }
    
    /**
     * 这里的whereSql中开头不含AND
     * @param whereSql
     * @return
     */
    public static List<QrShare> listQrShare(String whereSql) {
    	String sql = "SELECT * FROM `qrcode_share` WHERE 1=1 ";
    	if(!StringUtils.isBlank(whereSql)) {
    		sql += whereSql;
    	}
    	Query query = Model.em().createNativeQuery(sql, QrShare.class);
    	List<QrShare> list = query.getResultList();  
    	if(null == list || list.size() <= 0) {
    		return new ArrayList<QrShare>();
    	}
    	return list; 
    }
    
    public static int countQrShare() {
    	return new Long(QrShare.count()).intValue();
    }
}