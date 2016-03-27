package common.contants;

/**
 * 分销商类型
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public enum DistributorType {

    PERSONAL(0, "个人");
    
    private int code;
    private String desc;
    
    private DistributorType(int code, String desc) {
        this.code =code;
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
