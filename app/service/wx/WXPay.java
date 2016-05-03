package service.wx;

import service.wx.dto.order.OrderQueryReqDto;
import service.wx.dto.order.OrderQueryRspDto;
import service.wx.dto.order.UnifiedOrderReqDto;
import service.wx.dto.order.UnifiedOrderRspDto;
import service.wx.dto.qrcode.CreateQrCodeRspDto;
import service.wx.dto.qrcode.limit.CreateLimitQrCodeReqDto;
import service.wx.dto.qrcode.tmp.CreateTmpQrCodeReqDto;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.redpack.SendRedpackRspDto;
import service.wx.dto.refund.QueryRefundReqDto;
import service.wx.dto.refund.QueryRefundRspDto;
import service.wx.dto.refund.SendRefundReqDto;
import service.wx.dto.refund.SendRefundRspDto;
import service.wx.service.order.OrderQueryService;
import service.wx.service.order.UnifiedOrderService;
import service.wx.service.qrcode.CreateLimitQrCodeService;
import service.wx.service.qrcode.CreateTmpQrCodeService;
import service.wx.service.redpack.QueryRedpackService;
import service.wx.service.redpack.SendRedpackService;
import service.wx.service.refund.QueryRefundService;
import service.wx.service.refund.SendRefundService;
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
    public static SendRedpackRspDto sendRedpackService(SendRedpackReqDto sendRedpackReqDto) throws BusinessException {
    	return new SendRedpackService().request(sendRedpackReqDto);
    }
    
    //查询红包
    public static QueryRedpackRspDto queryRedpackStatusService(QueryRedpackReqDto queryRedpackReqDto) throws BusinessException {
    	return new QueryRedpackService().request(queryRedpackReqDto);
    }
    
    //生成临时二维码，返回的是一张图片
    public static CreateQrCodeRspDto CreateTmpQrCodeService(CreateTmpQrCodeReqDto createTmpQrCodeReqDto)
    		throws BusinessException {
    	return new CreateTmpQrCodeService().request(createTmpQrCodeReqDto);
    }
    
    //生成永久二维码，返回的是一张图片
    public static CreateQrCodeRspDto CreateLimitQrCodeService(CreateLimitQrCodeReqDto createLimitQrCodeReqDto)
    		throws BusinessException {
    	return new CreateLimitQrCodeService().request(createLimitQrCodeReqDto);
    }
    
    //请求退款
    public static SendRefundRspDto sendRefundServcie(SendRefundReqDto sendRefundReqDto) throws BusinessException {
    	return new SendRefundService().request(sendRefundReqDto);
    }
    
    //查询退款
    public static QueryRefundRspDto queryRefundServcie(QueryRefundReqDto queryRefundReqDto) throws BusinessException {
    	return new QueryRefundService().request(queryRefundReqDto);
    }

}
