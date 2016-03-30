package service.wx;

import service.wx.dto.UnifiedOrderReqDto;
import service.wx.service.UnifiedOrderService;

public class WXPay {

    /**
     * 统一下单
     * @param unifiedOrderReqData
     * @throws Exception
     */
    public static String requestUnifiedOrderService(UnifiedOrderReqDto unifiedOrderReqData) throws Exception {
    	return new UnifiedOrderService().request(unifiedOrderReqData);
    }


}
