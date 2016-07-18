package controllers.admin;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import models.Goods;
import models.GoodsColor;
import models.GoodsIcon;
import models.GoodsSize;
import models.GoodsType;
import play.Play;
import utils.StringUtil;
import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import dao.GoodsTypeDao;

public class GoodsCtrl extends WebController {

    public static void list(int page, String orderBy,boolean asc, String key, Integer goodsType, Integer state) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;

        String HQL = createHql(orderBy,asc, key, goodsType, state);
		Long count = Goods.count(HQL);
        List<Goods> goodses = Goods.find(HQL).fetch(page, pageSize);
        
        Pager<Goods> pageData = new Pager<Goods>(count.intValue(), page, pageSize);
        pageData.setList(goodses);
        List<GoodsType> goodsTypes = GoodsTypeDao.all();

        renderArgs.put("orderBy", orderBy);
        renderArgs.put("asc", asc);
        renderArgs.put("key", key);
        renderArgs.put("goodsType", goodsType);
        renderArgs.put("state", state);

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

    private static String createHql(String orderBy,boolean asc, String key, Integer goodsType, Integer state) {
        String HQL = "ORDER BY ";
        HQL += StringUtil.isNullOrEmpty(orderBy)?"id":orderBy;
        HQL += asc?" ASC":" DESC";
        goodsType = goodsType==null?-1:goodsType;
        state = state==null?-1:state;
        HQL = "goodsType " + (goodsType<0?" != -1":("="+goodsType)) + " " +HQL;
        HQL = "state " + (state<0?" != -1":("="+state)) + " and " +HQL;
        if (!StringUtil.isNullOrEmpty(key)) {
            HQL = "(title like '%"+key+"%' or identifier like '%" + key +"%') and " + HQL;
        }
        return HQL;
    }
}
