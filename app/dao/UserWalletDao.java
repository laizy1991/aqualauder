package dao;

import models.UserWallet;

/**
 * 用户钱包
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class UserWalletDao {
    
    public static boolean insert(UserWallet wallet) {
        if (wallet == null) {
            return false;
        }
        
        long nowTS = System.currentTimeMillis();
        wallet.setCreateTime(nowTS);
        wallet.setUpdateTime(nowTS);
        return wallet.create();
    }
    
    public static UserWallet getByUserId(Integer userId) {
        return UserWallet.findById(userId);
    }
    
    public static boolean update(UserWallet wallet) {
        if(wallet == null || wallet.getBalances() < 0) {
            return false;
        }
        
        wallet.setUpdateTime(System.currentTimeMillis());
        wallet.save();
        return true;
    }
    
}
