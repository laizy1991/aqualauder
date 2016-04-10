package common.constants;

/**
 * 退款状态
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public enum RefundStatus {
//-1=无退款，0=申请退款,2=退款中,3=退款成功,4=拒绝退款，5=取消退款',
    APPLY(0, "申请退款"),
    ING(1, "退款中"),
    SUCCESS(2, "退款成功"),
    REFUSE(3, "拒绝"),
    CANCEL(4, "取消"),
    NOTREFUND(-1, "无退款");
    
    private int code;
    private String desc;
    
    private RefundStatus(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    
    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }
    public static RefundStatus resolveType(int code) {
        for (RefundStatus rs: RefundStatus.values()) {
            if (code == rs.getCode()) {
                return rs;
            }
        }
        
        return null;
    }
}
