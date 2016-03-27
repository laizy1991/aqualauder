package common.contants;

/**
 * 操作类型
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public enum OperType {

    INTO(0, "转入"),
    OUT(1, "转出");
    
    private int code;
    private String desc;
    
    private OperType(int code, String desc) {
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
