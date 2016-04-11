package service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.MerchantTradeOrder;
import models.Order;
import models.OrderGoods;
import models.RefundOrder;
import models.User;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import play.Logger;
import play.Play;
import service.wx.WXPay;
import service.wx.common.Configure;
import service.wx.common.RandomStringGenerator;
import service.wx.common.Signature;
import service.wx.dto.unifiedOrder.UnifiedOrderReqDto;
import service.wx.dto.unifiedOrder.UnifiedOrderRspDto;
import common.constants.MessageCode;
import common.constants.RefundStatus;
import common.constants.wx.OutTradeStatus;
import common.constants.wx.OutTradeType;
import common.constants.wx.PayType;
import dao.OrderDao;
import dao.OrderGoodsDao;
import dao.RefundOrderDao;
import dto.OrderDetail;
import exception.BusinessException;

public class OrderService {

    public static Order get(long id) {
        return OrderDao.get(id);
    }

    public static boolean add(Order order) {
        order.setCreateTime(System.currentTimeMillis());
        order.setUpdateTime(System.currentTimeMillis());
        return OrderDao.insert(order);
    }

    public static void delete(Order order) {
        OrderDao.delete(order);
    }

    public static boolean update(Order order) {
        order.setUpdateTime(System.currentTimeMillis());
        return OrderDao.update(order);
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
	 * 订单使用微信支付
	 */
	public static String genOrderPayWithWx(long orderId) throws BusinessException{
		if(orderId <= 0) {
			throw new BusinessException(MessageCode.INVALID_PARAM.getMsg());
		}
		Order order = OrderService.get(orderId);
		if(null == order) {
			throw new BusinessException(MessageCode.GET_ORDER_FAILED.getMsg());
		}
		//获取用户信息
		User user = UserService.get(order.getUserId());
		if(null == user || StringUtils.isBlank(user.getOpenId())) {
			throw new BusinessException(MessageCode.GET_USER_FAILED.getMsg());
		}
		List<OrderGoods> goods = OrderGoodsDao.getByOrder(orderId);
		String subject = "";
		if(null != goods && goods.size() > 0) {
			for(int i=0; i<goods.size(); i++) {
				if(0 != i) {
					subject += "_";
				}
				subject += goods.get(i).getGoodsTitle();
			}
		} else {
			subject = "商品";
		}
		// TODO 记得改callbackUrl(不能带参数)，改交易金额
		String callbackUrl = Play.configuration.getProperty("local.host.domain");;
		//准备插入微信商户订单表
		long createTime = System.currentTimeMillis(); 
		MerchantTradeOrder tradeOrder = new MerchantTradeOrder();
		tradeOrder.setOutTradeNo(order.getOutTradeNo());
		tradeOrder.setOutTradeType(OutTradeType.WXPAY.getType());
		tradeOrder.setUserId(order.getUserId());
		tradeOrder.setTotalFee(order.getTotalFee());
		tradeOrder.setSubject(subject);
		tradeOrder.setCallbackUrl(callbackUrl);
		tradeOrder.setStatus(OutTradeStatus.ADDED);
		tradeOrder.setTradeMsg("");
		tradeOrder.setTradeNo("");
		tradeOrder.setOpenid(user.getOpenId());
		tradeOrder.setCreateTime(createTime);
		tradeOrder.setPayTime(null);
		tradeOrder.setCallbackTime(null);
		if(!MerchantTradeOrderService.add(tradeOrder)) {
			throw new BusinessException(MessageCode.ADD_MECHANT_TRADE_ORDER_FAILED.getMsg());
		}
		// TODO 获取客户端地址，在controller那里，写入order表
		String clientIp = "";
		UnifiedOrderReqDto req = new UnifiedOrderReqDto("WEB", subject, order.getOutTradeNo(), order.getTotalFee(),
				clientIp, callbackUrl, PayType.JS.getType(), user.getOpenId());
		// TODO 2016-04-11 做到这里
		
		UnifiedOrderRspDto rsp  = WXPay.requestUnifiedOrderService(req);
		//准备生成签名
		Map<String, Object> signMap = new HashMap<String,Object>();
		String timeStamp = "" + System.currentTimeMillis()/1000;
		String nonceStr = RandomStringGenerator.getRandomStringByLength(32);
		signMap.put("appId", Configure.getAppid());
		signMap.put("timeStamp", "" + timeStamp);
		signMap.put("nonceStr", nonceStr);
		signMap.put("package", "prepay_id="+rsp.getPrepay_id());
		signMap.put("signType", "MD5");
		String paySign = "";
		paySign = Signature.getSign(signMap);
		
		//准备将参数赋值给前台页面
		String jsRequestBody = "{appId:'" + Configure.getAppid() + "',"; 
		jsRequestBody += "timeStamp:'" + timeStamp + "',"; 
		jsRequestBody += "nonceStr:'" + nonceStr + "',"; 
		jsRequestBody += "package:'prepay_id=" + rsp.getPrepay_id() + "',"; 
		jsRequestBody += "signType:'MD5',"; 
		jsRequestBody += "paySign:'" + paySign + "'}"; 
		
		return jsRequestBody;
	}
	
    /**
     * 订单列表
     * 订单完整信息
     * @param userId 用户ID
     * @param page
     * @param pageSize
     * @return
     * @throws BusinessException 
     */
    public static List<OrderDetail> listOrder( int userId , int page, int pageSize) {
        if( page <= 0 ){
            page = 1;
        } 
        List<OrderDetail> result = new ArrayList<OrderDetail>();
        List<Order> orderList = OrderDao.list(userId, page, pageSize);
        if(CollectionUtils.isEmpty(orderList)) {
            return result;
        }
        
        for(Order order : orderList) {
            OrderDetail detail = new OrderDetail(order);
            result.add(detail);
            
            RefundOrder refund = RefundOrderDao.getByOrder(order.getId());
            if(refund == null) {
                detail.setRefundState(RefundStatus.NOTREFUND.getCode());
            } else {
                detail.setRefundState(refund.getRefundState());
            }
            List<OrderGoods> goods = OrderGoodsDao.getByOrder(order.getId());
            if(CollectionUtils.isEmpty(goods)) {
                continue;
            }
            
            for(OrderGoods item : goods) {
                detail.addGoodsInfo(item);
            }
        }
        return result;
    }
    
    /**
     * 获取单个订单详情
     * @param orderId
     * @return
     */
    public static OrderDetail getOrderDetail( long orderId ){
        Order order = OrderDao.get(orderId);
        OrderDetail detail = new OrderDetail(order);
        RefundOrder refund = RefundOrderDao.getByOrder(order.getId());
        if(refund == null) {
            detail.setRefundState(RefundStatus.NOTREFUND.getCode());
        } else {
            detail.setRefundState(refund.getRefundState());
        }
        List<OrderGoods> goods = OrderGoodsDao.getByOrder(order.getId());
        if(CollectionUtils.isNotEmpty(goods)) {
            for(OrderGoods item : goods) {
                detail.addGoodsInfo(item);
            }
        }
        
        return detail;
    } 
}
