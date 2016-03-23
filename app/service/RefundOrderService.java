package service;

import dao.RefundOrderDao;
import models.RefundOrder;

public class RefundOrderService {

    public static RefundOrder get(int id) {
        return RefundOrderDao.get(id);
    }

    public static void add(RefundOrder refundOrder) {
        refundOrder.setCreateTime(System.currentTimeMillis());
        refundOrder.setUpdateTime(System.currentTimeMillis());
        RefundOrderDao.insert(refundOrder);
    }

    public static void delete(RefundOrder refundOrder) {
        RefundOrderDao.delete(refundOrder);
    }

    public static void update(RefundOrder refundOrder) {
        refundOrder.setUpdateTime(System.currentTimeMillis());
        RefundOrderDao.update(refundOrder);
    }
}
