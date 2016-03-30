package common.constants;

/**
 * 分销商状态
 * @author laizy1991@gmail.com
 * @createDate 2016年3月30日
 *
 */
public enum DistributorStatus {

    UN_AUTH(0, "未认证"),
    ACCESS_AUTH(1, "通过认证"),
    FAIL_AUTh(2, "认证不通过");
    
    private int code;
    private String desc;
    
    private DistributorStatus(int code, String desc) {
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
