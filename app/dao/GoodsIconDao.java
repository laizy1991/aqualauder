package dao;

import java.util.List;

import models.GoodsIcon;
import utils.StringUtil;

/**
 * 
 * @author laizy1991@gmail.com
 * @createDate 2016年4月10日
 *
 */
public class GoodsIconDao {

   public static List<GoodsIcon> getByGoods(long goodsId) {
       return GoodsIcon.find("goodsId", goodsId).fetch();
   }

    public static boolean insert(GoodsIcon goodsIcon) {
        if(goodsIcon == null) {
            return false;
        }
        return goodsIcon.create();
    }

}
