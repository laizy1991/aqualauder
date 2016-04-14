package common.constants;

/**
 * 分销商状态
 * @author laizy1991@gmail.com
 * @createDate 2016年3月30日
 *
 */
public enum DistributorStatus {

    INIT(0, "未认证"),
    PASS(1, "通过认证"),
    FAILED(2, "认证不通过");
    
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
    
    public static boolean isExist(int code) {
        for (DistributorStatus ele : DistributorStatus.values()) {
            if (ele.getCode() == code) {
                return true;
            }
        }
        return false;
    }
}
