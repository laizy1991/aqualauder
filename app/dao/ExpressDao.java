package dao;

import models.Express;
import org.apache.commons.collections.CollectionUtils;
import play.Logger;

import java.util.List;

public class ExpressDao {

    public static boolean insert(Express express) {
        if(express == null) {
            return false;
        }
        return express.create();
    }
    
    public static Express get(int id) {
        List<Express> list = Express.find("id", id).fetch();
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        if(list.size() > 1) {
            Logger.error("too many express find. express_Id:" + id);
            return null;
        }
        
        return list.get(0);
    }
    
    public static void update(Express express) {
        if(express == null || express.getId() == 0) {
            return;
        }
        
        express.save();
    }

    public static void delete(Express express) {
        if (express != null) {
            express.delete();
        }
    }
}
