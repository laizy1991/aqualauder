package service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.math.RandomUtils;

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
    
    /**
	 * 产生一个orderId
	 */
	public static String genOrderId() {
		String formatStr = "yyyyMMddHHmmssSSS";
		DateFormat df = new SimpleDateFormat(formatStr);
		String time = df.format(new Date());
		int no = RandomUtils.nextInt(10000);
		String suffix = String.format("%04d",no);//左补0
		return time + suffix;
	}
	
	/**
	 * 生成一张订单，并用微信支付
	 */
	public static void genOrderPayWithWx() {
	}
}
