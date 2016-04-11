package service;

import models.MerchantTradeOrder;
import dao.MerchantTradeOrderDao;

public class MerchantTradeOrderService {

    public static MerchantTradeOrder get(long id) {
        return MerchantTradeOrderDao.get(id);
    }

    public static boolean add(MerchantTradeOrder order) {
        return MerchantTradeOrderDao.insert(order);
    }

    public static void delete(MerchantTradeOrder order) {
    	MerchantTradeOrderDao.delete(order);
    }

    public static boolean update(MerchantTradeOrder order) {
        return MerchantTradeOrderDao.update(order);
    }
    
}
