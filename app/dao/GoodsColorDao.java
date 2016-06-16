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

    public static void update(GoodsColor goodsColor) {
        goodsColor.save();
    }

    public static void insert(GoodsColor goodsColor) {
        goodsColor.save();
    }
    public static GoodsColor getByName(String color) {
        return GoodsColor.find("name = ?", color).first();
    }
}