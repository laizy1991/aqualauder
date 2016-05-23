package dao;

import java.util.List;

import models.Goods;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;

public class GoodsDao {

    public static boolean insert(Goods goods) {
        if(goods == null) {
            return false;
        }
        return goods.create();
    }
    
    public static Goods get(long id) {
        return Goods.findById(id);
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
    
    public static List<Goods> getBy(int type, int page, int size)  {
        if(page == -1 && size == -1) {
            return Goods.find("goods_type = ? and state = 1", type).fetch();
        } else {
            return Goods.find("goods_type = ? and state = 1", type).fetch(page, size);
        }
    }
}
