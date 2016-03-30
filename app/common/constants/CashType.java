package common.constants;

/**
 * 提现方式
 * @author laizy1991@gmail.com
 * @createDate 2016年3月30日
 *
 */
public enum CashType {

    EREDPACKET(0, "红包"),
    BANK(1, "银行卡");
    
    private int code;
    private String desc;
    
    private CashType(int code, String desc) {
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
