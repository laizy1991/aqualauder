package service;

import java.util.List;

import play.Logger;
import models.GoodsColor;
import models.GoodsSize;
import models.GoodsStock;
import utils.DistributeCacheLock;
import dao.GoodsColorDao;
import dao.GoodsSizeDao;
import dao.GoodsStockDao;

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
        long now = System.currentTimeMillis();
        if (goodsStock != null && goodsStock.size() != 0) {
            for (GoodsStock gs : goodsStock) {
                if(gs != null) {
                    GoodsStock gss = GoodsStockDao.get(gs.getId());
                    gss.setUpdateTime(now);
                    gss.setAmount(gs.getAmount());
                    gss.setStockDesc(gs.getStockDesc());
                    gss.setGoodsColor(gs.getGoodsColor());
                    gss.setGoodsSize(gs.getGoodsSize());
                    GoodsStockDao.save(gss);
                }
            }
        }
    }


    public static void delete(List<GoodsStock> goodsStock) {
        if (goodsStock != null && goodsStock.size() != 0) {
            for (GoodsStock gs : goodsStock) {
                if(gs != null) {
                    GoodsStock gss = GoodsStockDao.get(gs.getId());
                    GoodsStockDao.delete(gss);
                }
            }
        }
    }
    
    /**
     * 
     * @param goodsId
     * @param size
     * @param color
     * @param num   负数加库存，正数减库存
     * @return
     */
    public static boolean reduced(long goodsId, String size, String color, int num) {
        GoodsSize gs = GoodsSizeDao.getByName(size);
        GoodsColor gc = GoodsColorDao.getByName(color);
        if(gs == null || gc == null) {
            return false;
        }
        //加锁
        final DistributeCacheLock lockMgr = DistributeCacheLock.getInstance();
        final String lockKey = getLockKey(goodsId, size, color);
        
        if(lockMgr.isLocked(lockKey)) {
            return false;
        }
        if(!lockMgr.tryLock(lockKey)) {
            return false;
        }
        
        try {
            GoodsStock goodsStock = GoodsStockDao.get(goodsId, gs.getId().intValue(), gc.getId().intValue());
            if(goodsStock == null || goodsStock.getAmount() < num) {
                return false;
            }
            goodsStock.setAmount(goodsStock.getAmount() - num);
            goodsStock.setUpdateTime(System.currentTimeMillis());
            GoodsStockDao.save(goodsStock);
            return true;
        } catch(Exception e) {
            Logger.error(e, "");
        } finally {
            lockMgr.unLock(lockKey);
        }
        return false;
    }


    private static String getLockKey(long goodsId, String size, String color) {
        return "g_s_" + goodsId + "_" + size + "_" + color;
    }
}
