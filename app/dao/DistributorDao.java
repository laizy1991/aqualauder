package dao;

import models.Distributor;
import org.apache.commons.collections.CollectionUtils;
import play.Logger;

import java.util.List;

public class DistributorDao {

    public static boolean insert(Distributor distributor) {
        if(distributor == null) {
            return false;
        }
        return distributor.create();
    }
    
    public static Distributor get(int id) {
        List<Distributor> list = Distributor.find("user_id", id).fetch();
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        if(list.size() > 1) {
            Logger.error("too many distributor find user_id:" + id);
            return null;
        }
        
        return list.get(0);
    }
    
    public static void update(Distributor distributor) {
        if(distributor == null || distributor.getUserId() == null) {
            return;
        }
        
        distributor.save();
    }

    public static void delete(Distributor distributor) {
        if (distributor != null) {
            distributor.delete();
        }
    }
}
