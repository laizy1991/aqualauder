package service;

import dao.GoodsSizeDao;
import models.GoodsSize;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class GoodsSizeService {
        public static GoodsSize get(long id) {
            return GoodsSizeDao.get(id);
        }

        public static void add(GoodsSize goodsSize) {
            GoodsSizeDao.insert(goodsSize);
        }

//        public static void delete(GoodsSize goodsSize) {
//            GoodsSizeDao.delete(goodsSize);
//        }

        public static void update(GoodsSize goodsSize) {
            GoodsSizeDao.update(goodsSize);
        }

    }
