package service;

import java.text.SimpleDateFormat;
import java.util.Date;

import models.CashInfo;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.wx.common.Configure;
import utils.NumberUtil;
import common.constants.CashStatus;
import common.constants.CashType;
import dao.CashInfoDao;

public class CashInfoService {

    public static boolean create(int userId, CashType type, int amount, String slipNo, CashStatus status) {
        CashInfo info = new CashInfo();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        mchBillno += sdf.format(new Date());
        mchBillno += NumberUtil.getRandomNumberString(10);
        
        info.setMchBillno(mchBillno);
        return CashInfoDao.insert(info);
    }
    
    public static CashInfo get(long id) {
    	if(id <= 0)
    		return null;

    	return CashInfoDao.get(id);
    }
    
}
