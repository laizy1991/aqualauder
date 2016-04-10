package service.wx.service;

import net.sf.json.JSONObject;
import play.Logger;
import service.wx.common.Configure;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.common.XMLParser;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;
import exception.BusinessException;

public class UnifiedOrderService extends BaseService{

    public UnifiedOrderService() {
        super(Configure.UNIFIED_ORDER_API);
    }

    /**
     * 统一下单
     * @param unifiedOrderReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public UnifiedOrderRspDto request(UnifiedOrderReqDto unifiedOrderReqData) throws BusinessException {
    	String responseString = "";
    	try {
    		responseString = sendPost(unifiedOrderReqData, false).trim();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信统一下单接口发生错误");
    	}
    	Logger.info("统一下单API返回的数据是：%s", responseString);
        //将从API返回的XML数据映射到Java对象
    	UnifiedOrderRspDto rsp = (UnifiedOrderRspDto)Util.getObjectFromXMLWithXStream(responseString, UnifiedOrderRspDto.class);
//    	UnifiedOrderRspDto rsp = XMLParser.getObjectFromXML(responseString, UnifiedOrderRspDto.class);
	  /* if(null == rsp || null == rsp.getReturn_code()) {
			//通信失败
			Logger.error("微信统一下单接口通信失败，请求数据为：%s, 返回数据为：%s", JSONObject.fromObject(unifiedOrderReqData).toString(),
					JSONObject.fromObject(rsp).toString());
			 throw new BusinessException("微信统一下单接口通信失败");
		} else {
			Logger.info("统一下单API成功返回数据");
			//先验证一下数据有没有被第三方篡改，确保安全
			try {
				if (!Signature.checkIsSignValidFromResponseString(responseString)) {
					Logger.error("统一下单API返回的数据签名验证失败，有可能数据被篡改了");
	            }
			} catch(Exception e) {
	    		e.printStackTrace();
	    		throw new BusinessException("微信统一下单API返回的数据签名验证失败");
	    	}
			
			if (rsp.getResult_code().equals("SUCCESS")) {
				return rsp;
            } else {
            	throw new BusinessException("微信统一下单API返回的数据签名验证失败");
            }
	   }*/
    	return null;
    }
}
