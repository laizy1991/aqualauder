package dao;

import java.util.List;

import models.GoodsStock;

public class GoodsStockDao {

    public static List<GoodsStock> getByGoods(long goodsId) {
        return GoodsStock.find("goodsId", goodsId).fetch();
    }

    public static boolean insert(GoodsStock goodsStock) {
        if(goodsStock == null) {
            return false;
        }
        return goodsStock.create();
    }

    public static void save(GoodsStock goodsStock) {
        if(goodsStock != null) {
            goodsStock.save();
        }
    }

    public static void delete(GoodsStock goodsStock) {
        if(goodsStock != null) {
            goodsStock.delete();
        }
    }

    public static GoodsStock get(long id) {
        return GoodsStock.findById(id);
    }

    public static GoodsStock get(long goodId, int goodsSize, int goodsColor) {
        return GoodsStock.find("goodsId = ? and goodsSize = ? and goodsColor = ?", goodId, goodsSize, goodsColor).first();
    }

}
