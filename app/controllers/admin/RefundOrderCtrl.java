package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.RefundOrder;
import utils.StringUtil;

import java.util.List;

public class RefundOrderCtrl extends WebController {

    public static void list(int page, String orderBy,boolean asc, String key, Integer state) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
        String HQL = createHql(orderBy,asc, key, state);
		Long count = RefundOrder.count(HQL);
        List<RefundOrder> refundOrders = RefundOrder.find(HQL).fetch(page, pageSize);
        
        Pager<RefundOrder> pageData = new Pager<RefundOrder>(count.intValue(), page, pageSize);
        pageData.setList(refundOrders);
        renderArgs.put("orderBy", orderBy);
        renderArgs.put("asc", asc);
        renderArgs.put("key", key);
        renderArgs.put("state", state);
        render("/admin/RefundOrder/list.html", pageData);
    }

    private static String createHql(String orderBy,boolean asc, String key, Integer state) {
        String HQL = "ORDER BY ";
        HQL += StringUtil.isNullOrEmpty(orderBy)?"id":orderBy;
        HQL += asc?" ASC":" DESC";
        state = state==null?-2:state;
        HQL = "refund_state " + (state<-1?" != -2":("="+state)) + " " +HQL;
        if (!StringUtil.isNullOrEmpty(key)) {
            HQL = "(outTradeNo like '%"+key+"%' or weixin like '%"+key+"%' or goodsTitle like '%"+key+"%') and " + HQL;
        }
        return HQL;
    }
    
}
