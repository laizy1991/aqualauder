package common.constants;

public enum AuditStatus {
    INIT(0,"未审核"),
    PASS(1,"审核通过"),
    FAILED(-1,"审核不通过");
    
    private int status;
    private String desc;
    
    private AuditStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static AuditStatus resolveType(int status) {
        for (AuditStatus ele : AuditStatus.values()) {
            if (ele.getStatus() == status) {
                return ele;
            }
        }
        return null;
    }
}
