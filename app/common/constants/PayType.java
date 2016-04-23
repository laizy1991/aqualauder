package common.constants;

/**
 * 支付类型
 * @author laizy1991@gmail.com
 * @createDate 2016年3月30日
 *
 */
public enum PayType {

    WX(0, "微信"),
    BALANCE(1, "余额");
    
    private int code;
    private String desc;
    
    private PayType(int code, String desc) {
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
    
}
