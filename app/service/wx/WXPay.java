package service.wx;

import service.wx.dto.orderQuery.OrderQueryReqDto;
import service.wx.dto.orderQuery.OrderQueryRspDto;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.redpack.SendRedpackRspDto;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;
import service.wx.service.OrderQueryService;
import service.wx.service.QueryRedpackService;
import service.wx.service.SendRedpackService;
import service.wx.service.UnifiedOrderService;
import exception.BusinessException;

public class WXPay {

    //统一下单
    public static UnifiedOrderRspDto requestUnifiedOrderService(UnifiedOrderReqDto unifiedOrderReqData) throws BusinessException {
    	return new UnifiedOrderService().request(unifiedOrderReqData);
    }
    
    //查询订单
    public static OrderQueryRspDto requestOrderQueryService(OrderQueryReqDto orderQueryReqDto) throws BusinessException {
    	return new OrderQueryService().request(orderQueryReqDto);
    }
    
    //发送红包
    public static SendRedpackRspDto sendRedpackService(SendRedpackReqDto sendRedpackReqDto) throws Exception {
    	return new SendRedpackService().request(sendRedpackReqDto);
    }
    
    //查询红包
    public static String queryRedpackStatusService(QueryRedpackReqDto queryRedpackReqDto) throws Exception {
    	return new QueryRedpackService().request(queryRedpackReqDto);
    }


}
