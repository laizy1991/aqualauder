package dao;

import java.util.List;

import javax.persistence.Query;

import models.QrShare;
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
    
    public static int countIsEnabledRecs(int isEnabled) {
    	return new Long(QrShare.count("is_enabled = ?", isEnabled)).intValue();
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
}