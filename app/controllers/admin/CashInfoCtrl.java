package controllers.admin;

import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import models.CashInfo;
import utils.StringUtil;

import java.util.List;

public class CashInfoCtrl extends WebController {

    public static void list(int page, String orderBy,boolean asc, String key, Integer state, Integer cashType) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
        String HQL = createHql(orderBy,asc, key, state, cashType);
		Long count = CashInfo.count(HQL);
        List<CashInfo> refundOrders = CashInfo.find(HQL).fetch(page, pageSize);
        
        Pager<CashInfo> pageData = new Pager<CashInfo>(count.intValue(), page, pageSize);
        pageData.setList(refundOrders);
        renderArgs.put("orderBy", orderBy);
        renderArgs.put("asc", asc);
        renderArgs.put("key", key);
        renderArgs.put("state", state);
        renderArgs.put("cashType", cashType);
        render("/admin/CashInfo/list.html", pageData);
    }

    private static String createHql(String orderBy,boolean asc, String key, Integer state, Integer cashType) {
        String HQL = "ORDER BY ";
        HQL += StringUtil.isNullOrEmpty(orderBy)?"id":orderBy;
        HQL += asc?" ASC":" DESC";
        state = state==null?-1:state;
        cashType = cashType==null?-1:cashType;
        HQL = "cash_status " + (state<0?" != -1":("="+state)) + " " +HQL;
        HQL = "cash_type " + (cashType<0?" != -1":("="+cashType)) + " and " +HQL;
        if (!StringUtil.isNullOrEmpty(key)) {
            HQL = "(mch_billno like '%"+key+"%' or weixin like '%"+key+"%' or slip_no like '%"+key+"%') and " + HQL;
        }
        return HQL;
    }
    
}
