package service;

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

        public static void update(QrShare qrShare) {
        	QrShareDao.update(qrShare);
        }

    }
