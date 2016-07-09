package dao;

import java.util.List;

import models.Goods;

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
        String sql = "";
        if(type < 0) {
            sql = "state = 1 and id != 1 order by id desc";                
        } else if(type == 0) {
            sql = "state = 1 and id != 1 order by create_time desc";
        } else {
            List<Integer> ids = GoodsTypeDao.getAllSubType(type);
            String idStr = "";
            String split = "";
            for(Integer id : ids) {
                idStr += id + split;
                split = ",";
            }
            if(idStr.endsWith(",")) {
                idStr = idStr.substring(0, idStr.length()-1);
            }
            
            sql = "goods_type in ( " + idStr + ") and state = 1 and id != 1 order by id desc";
        }
        if(page == -1 && size == -1) {
            if(type <= 0) {
                return Goods.find(sql).fetch();                
            } else {
                return Goods.find(sql).fetch();
            }
        } else {
            if(type <= 0) {
                return Goods.find(sql).fetch(page, size);
            } else {
                return Goods.find(sql).fetch(page, size);
            }
        }
    }
}
