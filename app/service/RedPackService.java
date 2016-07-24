package service;

import models.CashInfo;
import models.User;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.wx.WXPay;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackReqDto;
import service.wx.dto.redpack.SendRedpackRspDto;

import common.constants.CashStatus;

import exception.BusinessException;

public class RedPackService {

	/**
	 * 发送红包
	 * @param cashId
	 * @return
	 * @throws BusinessException
	 */
	public static SendRedpackRspDto sendRedPack(long cashId) throws BusinessException{
    	if(cashId <= 0) {
    		Logger.error("发送现金红包入参不正确，cashId: %d", cashId);
    		throw new BusinessException("发送现金红包入参不正确");
    	}
    	CashInfo ci = CashInfoService.get(cashId);
    	if(null == ci) {
    		Logger.error("获取提现的记录为空，cashId: %d", cashId);
    		throw new BusinessException("获取提现的记录为空");
    	}
    	
    	return sendRedPack(ci);
    }
    
	public static SendRedpackRspDto sendRedPack(CashInfo ci) throws BusinessException{
	    if(ci == null) {
	        return null;
	    }
	    if(ci.getCashStatus() == CashStatus.SUCCESS.getCode()) {
            //已提现成功
            Logger.info("该订单之前已提现成功，cashId: %d", ci.getId());
            throw new BusinessException("该订单之前已提现成功");
        }
        String mchBillno = ci.getMchBillno();
        User user = UserService.get(ci.getUserId());
        if(null == user || StringUtils.isEmpty(user.getOpenId())) {
            Logger.error("发送微信红包时无法获取用户Openid，cashId: %d", ci.getId());
            throw new BusinessException("发送微信红包时无法获取用户Openid");
        }
        String openid = user.getOpenId();
        int totalAmount = ci.getAmount();
        SendRedpackReqDto sendRedpackReqDto = new SendRedpackReqDto(mchBillno, openid, totalAmount);
        SendRedpackRspDto rsp = null;
        try {
            rsp = WXPay.sendRedpackService(sendRedpackReqDto);
        } catch (Exception e) {
            e.printStackTrace();
            rsp = null;
        }
        
        return rsp;
    }
	/**
	 * 查询红包状态
	 * @param cashId
	 * @return
	 * @throws BusinessException
	 */
    public static QueryRedpackRspDto queryRedPack(long cashId) throws BusinessException {
    	if(cashId <= 0) {
    		Logger.error("发送现金红包入参不正确，cashId: %d", cashId);
    		throw new BusinessException("发送现金红包入参不正确");
    	}
    	CashInfo ci = CashInfoService.get(cashId);
    	if(null == ci) {
    		Logger.error("获取提现的记录为空，cashId: %d", cashId);
    		throw new BusinessException("获取提现的记录为空");
    	}
    	if(ci.getCashStatus() == CashStatus.SUCCESS.getCode()) {
    		//已提现成功
    		Logger.info("该订单之前已提现成功，cashId: %d", cashId);
    		throw new BusinessException("该订单之前已提现成功");
    	}
    	String mchBillno = ci.getMchBillno();
    	QueryRedpackReqDto queryRedpackReqDto = new QueryRedpackReqDto(mchBillno);
    	QueryRedpackRspDto rsp = null;
		try {
			rsp = WXPay.queryRedpackStatusService(queryRedpackReqDto);
		} catch (Exception e) {
			e.printStackTrace();
			rsp = null;
		}
		return rsp;
    }
}
