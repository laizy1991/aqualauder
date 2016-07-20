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
import utils.StringUtil;

import java.util.List;

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
                if (gs != null) {
                    gs.setGoodsId(goods.getId());
                    gs.setCreateTime(now);
                    gs.setUpdateTime(now);
                    GoodsStockDao.insert(gs);
                }
            }
        }

        if (saved && goodsIcon != null && goodsIcon.size() != 0) {
            for (GoodsIcon gi : goodsIcon) {
                if (gi != null) {
                    gi.setGoodsId(goods.getId());
                    GoodsIconDao.insert(gi);
                }
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
        return icons.get(0).getIconUrl();
        
//        StringBuilder sb = new StringBuilder();
//        String sep = "";
//        for(GoodsIcon icon : icons) {
//            sb.append(sep);
//            sb.append(icon.getIconUrl());
//            sep = Separator.COMMON_SEPERATOR_COMME;
//        }
//
//        return sb.toString();
    }

    public static boolean deleteIcon(GoodsIcon goodsIcon) {
        try {
            GoodsIconDao.delete(goodsIcon);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean updateIcon(GoodsIcon goodsIcon) {
        try {
            GoodsIconDao.update(goodsIcon);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean addGoodsIcon(GoodsIcon goodsIcon) {
        try {
            return GoodsIconDao.insert(goodsIcon);
        } catch (Exception e) {
            return false;
        }
    }

    public static Goods getByIdentifier(String identifier) {
        if (StringUtil.isNullOrEmpty(identifier)) {
            return null;
        }

        try {
            return GoodsDao.getByIdentifier(identifier);
        }catch (Exception e) {
            return null;
        }
    }
}
