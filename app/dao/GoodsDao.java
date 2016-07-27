package dao;

import java.util.List;

import models.Goods;
import utils.StringUtil;

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
            sql = "from Goods g where g.state = 1 order by g.id desc";                
        } else if(type == 0) {
            sql = "from Goods g where g.state = 1 order by g.createTime desc";
        } else {
            List<Integer> ids = GoodsTypeDao.getAllSubTypeAndSelf(type);
            String idStr = "";
            String split = "";
            for(Integer id : ids) {
                idStr += id + split;
                split = ",";
            }
            if(idStr.endsWith(",")) {
                idStr = idStr.substring(0, idStr.length()-1);
            }
            
            sql = "from Goods g where g.goodsType in ( " + idStr + ") and g.state = 1 order by g.id desc";
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

    public static Goods getByIdentifier(String identifier) {
        if (StringUtil.isNullOrEmpty(identifier)) {
            return null;
        }
        List<Goods> list = Goods.find("lower(identifier) = lower(?)", identifier).fetch();

        if (list.size() > 0) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
