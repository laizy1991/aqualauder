package service;

import dao.GoodsDao;
import models.Goods;

public class GoodsService {

    public static Goods get(int id) {
        return GoodsDao.get(id);
    }

    public static void add(Goods goods) {
        goods.setCreateTime(System.currentTimeMillis());
        goods.setUpdateTime(System.currentTimeMillis());
        GoodsDao.insert(goods);
    }

    public static void delete(Goods goods) {
        GoodsDao.delete(goods);
    }

    public static void update(Goods goods) {
        goods.setUpdateTime(System.currentTimeMillis());
        GoodsDao.update(goods);
    }
}
