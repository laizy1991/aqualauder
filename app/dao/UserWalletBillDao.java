package dao;

import models.UserWalletBill;

/**
 * 用户钱包流水
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class UserWalletBillDao {

    public static boolean insert(UserWalletBill bill) {
        if(bill == null) {
            return false;
        }
        
        return bill.create();
    }
    
}
