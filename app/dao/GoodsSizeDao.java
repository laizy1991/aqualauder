package dao;

import models.GoodsSize;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class GoodsSizeDao {
    public static GoodsSize get(long id) {
        return GoodsSize.findById(id);
    }

    public static void update(GoodsSize goodsSize) {
        goodsSize.save();
    }

    public static void insert(GoodsSize goodsSize) {
        goodsSize.save();
    }
}