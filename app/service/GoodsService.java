package service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Goods;
import models.GoodsIcon;
import models.GoodsStock;

import org.apache.commons.collections.CollectionUtils;

import play.Logger;

import common.constants.Separator;

import dao.GoodsDao;
import dao.GoodsIconDao;
import dao.GoodsStockDao;
import dto.GoodsDetail;

public class GoodsService {

    public static Goods get(long id) {
        return GoodsDao.get(id);
    }

    public static void add(Goods goods, List<GoodsStock> goodsStock, List<GoodsIcon> goodsIcon) {
        long now = System.currentTimeMillis();
        goods.setCreateTime(now);
        goods.setUpdateTime(now);
        boolean saved = GoodsDao.insert(goods);

        if (saved && goodsStock != null && goodsStock.size() != 0) {
            for (GoodsStock gs : goodsStock) {
                gs.setGoodsId(goods.getId());
                gs.setCreateTime(now);
                gs.setUpdateTime(now);
                GoodsStockDao.insert(gs);
            }
        }

        if (saved && goodsIcon != null && goodsIcon.size() != 0) {
            for (GoodsIcon gi : goodsIcon) {
                gi.setGoodsId(goods.getId());
                GoodsIconDao.insert(gi);
            }
        }
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
    
    public static String getIcon(long goodsId) {
        List<GoodsIcon> icons = GoodsIconDao.getByGoods(goodsId);
        if(CollectionUtils.isEmpty(icons)) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        String sep = "";
        for(GoodsIcon icon : icons) {
            sb.append(icon.getIconUrl());
            sb.append(sep);
            sep = Separator.COMMON_SEPERATOR_COMME;
        }
        
        return sb.toString();
    }
}
