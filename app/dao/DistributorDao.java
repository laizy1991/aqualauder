package dao;

import models.Distributor;

public class DistributorDao {

    public static boolean insert(Distributor distributor) {
        if(distributor == null) {
            return false;
        }
        return distributor.create();
    }
    
    public static Distributor get(int userId) {
        Distributor distributor = Distributor.findById(userId);
        return distributor;
    }
    
    public static boolean update(Distributor distributor) {
        if(distributor == null || distributor.getUserId() == null) {
            return false;
        }
        
        distributor.save();
        return true;
    }

    public static void delete(Distributor distributor) {
        if (distributor != null) {
            distributor.delete();
        }
    }
}
