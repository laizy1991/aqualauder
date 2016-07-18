package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.Express;
import models.GoodsColor;
import models.GoodsIcon;
import models.GoodsSize;
import models.Order;
import models.OrderGoods;
import net.sf.json.JSON;
import net.sf.json.util.JSONUtils;
import utils.StringUtil;

import java.util.List;

import org.apache.commons.lang.StringUtils;

public class OrderCtrl extends WebController {

    public static void list(int page, String orderBy,boolean asc, String key, Integer state) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
        String HQL = createHql(orderBy,asc, key, state);

        Long count = Order.count(HQL);
        List<Order> orders = Order.find(HQL).fetch(page, pageSize);
        Pager<Order> pageData = new Pager<Order>(count.intValue(), page, pageSize);
        pageData.setList(orders);
        
        List<Express> expresses = Express.all().fetch();
        List<GoodsColor> colors = GoodsColor.all().fetch();
        List<GoodsSize> sizes = GoodsSize.all().fetch();
        renderArgs.put("expresses", expresses);
        renderArgs.put("colors", colors);
        renderArgs.put("sizes", sizes);
        renderArgs.put("orderBy", orderBy);
        renderArgs.put("asc", asc);
        renderArgs.put("key", key);
        renderArgs.put("state", state);

        render("/admin/Order/list.html", pageData);
    }

    private static String createHql(String orderBy,boolean asc, String key, Integer state) {
        String HQL = "ORDER BY ";
        HQL += StringUtil.isNullOrEmpty(orderBy)?"id":orderBy;
        HQL += asc?" ASC":" DESC";
        state = state==null?-1:state;
        HQL = "state " + (state<0?" != -1":("="+state)) + " " +HQL;
        if (!StringUtil.isNullOrEmpty(key)) {
            HQL = "(outTradeNo like '%"+key+"%' or weixin like '%"+key+"%' or goodsTitle like '%"+key+"%' or identifier like '%" + key +"%') and " + HQL;
        }
        return HQL;
    }
    
}
