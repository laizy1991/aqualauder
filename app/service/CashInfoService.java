package service;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.CashInfo;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.wx.common.Configure;
import service.wx.dto.redpack.SendRedpackRspDto;
import utils.DateUtil;
import utils.IdGenerator;
import utils.NumberUtil;

import common.constants.BillType;
import common.constants.CashStatus;
import common.constants.CashType;
import common.constants.CommonDictKey;
import common.constants.CommonDictType;

import dao.CashInfoDao;
import exception.BusinessException;

public class CashInfoService {

    public static boolean create(int userId, CashType type, int amount, String slipNo, CashStatus status) {
        if(amount < 20000) {
            type = CashType.REDPACK;
        }
        
        CashInfo info = new CashInfo();
        info.setId(IdGenerator.getId());
        info.setAmount(amount);
        info.setCashStatus(status.getCode());
        info.setSlipNo(slipNo);
        info.setCashType(type.getCode());
        info.setUserId(userId);
        //添加发送红包微信必传的订单号，请不要修改
        String mchBillno = Configure.getMchid();
        if(StringUtils.isEmpty(mchBillno)) {
        	Logger.error("获取微信商户Mchid为空");
        	return false; 
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        mchBillno += sdf.format(new Date());
        mchBillno += NumberUtil.getRandomNumberString(4);
        
        info.setMchBillno(mchBillno);
        
        //创建的时候判断是否
        String cashAuditLimitStr = CommonDictService.getValue(CommonDictType.CONFIG, CommonDictKey.CASH_AUDIT_LIMIT);
        int limit = -1;
        try {
            limit = Integer.parseInt(cashAuditLimitStr);
        } catch(Exception e) {
            Logger.error("invalid config, key:%s", CommonDictKey.CASH_AUDIT_LIMIT);
            limit = -1;
        }
        boolean autoAudit = true;
        if((limit > 0 && amount > limit) || type.getCode() == CashType.BANK.getCode()) {
            autoAudit = false;
        }
        
        boolean isSuccess = CashInfoDao.insert(info);
        if(isSuccess && autoAudit) {
            return cashAudit(info, true);
        } 
        return isSuccess;
    }
    
    public static CashInfo get(long id) {
    	if(id <= 0)
    		return null;

    	return CashInfoDao.get(id);
    }
    
    public static boolean cashAudit(long id, boolean pass) {
        CashInfo info = CashInfoDao.get(id);
        if(info == null) {
            Logger.error("cash info not found, id:%d", id);
            return false;
        }
        return cashAudit(info, pass);
    }
    
    public static boolean cashAudit(CashInfo info, boolean pass) {
        if(info == null) {
            return false;
        }
        if(!pass) {
            cashFail(info);
            return true;
        }
        
        if(info.getCashType() == CashType.BANK.getCode()) {
            info.setCashStatus(CashStatus.SUCCESS.getCode());
            info.setUpdateTime(System.currentTimeMillis());
            CashInfoDao.update(info);
            return true;
        }
        
        SendRedpackRspDto rsp = null;
        
        try {
            rsp = RedPackService.sendRedPack(info);
        } catch (BusinessException e) {
            Logger.error(e, "");
        }
        
        if(rsp != null && rsp.getReturn_code().equalsIgnoreCase("SUCCESS") && rsp.getResult_code().equalsIgnoreCase("SUCCESS")) {
            //红包发送成功
            info.setCashStatus(CashStatus.SUCCESS.getCode());
            info.setUpdateTime(System.currentTimeMillis());
            CashInfoDao.update(info);
            return true;
        } else {
            cashFail(info);
            return false;
        }
    }
    
    private static void cashFail(CashInfo info) {
        info.setCashStatus(CashStatus.FAILED.getCode());
        info.setUpdateTime(System.currentTimeMillis());
        CashInfoDao.update(info);
        //退回余额
        UserWalletService.income(BillType.CASHFAIL, info.getUserId(), null, DateUtil.getThisMonth(), info.getAmount(), info.getMchBillno());
    }
}
