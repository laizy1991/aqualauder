package service;

import models.UserWallet;
import play.Logger;
import utils.DistributeCacheLock;

import common.contants.BillType;
import common.contants.OperType;

import dao.UserWalletDao;

/**
 * 钱包相关操作
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class UserWalletService {
    /**
     * 用户消费
     * @param userId
     * @param amount
     * @param outTradeNo
     * @param billType
     * @param consumerId
     * @param blotter
     * @param month
     * @param limit 是否限额
     * @return
     */
    public static boolean spend(int userId, int amount, String outTradeNo, BillType billType,
            Integer consumerId, long blotter, int month) {
        if(userId <= 0 || amount <= 0) {
            return false;
        }

        //加锁
        final DistributeCacheLock lockMgr = DistributeCacheLock.getInstance();
        final String lockKey = getWalletsLockKey(userId);
        
        if(lockMgr.isLocked(lockKey)) {
            return false;
        }
        if(!lockMgr.tryLock(lockKey)) {
            return false;
        }
        
        try {
            UserWallet userWallet = UserWalletDao.getByUserId(userId);
            if(userWallet == null || userWallet.getBalances() < amount) {
                return false;
            }
            
            userWallet .setBalances(userWallet.getBalances() - amount);
            boolean isSucc = UserWalletDao.update(userWallet);
            if(isSucc) {
                Integer balance = userWallet.getBalances();
                UserWalletBillService.createBill(userId, consumerId, amount,
                        OperType.OUT.getCode(), billType, balance, month, outTradeNo);
            }
            return isSucc;
        } finally {
            lockMgr.unLock(lockKey);
        }
    }

    public static boolean income(BillType billType, int userId, Integer consumerId, int month, int addMoney, String outTradeNo) {
        if(userId <= 0 || addMoney <= 0) {
            return false;
        }
        

        //加锁
        final DistributeCacheLock lockMgr = DistributeCacheLock.getInstance();
        final String lockKey = getWalletsLockKey(userId);
        
        if(lockMgr.isLocked(lockKey)) {
            return false;
        }
        if(!lockMgr.tryLock(lockKey)) {
            return false;
        }
        
        try {
            UserWallet userWallet = UserWalletDao.getByUserId(userId);
            if(userWallet == null) {
                userWallet = new UserWallet();
                userWallet.setBalances(0);
                userWallet.setIncome(0);
                userWallet.setUserId(userId);
                boolean isUScc = UserWalletDao.insert(userWallet);
                if(!isUScc) {
                    Logger.error("user wallets not found and init fail, userId:%d, addMoney:%d, billType:%d", userId, addMoney, billType.getCode());
                    return false;
                }
            }
            
            userWallet.setBalances(userWallet.getBalances() + addMoney);
            userWallet.setIncome(userWallet.getIncome() + addMoney);
            boolean isSucc = UserWalletDao.update(userWallet);
            if(isSucc) {
                Integer balance = userWallet.getBalances();
                UserWalletBillService.createBill(userId, consumerId, addMoney,
                        OperType.INTO.getCode(), billType, balance, month, outTradeNo);
            }
            
            return isSucc;
        } finally {
            lockMgr.unLock(lockKey);
        }
    }
    
    
    /**
     * 锁的键
     * @param userId
     * @return
     */
    public static String getWalletsLockKey(int userId) {
        return "user_wallets_" + userId;
    }
    
}
