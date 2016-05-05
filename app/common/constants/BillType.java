package common.constants;
/**
 * 流水类型
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public enum BillType {

    CONSUMPTION(0, "", "推广提成(来自%s的消费)"),
    SPREAD(1, "", "推广提成(来自%s的推广)"),
    CASH(2, "提现", ""),
    CASHFAIL(3, "提现失败", ""),
    PAY(4, "支付", "");
    
    private int code;
    private String template;
    private String desc;
    
    private BillType(int code, String desc, String template) {
        this.code = code;
        this.desc = desc;
        this.template = template;
    }
    
    //判断是否为推广金额
    public static boolean isTG(Integer code) {
        if(code == null) {
            return false;
        }
        
        boolean isTg = false;
        switch (code) {
        case 0:
        case 1:
            isTg = true;
            break;
        default:
            isTg = false;
            break;
        }
        
        return isTg;
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

    public String getTemplate() {
        return template;
    }
    
}
