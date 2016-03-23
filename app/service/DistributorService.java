package service;

import dao.DistributorDao;
import models.Distributor;

public class DistributorService {

    public static Distributor get(int id) {
        return DistributorDao.get(id);
    }

    public static void add(Distributor distributor) {
        distributor.setCreateTime(System.currentTimeMillis());
        distributor.setUpdateTime(System.currentTimeMillis());
        DistributorDao.insert(distributor);
    }

    public static void delete(Distributor distributor) {
        DistributorDao.delete(distributor);
    }

    public static void update(Distributor distributor) {
        distributor.setUpdateTime(System.currentTimeMillis());
        DistributorDao.update(distributor);
    }
}
