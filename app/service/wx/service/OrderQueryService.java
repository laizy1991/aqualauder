package service.wx.service;

import net.sf.json.JSONObject;
import play.Logger;
import service.wx.common.Configure;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.orderQuery.OrderQueryReqDto;
import service.wx.dto.orderQuery.OrderQueryRspDto;
import exception.BusinessException;

public class OrderQueryService extends BaseService{

    public OrderQueryService() {
        super(Configure.ORDER_QUERY_API);
    }

    /**
     * 查询订单
     * @param orderQueryReqDto 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public OrderQueryRspDto request(OrderQueryReqDto orderQueryReqDto) throws BusinessException {
    	String responseString = "";
    	try {
    		responseString = sendPost(orderQueryReqDto, false).trim();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信查询订单接口发生错误");
    	}
    	Logger.info("查询订单API返回的数据是：%s", responseString);
        //将从API返回的XML数据映射到Java对象
    	OrderQueryRspDto rsp = (OrderQueryRspDto)Util.getObjectFromXMLWithXStream(responseString, OrderQueryRspDto.class);
	   if(null == rsp || null == rsp.getReturn_code()) {
			//通信失败
			Logger.error("查询订单接口通信失败，请求数据为：%s, 返回数据为：%s", JSONObject.fromObject(orderQueryReqDto).toString(),
					JSONObject.fromObject(rsp).toString());
			 throw new BusinessException("查询订单接口通信失败");
		} else {
			Logger.info("查询订单API成功返回数据");
			//先验证一下数据有没有被第三方篡改，确保安全
			try {
				if (!Signature.checkIsSignValidFromResponseString(responseString)) {
					Logger.error("查询订单API返回的数据签名验证失败，有可能数据被篡改了");
	            }
			} catch(Exception e) {
	    		e.printStackTrace();
	    		throw new BusinessException("查询订单API返回的数据签名验证失败");
	    	}
			
			if (rsp.getResult_code().equals("SUCCESS")) {
				return rsp;
            } else {
            	throw new BusinessException("查询订单API返回的数据签名验证失败");
            }
	   }
    }
}
