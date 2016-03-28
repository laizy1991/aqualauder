package common.constants;

public enum CashStatus {

    APPLY(0, "申请中"),
    ING(1, "转账中"),
    SUCCESS(2, "提现成功"),
    FAILED(3, "提现失败");
    
    //0-申请中，1-转账中，2-提现成功，3-提现失败
    private int code;
    private String desc;
    private CashStatus(int code, String desc) {
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
