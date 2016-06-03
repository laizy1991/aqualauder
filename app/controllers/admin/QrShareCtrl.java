package controllers.admin;

import java.util.List;

import service.QrShareService;
import models.QrShare;
import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;

public class QrShareCtrl extends WebController {

	public static void list(int page) {
		if(0 == page) {
			page = 1;
		}
		int pageSize = GlobalConstants.DEFAULT_PAGE_SIZE;
		
		String whereSql = " ORDER BY id DESC LIMIT %d,%d";
		int start = (page-1)*pageSize;
		int count = QrShareService.countQrShare();
        List<QrShare> qrShares = QrShareService.listQrShare(String.format(whereSql, start, pageSize));
        
        Pager<QrShare> pageData = new Pager<QrShare>(count, page, pageSize);;
        pageData.setList(qrShares);
        
        render("/admin/QrShare/list.html", pageData);
    }
}
