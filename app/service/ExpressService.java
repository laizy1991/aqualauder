package service;

import dao.ExpressDao;
import models.Express;

public class ExpressService {

    public static Express get(int id) {
        return ExpressDao.get(id);
    }

    public static void add(Express express) {
        express.setCreateTime(System.currentTimeMillis());
        express.setUpdateTime(System.currentTimeMillis());
        ExpressDao.insert(express);
    }

    public static void delete(Express express) {
        ExpressDao.delete(express);
    }

    public static void update(Express express) {
        express.setUpdateTime(System.currentTimeMillis());
        ExpressDao.update(express);
    }
}
