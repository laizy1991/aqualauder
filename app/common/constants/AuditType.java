package common.constants;

public enum AuditType {
    DISTRIBUTOR(0,"分销商身份审核");    
    private int code;
    private String desc;
    
    private AuditType(int code, String desc) {
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
