package controllers.front;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import models.CashInfo;
import models.Order;
import models.User;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.CashInfoService;
import service.OrderService;
import service.UserService;
import service.wx.WXPay;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.order.UnifiedOrderCallbackDto;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.redpack.SendRedpackRspDto;
import service.wx.service.user.WxUserService;
import utils.WxUtil;

import com.google.gson.Gson;

import common.constants.GlobalConstants;
import common.constants.RegType;
import common.constants.wx.OutTradeStatus;
import common.core.FrontController;
import exception.BusinessException;


public class Demo extends FrontController {
	
	public static Gson gson = new Gson();

	
    public static void getUserInfo(String code) throws BusinessException{
    	if(StringUtils.isBlank(code)) {
    		renderText("code为空");
    	}
    	String openId = WxUserService.getUserOpenId(code);
    	if(StringUtils.isBlank(openId)) {
    		renderText("获取到的openId为空");
    	}
    	User user = UserService.getByOpenId(openId);
    	if(null == user) {
    		Logger.info("表中没有此用户, 向微信获取用户信息, openId[%s]", openId);
    		JSONObject userInfoJson = WxUserService.getUserInfoByOpenId(openId);
    		if(null == userInfoJson) {
    			renderText("获取到的用户信息为空");
    		}
    		Logger.info("获取到的用户信息为: %s", userInfoJson);
    		user = new User();
    		user.setMobile("");
    		user.setRegType(RegType.WeiXin.getValue());
    		user.setOpenId(openId);
    		user.setNickname(userInfoJson.optString("nickname"));
    		user.setSex(userInfoJson.getInt("sex"));
    		user.setBirthday(null);
    		user.setHeadImgUrl(userInfoJson.optString("headimgurl"));
    		user.setSubscribeTime(userInfoJson.getLong("subscribe_time") * 1000);
    		user.setCreateTime(System.currentTimeMillis());
    		user.setUpdateTime(System.currentTimeMillis());
    		if(!UserService.add(user)) {
    			renderText("创建用户失败，用户信息为：%s", gson.toJson(user));
    		}
    	}
    	user = UserService.getByOpenId(openId);
    	if(null == user) {
    		renderText("插入用户表后获取到的用户信息为空");
    	}
    	renderText("创建用户成功，信息为：%s", gson.toJson(user));
//    	Logger.info("开始创建订单, code[%s], openId[%s]", code, openId);
//    	Order order = new Order();
//    	order.setUserId(user.getUserId());
    	
//    	OrderService.add(order);
//    	render("/Front/Pay/wxPay.html", jsRequestBody, totalFee, order);
    }
    
}
