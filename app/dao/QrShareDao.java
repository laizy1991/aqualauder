package dao;

import models.QrShare;

public class QrShareDao {
    public static QrShare get(long id) {
        return QrShare.findById(id);
    }

    public static void update(QrShare qrShare) {
    	qrShare.save();
    }

    public static void insert(QrShare qrShare) {
    	qrShare.save();
    }
    
    public static void delete(QrShare qrShare) {
    	qrShare.delete();
    }
}