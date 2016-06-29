package dao;

import java.util.List;

import models.GoodsType;

public class GoodsTypeDao {
    public static GoodsType get(long id) {
        return GoodsType.findById(id);
    }

    public static void save(GoodsType gt) {
        gt.save();
    }

    public static List<GoodsType> all() {
        return GoodsType.all().fetch();
    }
}