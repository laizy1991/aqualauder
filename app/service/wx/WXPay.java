package service.wx;

import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;
import service.wx.service.QueryRedpackService;
import service.wx.service.SendRedpackService;
import service.wx.service.UnifiedOrderService;
import exception.BusinessException;

public class WXPay {

    /**
     * 统一下单
     * @param unifiedOrderReqData
     * @throws Exception
     */
    public static UnifiedOrderRspDto requestUnifiedOrderService(UnifiedOrderReqDto unifiedOrderReqData) throws BusinessException {
    	return new UnifiedOrderService().request(unifiedOrderReqData);
    }
    
    public static String sendRedpackService(SendRedpackReqDto sendRedpackReqDto) throws Exception {
    	return new SendRedpackService().request(sendRedpackReqDto);
    }
    
    public static String queryRedpackStatusService(QueryRedpackReqDto queryRedpackReqDto) throws Exception {
    	return new QueryRedpackService().request(queryRedpackReqDto);
    }


}
