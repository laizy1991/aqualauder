package controllers.admin;

import common.core.WebController;
import models.Express;
import models.Order;
import net.sf.json.JSON;
import net.sf.json.util.JSONUtils;

import java.util.List;

public class OrderCtrl extends WebController {

    public static void list() {
        List<Order> orders = Order.all().fetch();
        List<Express> expresses = Express.all().fetch();

        renderArgs.put("expresses",expresses);
        render("/admin/Order/list.html", orders);
    }
    
}
