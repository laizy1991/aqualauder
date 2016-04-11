package controllers.front;

import java.util.HashMap;
import java.util.Map;

import service.OrderService;
import service.wx.WXPay;
import service.wx.common.Configure;
import service.wx.common.RandomStringGenerator;
import service.wx.common.Signature;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;
import common.core.FrontController;
import exception.BusinessException;


public class WxTest extends FrontController {

	
    public static void pay() throws BusinessException{
        String outTradeNo = OrderService.genOrderId();
        String openId = "olVhYv2ogEVJYgaRhEpIh83NZh5c";
    	UnifiedOrderReqDto orderReq = new UnifiedOrderReqDto("WEB", "Ipad mini  16G  白色", outTradeNo, 1, "192.168.0.112", "http://www.weixin.qq.com/wxpay/pay.php", "JSAPI", openId);
		
		
    	render("/Front/pay.html", jsRequestBody);
    }
}
