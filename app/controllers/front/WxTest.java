package controllers.front;

import utils.WxUtil;
import common.core.FrontController;

public class WxTest extends FrontController {
	public static void get() {
		System.out.println(WxUtil.getAccessToken());
	}
}
