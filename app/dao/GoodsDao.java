package dao;

import models.Goods;
import org.apache.commons.collections.CollectionUtils;
import play.Logger;

import java.util.List;

public class GoodsDao {

    public static boolean insert(Goods goods) {
        if(goods == null) {
            return false;
        }
        return goods.create();
    }
    
    public static Goods get(int id) {
        List<Goods> list = Goods.find("goods_Id", id).fetch();
        if(CollectionUtils.isEmpty(list)) {
            return null;
        }
        
        if(list.size() > 1) {
            Logger.error("too many goods find. goods_Id:" + id);
            return null;
        }
        
        return list.get(0);
    }
    
    public static void update(Goods goods) {
        if(goods == null || goods.getId() == 0) {
            return;
        }
        
        goods.save();
    }

    public static void delete(Goods goods) {
        if (goods != null) {
            goods.delete();
        }
    }
}
