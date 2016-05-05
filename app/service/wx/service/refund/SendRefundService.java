package service.wx.service.refund;

import net.sf.json.JSONObject;
import play.Logger;
import service.wx.common.Configure;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.refund.SendRefundReqDto;
import service.wx.dto.refund.SendRefundRspDto;
import service.wx.service.BaseService;
import exception.BusinessException;

public class SendRefundService extends BaseService{

    public SendRefundService() {
        super(Configure.SEND_REFUND_API);
    }

    /**
     * 请求退款
     * @param sendRefundReqDto 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public SendRefundRspDto request(SendRefundReqDto sendRefundReqDto) throws BusinessException {
    	String responseString = "";
    	try {
    		responseString = sendPost(sendRefundReqDto, true).trim();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信请求退款接口发生错误");
    	}
    	Logger.info("请求退款API返回的数据是：%s", responseString);
        //将从API返回的XML数据映射到Java对象
    	SendRefundRspDto rsp = (SendRefundRspDto)Util.getObjectFromXMLWithXStream(responseString, SendRefundRspDto.class);
	   if(null == rsp || null == rsp.getReturn_code() || rsp.getReturn_code().equals("FAIL")) {
			//通信失败
			Logger.error("微信请求退款接口通信失败，请求数据为：%s, 返回数据为：%s", JSONObject.fromObject(sendRefundReqDto).toString(),
					JSONObject.fromObject(rsp).toString());
			 throw new BusinessException("微信请求退款接口通信失败");
		} else {
			Logger.info("请求退款API成功返回数据");
			//先验证一下数据有没有被第三方篡改，确保安全
			try {
				if (!Signature.checkIsSignValidFromResponseString(responseString)) {
					Logger.error("请求退款API返回的数据签名验证失败，有可能数据被篡改了");
	            }
			} catch(Exception e) {
	    		e.printStackTrace();
	    		throw new BusinessException("微信请求退款API返回的数据签名验证失败");
	    	}
			
			return rsp;
	   }
    }
}
