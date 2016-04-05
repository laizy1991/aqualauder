package service;

import models.CashInfo;

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
        return CashInfoDao.insert(info);
    }
    
}
