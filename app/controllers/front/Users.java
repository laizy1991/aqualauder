package controllers.front;

import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.DistributorService;
import service.UserWalletService;
import service.wx.service.user.WxUserService;

import common.annotation.GuestAuthorization;
import common.constants.CashType;
import common.core.FrontController;

import dto.DistributorDetail;
import dto.MySpaceDto;

public class Users extends FrontController {
    @GuestAuthorization
    public static void orders() {
        render("/Front/user/orders.html");
    }
    @GuestAuthorization
    public static void qrcode() {
        render("/Front/user/qrcode.html");
    }
    @GuestAuthorization
    public static void getUserInfo(String code) {
        String openId = session.get("openId");
        if(StringUtils.isBlank(openId)) {
            if(StringUtils.isBlank(code)) {
                Logger.error("code为空");
                renderText("非法请求");
            }
            openId = WxUserService.getUserOpenIdByCode(code);
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
        data.setDetail(detail);
        render("/Front/user/myspace.html", data, code);
    }
    @GuestAuthorization
    public static void cash(String amount) {
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
                isSucc = UserWalletService.cash(user.getUserId(), cashAmount, CashType.REDPACK.getCode(), "");
            }
        } catch(Exception e) {
            Logger.error(e, "");
        }
        renderJSON(isSucc);
    }
}
