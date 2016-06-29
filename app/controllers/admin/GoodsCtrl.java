package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import dao.GoodsTypeDao;
import models.Goods;
import models.GoodsColor;
import models.GoodsSize;
import models.GoodsType;

import java.util.List;

public class GoodsCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		Long count = Goods.count();
        List<Goods> goodses = Goods.find("ORDER BY id DESC").fetch(page, pageSize);
        
        Pager<Goods> pageData = new Pager<Goods>(count.intValue(), page, pageSize);
        pageData.setList(goodses);
        List<GoodsType> goodsTypes = GoodsTypeDao.all();

        render("/admin/Goods/list.html", pageData, goodsTypes);
    }

    public static void add() {
        List<GoodsColor> goodsColors = GoodsColor.all().fetch();
        List<GoodsSize> goodsSizes = GoodsSize.all().fetch();
        List<GoodsType> goodsTypes = GoodsTypeDao.all();
        render("/admin/Goods/add.html", goodsColors, goodsSizes, goodsTypes);
    }

    public static void update(Goods goods) {
        List<GoodsColor> goodsColors = GoodsColor.all().fetch();
        List<GoodsSize> goodsSizes = GoodsSize.all().fetch();
        List<GoodsType> goodsTypes = GoodsTypeDao.all();
        render("/admin/Goods/update.html", goods, goodsColors, goodsSizes, goodsTypes);
    }
}
