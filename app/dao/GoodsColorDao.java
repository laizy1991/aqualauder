package dao;

import models.GoodsColor;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class GoodsColorDao {
    public static GoodsColor get(long id) {
        return GoodsColor.findById(id);
    }

    public static void update(GoodsColor goodsSize) {
        goodsSize.save();
    }

    public static void insert(GoodsColor goodsSize) {
        goodsSize.save();
    }
}