package service;

import dao.OrderDao;
import models.Order;

public class OrderService {

    public static Order get(int id) {
        return OrderDao.get(id);
    }

    public static void add(Order order) {
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        OrderDao.insert(order);
    }

    public static void delete(Order order) {
        OrderDao.delete(order);
    }

    public static void update(Order order) {
        order.setUpdateTime(System.currentTimeMillis());
        OrderDao.update(order);
    }
}
