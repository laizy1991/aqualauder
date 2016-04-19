package service;

import dao.GoodsColorDao;
import models.GoodsColor;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class GoodsColorService {
        public static GoodsColor get(long id) {
            return GoodsColorDao.get(id);
        }

        public static void add(GoodsColor goodsSize) {
            GoodsColorDao.insert(goodsSize);
        }

//        public static void delete(GoodsColor goodsSize) {
//            GoodsColorDao.delete(goodsSize);
//        }

        public static void update(GoodsColor goodsSize) {
            GoodsColorDao.update(goodsSize);
        }

    }
