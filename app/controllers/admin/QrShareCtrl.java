package controllers.admin;

import java.util.List;

import models.QrShare;

import common.core.WebController;

public class QrShareCtrl extends WebController {

	public static void list() {
        List<QrShare> qrShares = QrShare.all().fetch();
        render("/admin/QrShare/list.html", qrShares);
    }
}
