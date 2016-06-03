package service;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import common.constants.GlobalConstants;
import models.QrShare;
import dao.QrShareDao;

public class QrShareService {
        public static QrShare get(long id) {
            return QrShareDao.get(id);
        }

        public static void add(QrShare qrShare) {
        	QrShareDao.insert(qrShare);
        }

        public static void delete(QrShare qrShare) {
        	QrShareDao.delete(qrShare);
        }

        public static boolean update(QrShare qrShare) {
        	if(null == qrShare)
        		return false;
        	
        	return QrShareDao.update(qrShare);
        }
        
        public static int countIsEnabledRecsButId(long id, int isEnabled) {
        	
        	return QrShareDao.countIsEnabledRecsButId(id, isEnabled);
        }
        
        public static QrShare getLastIsEnabledRec(int isEnabled) {
        	
        	return QrShareDao.getLastIsEnabledRec(isEnabled);
        }
        
        public static List<QrShare> listQrShare(String whereSql) {
        	String sql = "";
        	if(!StringUtils.isBlank(whereSql)) {
        		sql += whereSql; 
        	}
        	
        	return QrShareDao.listQrShare(sql);
        }
        
        public static int countQrShare() {
        	return QrShareDao.countQrShare();
        }

    }
