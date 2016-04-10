package common.constants;


/**
 * 订单状态
 * @author laizy1991@gmail.com
 * @createDate 2016年4月10日
 *
 */
public enum OrderStatus {
//订单对应的状态: 0=创建订单(待支付)，1=完成支付,2=待发货，3=完成发货，4=交易成功(确认收货),5=交易关闭
    INIT(0,"订单创建(待支付)"),
    PAYED(1,"完成支付"),
    DELIVERING(2,"待发货"),
    DELIVERED(3,"完成发货"),
    COMPLETE(4,"交易成功(确认收货)"),
    CLOSE(5, "交易关闭");
    
    private int state;
    private String desc;
    
    private OrderStatus(int state, String desc) {
        this.state = state;
        this.desc = desc;
    }
    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static OrderStatus resolveType(int state)
    {
        for(OrderStatus ele:OrderStatus.values())
        {
            if(ele.getState() == state)
            {
                return ele;
            }
        }
        return null;
    }
}