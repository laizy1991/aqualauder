package service;

import common.constants.Separator;
import dao.GoodsDao;
import dao.GoodsIconDao;
import dao.GoodsStockDao;
import dto.GoodsDetail;
import models.Goods;
import models.GoodsIcon;
import models.GoodsStock;
import org.apache.commons.collections.CollectionUtils;
import play.Logger;

import java.util.List;

public class GoodsStockService {

    public static void add(List<GoodsStock> goodsStock) {
        long now = System.currentTimeMillis();

        if (goodsStock != null && goodsStock.size() != 0) {
            for (GoodsStock gs : goodsStock) {
                if(gs != null) {
                    gs.setCreateTime(now);
                    gs.setUpdateTime(now);
                    GoodsStockDao.insert(gs);
                }
            }
        }

    }


    public static void update(List<GoodsStock> goodsStock) {
        if (goodsStock != null && goodsStock.size() != 0) {
            for (GoodsStock gs : goodsStock) {
                if(gs != null) {
                    gs.setUpdateTime(System.currentTimeMillis());
                    GoodsStockDao.save(gs);
                }
            }
        }
    }


    public static void delete(List<GoodsStock> goodsStock) {
        if (goodsStock != null && goodsStock.size() != 0) {
            for (GoodsStock gs : goodsStock) {
                if(gs != null) {
                    GoodsStockDao.delete(gs);
                }
            }
        }
    }
}
