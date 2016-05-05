package common.constants;

/**
 * 退款状态
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public enum RefundStatus {
	//-1=无退款，0=申请退款,1=退款中,2=退款成功,3=拒绝,4=取消,5=退款失败,6=状态未确定,7=代入转发'
    APPLY(0, "申请退款"),
    ING(1, "退款中"),
    SUCCESS(2, "退款成功"),
    REFUSE(3, "拒绝"),
    CANCEL(4, "取消"),
    FAIL(5, "退款失败"),
    NOTSURE(6, "退款状态未确定，需要商户原退款单号重新发起"),
    CHANGE(7, "转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。"),
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
