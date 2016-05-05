package service.wx.service.refund;

import net.sf.json.JSONObject;
import play.Logger;
import service.wx.common.Configure;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.refund.QueryRefundReqDto;
import service.wx.dto.refund.QueryRefundRspDto;
import service.wx.service.BaseService;
import exception.BusinessException;

public class QueryRefundService extends BaseService{

    public QueryRefundService() {
        super(Configure.QUERY_REFUND_API);
    }

    /**
     * 查询退款
     * @param sendRefundReqDto 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public QueryRefundRspDto request(QueryRefundReqDto queryRefundReqDto) throws BusinessException {
    	String responseString = "";
    	try {
    		responseString = sendPost(queryRefundReqDto, false).trim();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信查询退款接口发生错误");
    	}
    	Logger.info("查询退款API返回的数据是：%s", responseString);
        //将从API返回的XML数据映射到Java对象
    	QueryRefundRspDto rsp = (QueryRefundRspDto)Util.getObjectFromXMLWithXStream(responseString, QueryRefundRspDto.class);
	   if(null == rsp || null == rsp.getReturn_code() || rsp.getReturn_code().equals("FAIL")) {
			//通信失败
			Logger.error("微信查询退款接口通信失败，请求数据为：%s, 返回数据为：%s", JSONObject.fromObject(queryRefundReqDto).toString(),
					JSONObject.fromObject(rsp).toString());
			 throw new BusinessException("微信查询退款接口通信失败");
		} else {
			Logger.info("查询退款API成功返回数据");
			//先验证一下数据有没有被第三方篡改，确保安全
			try {
				if (!Signature.checkIsSignValidFromResponseString(responseString)) {
					Logger.error("查询退款API返回的数据签名验证失败，有可能数据被篡改了");
	            }
			} catch(Exception e) {
	    		e.printStackTrace();
	    		throw new BusinessException("微信查询退款API返回的数据签名验证失败");
	    	}
			
			return rsp;
	   }
    }
}
