package service;

import models.UserWalletBill;
import play.Logger;

import com.google.gson.Gson;
import common.contants.BillType;

import dao.UserWalletBillDao;

public class UserWalletBillService {

    public static void createBill(Integer userId, Integer trigger, Integer amount, int operType,
            BillType billType, Integer balance, Integer month, String outTradeNo) {
        if(userId == null || amount == null || amount <= 0) {
            return;
        }
        
        UserWalletBill bill = new UserWalletBill();
        bill.setAmount(amount);
        bill.setBillTime(System.currentTimeMillis());
        bill.setOperType(operType);
        bill.setTrigger(trigger);
        bill.setUserId(userId);
        bill.setBillDesc(billType.getDesc());
        bill.setBalance(balance);
        bill.setBillMonth(month);
        bill.setBillType(billType.getCode());
        bill.setObjId(outTradeNo);
        boolean isSucc = UserWalletBillDao.insert(bill);
        if(isSucc) {
            Logger.error(new Gson().toJson(bill));
        }
    }
    
}
