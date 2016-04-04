package dao;

import java.util.List;

import models.GoodsStock;

public class GoodsStockDao {

    public static List<GoodsStock> getByGoods(long goodsId) {
        return GoodsStock.find("goodsId", goodsId).fetch();
    }
    
}
