package service;

import java.util.List;

import models.Goods;
import models.GoodsStock;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;
import dao.GoodsDao;
import dao.GoodsStockDao;
import dto.GoodsDetail;

public class GoodsService {

    public static Goods get(long id) {
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
    
    /**
     * 获取商品列表
     * @param page
     * @param pageSize
     * @param type
     * @return
     */
    public static List<Goods> list(int page, int pageSize, int type) {
        return GoodsDao.getBy(type, page, pageSize);
    }
    
    public static GoodsDetail getDetail(long id) {
        Goods goods = get(id);
        if(goods == null) {
            Logger.error("goods not found, id:%s", id);
            return null;
        }
        
        GoodsDetail detail = new GoodsDetail(goods);
        
        List<GoodsStock> stocks = GoodsStockDao.getByGoods(id);
        if(CollectionUtils.isEmpty(stocks)) {
            return detail;
        }
        
        for(GoodsStock stock : stocks) {
            detail.addStock(stock);
        }
        
        return detail;
    }
}
