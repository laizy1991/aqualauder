package controllers.front;

import net.sf.json.JSONObject;
import play.Logger;
import play.Play;
import service.OrderService;
import service.wx.WXPay;
import service.wx.common.Configure;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;
import common.core.FrontController;
import exception.BusinessException;


public class WxTest extends FrontController {

	
    public static void pay() throws BusinessException{
        String outTradeNo = OrderService.genOrderId();
        String openId = "olVhYv2ogEVJYgaRhEpIh83NZh5c";
    	UnifiedOrderReqDto orderReq = new UnifiedOrderReqDto("WEB", "Ipad mini  16G  白色", outTradeNo, 1, "192.168.0.112", "http://www.weixin.qq.com/wxpay/pay.php", "JSAPI", openId);
		UnifiedOrderRspDto rsp  = WXPay.requestUnifiedOrderService(orderReq);
		String jsRequestBody = "{'appId':'" + Configure.getAppid() + "',"; 
		jsRequestBody += "'timeStamp':'" + System.currentTimeMillis()/1000 + "',"; 
		jsRequestBody += "'nonceStr':'" + rsp.getNonce_str() + "',"; 
		jsRequestBody += "'package':'prepay_id=" + rsp.getPrepay_id() + "',"; 
		jsRequestBody += "'signType':'MD5',"; 
		jsRequestBody += "'paySign':'" + rsp.getSign() + "'}"; 
    	render("/Front/pay.html", jsRequestBody);
    }
}
