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

    public static void getUserInfo(String code) {
    	if(StringUtils.isBlank(code)) {
    		renderText("code为空");
    	}
    	String openId = WxUserService.getUserOpenIdByCode(code);
    	if(StringUtils.isBlank(openId)) {
    		renderText("获取到的openId为空");
    	}
    	User user = WxUserService.getUserInfo(openId);
    	if(null == user) {
    		renderText("获取用户信息失败, openId: %s", openId);
    	}
    	renderText(gson.toJson(user));
    	session.put("openId", openId);
    }
}
