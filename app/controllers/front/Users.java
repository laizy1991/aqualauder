package controllers.front;

import java.util.ArrayList;
import java.util.List;

import models.Distributor;
import models.QrShare;
import models.User;
import models.UserWallet;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.BuyerService;
import service.DistributorService;
import service.OrderService;
import service.QrShareService;
import service.UserService;
import service.UserWalletService;
import service.WxMsgService;
import service.wx.dto.jspai.JsapiConfig;
import service.wx.service.jsapi.JsApiService;
import service.wx.service.user.WxUserService;

import com.google.gson.Gson;

import common.annotation.GuestAuthorization;
import common.constants.CashType;
import common.constants.GlobalConstants;
import common.core.FrontController;
import dao.UserWalletDao;
import dto.DistributorDetail;
import dto.MySpaceDto;
import dto.OrderDetail;

public class Users extends FrontController {
	public static Gson gson = new Gson();
	@GuestAuthorization
    public static void order(long orderId) {
		if(orderId <= 0) {
			renderText("非法订单");
		}
		OrderDetail orderDetail = OrderService.getOrderDetail(orderId);
		if(null == orderDetail) {
			renderText("非法订单");
		}
        List<OrderDetail> orders = new ArrayList<OrderDetail>();
        orders.add(orderDetail);
        
        render("/Front/user/orders.html", orders);
    }
	
    @GuestAuthorization
    public static void orders() {
        String openId = session.get("openId");
        User user = null; 
        if(!StringUtils.isBlank(openId)) {
            user = WxUserService.getUserInfo(openId);
        }
        if(null == user) {
            render("/Front/user/orders.html");
        }
        List<OrderDetail> orders = OrderService.listOrder(user.getUserId(), 1, -1);
        render("/Front/user/orders.html", orders);
    }
    public static void qrcodeShare(String userId) {
    	String qrcodeBg = getQrCodeBg();
    	if(StringUtils.isBlank(userId)) {
    		render("/Front/user/companyQrcode.html", qrcodeBg);
    	}
    	User user = null;
    	int id = -1;
    	try {
    		id = Integer.parseInt(userId);
    	} catch(Exception e) {
    		Logger.error("userId[%s]非整型", userId);
    	}
    	if(id > 0) {
    		user = UserService.get(id);
    	} 
    	if(null == user) {
    		render("/Front/user/companyQrcode.html", qrcodeBg);
    	}
    	Distributor dist = DistributorService.get(user.getUserId());
    	if(null == dist) {
    		qrcodeBg = getQrCodeBg();
    		render("/Front/user/companyQrcode.html", qrcodeBg);
    	}
    	String querystring = request.querystring;
    	String protocol = request.secure?"https://":"http://";
    	String action = request.path;
    	String url =  protocol + request.domain + action + "?" + querystring;
    	
    	Logger.info("首页生成的分享链接为: %s", url);
    	JsapiConfig config = JsApiService.getSign(url);
    	Logger.info("首页config参数为: %s", gson.toJson(config));
    	
    	String qrImg = dist.getQrcodeLimitPath();
    	
    	QrShare qrShare = QrShareService.getLastIsEnabledRec(GlobalConstants.IS_ENABLED);

    	render("/Front/user/qrcodeShare.html", user, qrImg, config, qrcodeBg, qrShare);
    }
    
