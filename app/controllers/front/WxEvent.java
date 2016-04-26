package controllers.front;

import models.User;
import service.wx.dto.user.SubscribeReqDto;
import service.wx.service.user.WxUserService;

import common.core.FrontController;

public class WxEvent extends FrontController{
	
	/**
	 * 关注事件
	 */
	public static void subscribe() {
		//TODO 获取关注参数
		SubscribeReqDto subscribeReqDto = new SubscribeReqDto();
		boolean subscribedFlag = WxUserService.subscribe(subscribeReqDto);
		if(false == subscribedFlag) {
			//用户入库失败，不返回信息，让微信重试
			return;
		}
		//服务器无法保证在五秒内处理并回复，可以直接回复空串，微信服务器不会对此作任何处理，并且不会发起重试。
		renderJSON("");
	}
}
