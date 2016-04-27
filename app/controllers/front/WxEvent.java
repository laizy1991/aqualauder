package controllers.front;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.cache.Cache;
import service.wx.common.SignUtil;
import service.wx.common.Util;
import service.wx.common.XMLParser;
import service.wx.dto.user.SubscribeReqDto;
import service.wx.service.user.WxUserService;

import com.google.gson.Gson;

import common.core.FrontController;

public class WxEvent extends FrontController{
	public static Gson gson = new Gson();
	
	/*public static void valid() {
		String signature = request.params.get("signature");  
        String timestamp = request.params.get("timestamp");  
        String nonce = request.params.get("nonce");  
        String echostr = request.params.get("echostr");  
        
        if(SignUtil.checkSignature(signature, timestamp, nonce)) {
        	renderJSON(echostr);
        }
	}*/
	/**
	 * 关注事件
	 */
	public static void dispatchEvent() {
		if(request.method.toUpperCase().equals("GET")) {
			//处理检验
			String signature = request.params.get("signature");  
			String timestamp = request.params.get("timestamp");  
			String nonce = request.params.get("nonce");  
			String echostr = request.params.get("echostr");  
			
			if(SignUtil.checkSignature(signature, timestamp, nonce)) {
				renderJSON(echostr);
			} else {
				Logger.error("微信事件验证失败, 各参数为: %s", gson.toJson(request.params));
				renderJSON("");
			}
		}
		StringBuffer reqStr = new StringBuffer();
		try {
			String requestBody = "";
			InputStream in = request.body;
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			while((requestBody = reader.readLine())!=null){
				reqStr.append(requestBody);
			}
		} catch (IOException e) {
			Logger.error("微信事件请求发生错误");
			e.printStackTrace();
		}
		String reqXml = new String(reqStr);
		if(!StringUtils.isEmpty(reqXml)) {
			Logger.info("微信事件请求报文为: %s", reqStr);
			Map<String,Object> xmlMap = null;
			try {
				xmlMap = XMLParser.getMapFromXML(reqXml);
				Logger.info("微信事件转换为Map: %s", gson.toJson(xmlMap));
			} catch (Exception e) {
				Logger.error("将微信事件请求从XML转为Map出错");
				e.printStackTrace();
			}
			//微信服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次。
			if(null != xmlMap) {
				String event = (String)xmlMap.get("Event");
				if(StringUtils.isBlank(event)) {
					Logger.error("获取到的微信事件类型为空");
					return;
				}
				if(event.toLowerCase().equals("subscribe")) {
					//订阅事件，关注
					Logger.info("微信事件为订阅事件");
					//TODO 这下面的方法不执行，也不报错，日了狗了
					subscribe(reqXml);
				} else {
					Logger.info("微信事件类型为: %s", event);
					return;
				}
			} else {
				Logger.error("微信事件请求Map为空");
				return;
			}
		} else {
			Logger.error("微信事件请求报文为空");
			return;
		}
		//服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
	}
	
	/**
	 * 订阅事件，关注
	 * @param reqXml String 请求的XML字符串
	 */
	public static void subscribe(String reqXml) {
		if(StringUtils.isBlank(reqXml)) {
			Logger.info("微信订阅事件传入的参数为空");
			return;
		}
		//XML转为对象
		SubscribeReqDto req = (SubscribeReqDto)Util.getObjectFromXMLWithXStream(reqXml, SubscribeReqDto.class);
		if(null == req) {
			Logger.error("微信订阅事件由XML转换得来的对象为空");
			return;
		}
		String cacheKey = req.getFromUserName()+"_"+req.getCreateTime();	//微信可能会重试多次
		if(!StringUtils.isBlank((String)Cache.get(cacheKey))) {
			//前面已成功处理过
			Logger.info("用户的订阅事件已处理过, 参数: %s", gson.toJson(req));
			renderJSON("");
		}
		boolean subscribedFlag = WxUserService.subscribe(req);
		if(false == subscribedFlag) {
			//用户入库失败，不返回信息，让微信重试
			return;
		}
		session.put("openId", req.getFromUserName());
		renderJSON("");
	}
}