package service.wx.service;

import com.google.gson.Gson;

import net.sf.json.JSONObject;
import play.Logger;
import exception.BusinessException;
import service.wx.common.Configure;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.redpack.SendRedpackRspDto;

public class SendRedpackService extends BaseService{
	public static Gson gson = new Gson();
    public SendRedpackService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.SEND_REDPACK_API);
    }

    /**
     * 发送现金红包
     * @param sendRedpackReqDto
     * @return
     * @throws Exception
     */
    public SendRedpackRspDto request(SendRedpackReqDto sendRedpackReqDto) throws Exception {

    	String responseString = "";
    	try {
    		responseString = sendPost(sendRedpackReqDto, true);
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信发送现金红包接口发生错误");
    	}
    	Logger.info("现金红包API返回的数据是：%s", responseString);
    	SendRedpackRspDto rsp = (SendRedpackRspDto)Util.getObjectFromXMLWithXStream(responseString, SendRedpackRspDto.class);
    	if(null == rsp || null == rsp.getReturn_code()) {
			//通信失败
			Logger.error("微信现金红包接口通信失败，请求数据为：%s, 返回数据为：%s", JSONObject.fromObject(sendRedpackReqDto).toString(),
					JSONObject.fromObject(rsp).toString());
			 throw new BusinessException("微信现金红包接口通信失败");
		} else {
			Logger.info("现金红包API成功返回对象数据：%s", gson.toJson(rsp));
			if(!rsp.getReturn_code().equals("SUCCESS")) {
				throw new BusinessException("微信现金红包API接口通信失败");
			}
			
			if (rsp.getResult_code().equals("SUCCESS")) {
				try {
					if (!Signature.checkIsSignValidFromResponseString(responseString)) {
						Logger.error("现金红包API返回的数据签名验证失败，有可能数据被篡改了");
					}
				} catch(Exception e) {
					e.printStackTrace();
					throw new BusinessException("微信现金红包API返回的数据签名验证失败");
				}
				return rsp;
            } else {
            	throw new BusinessException("微信现金红包API接口交易失败");
            }
	   }
    }

}
