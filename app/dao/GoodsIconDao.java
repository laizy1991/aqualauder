package dao;

import java.util.List;

import models.GoodsIcon;

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
    
}
