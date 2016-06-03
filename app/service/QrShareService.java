package service;

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

    }
