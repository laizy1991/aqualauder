package common.contants;

/**
 * 通用字典类型
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public enum CommonDictType {

    LEVEL_PAY(0, "等级提成"),
    CONFIG(1, "配置");
    
    private int code;
    private String desc;
    private CommonDictType(int code, String desc) {
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