    @GuestAuthorization
    public static void qrcode() {
    	String openId = session.get("openId");
    	User user = null; 
		if(!StringUtils.isBlank(openId)) {
			user = WxUserService.getUserInfo(openId);
		}
		String qrcodeBg = "";
		if(null == user) {
			qrcodeBg = getQrCodeBg();
    		render("/Front/user/companyQrcode.html", qrcodeBg);
    	}
    	Distributor dist = DistributorService.get(user.getUserId());
    	if(null == dist) {
    		qrcodeBg = getQrCodeBg();
    		render("/Front/user/companyQrcode.html", qrcodeBg);
    	}
    	redirect("/front/Users/qrcodeShare?userId="+user.getUserId());
    }
    @GuestAuthorization
    public static void getUserInfo(String code) {
        String openId = session.get("openId");
        if(StringUtils.isBlank(openId)) {
            if(StringUtils.isBlank(code)) {
                Logger.error("code为空");
                renderText("非法请求");
            }
            JSONObject accessCodeJson = WxUserService.getUserOpenIdAndAccessTokenByCode(code); 
        	if(null == accessCodeJson || StringUtils.isBlank(accessCodeJson.optString("openid"))) {
        	    Logger.error("openId为空");
        	    renderText("非法请求");
        	}
        	openId = accessCodeJson.optString("openid");
        }
        
        if(StringUtils.isBlank(openId)) {
            Logger.error("openId为空");
            renderText("非法请求");
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderText("非法请求");
        }
        
        DistributorDetail detail = DistributorService.distributorDetail(user.getUserId());
        
        MySpaceDto data = new MySpaceDto();
        data.setUser(user);
        if(detail == null) {
            detail = new DistributorDetail();
        }
        
        data.setDetail(detail);
        render("/Front/user/myspace.html", data, code);
    }
    @GuestAuthorization
    public static void cash(String amount, int bank) {
        String openId = session.get("openId");
        boolean isSucc = false;
        if(StringUtils.isBlank(openId)) {
            renderJSON(isSucc);
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderJSON(isSucc);
        }
        try {
            int cashAmount = (int)(Double.parseDouble(amount) * 100);
            if(cashAmount > 0) {
                if(cashAmount >= 20000 && bank == 1) {
                    isSucc = UserWalletService.cash(user.getUserId(), cashAmount, CashType.BANK.getCode(), "");
                } else {
                    isSucc = UserWalletService.cash(user.getUserId(), cashAmount, CashType.REDPACK.getCode(), "");
                }
            }
        } catch(Exception e) {
            Logger.error(e, "");
        }
        renderJSON(isSucc);
    }
    
    public static void userInfo() {
        String openId = session.get("openId");
        if(StringUtils.isBlank(openId)) {
            Logger.error("openId为空");
            renderText("非法请求");
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderText("非法请求");
        }
        UserWallet wallet = UserWalletService.get(user.getUserId());
        render("/Front/user/userinfo.html", user, wallet);
    }


    public static void setUserInfo(String cardNo, String bankName, String realName, String weixin) {
        String openId = session.get("openId");
        User user = null; 
        if(!StringUtils.isBlank(openId)) {
            user = WxUserService.getUserInfo(openId);
        }
        if(null == user) {
            renderJSON("{\"msg\":\"更新用户信息失败\"}");
        }
        UserWallet wallet = UserWalletService.get(user.getUserId());
        if(wallet == null) {
            renderJSON("{\"msg\":\"更新用户信息失败\"}");
        }
        UserService.setWeixin(user.getUserId(), weixin);
        wallet.setBankName(bankName);
        wallet.setCardNo(cardNo);
        wallet.setRealName(realName);
        UserWalletDao.update(wallet);
        
        redirect("front.Users.userInfo");
    }
    
    public static void refundApply(long orderId) {
        String openId = session.get("openId");
        boolean isSucc = false;
        if(StringUtils.isBlank(openId)) {
            renderJSON(isSucc);
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderJSON(isSucc);
        }
        
        isSucc = BuyerService.refundApply(user.getUserId(), orderId, "");
        renderJSON(isSucc);
    }
    public static void refundCancel(long refundId) {
        String openId = session.get("openId");
        boolean isSucc = false;
        if(StringUtils.isBlank(openId)) {
            renderJSON(isSucc);
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderJSON(isSucc);
        }
        
        isSucc = BuyerService.refundCancel(user.getUserId(), refundId);
        renderJSON(isSucc);
    }
    

    public static void receiving(long orderId) {
        String openId = session.get("openId");
        boolean isSucc = false;
        if(StringUtils.isBlank(openId)) {
            renderJSON(isSucc);
        }
        User user = WxUserService.getUserInfo(openId);
        if(null == user) {
            Logger.error("获取用户信息失败, openId: %s", openId);
            renderJSON(isSucc);
        }
        
        isSucc = BuyerService.receiving(user.getUserId(), orderId);
        if(isSucc) {
        	WxMsgService.confirmDelivered(orderId);
        }
        renderJSON(isSucc);
    }
}
