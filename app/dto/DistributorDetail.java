package dto;

import java.util.Map;

public class DistributorDetail {
    private Long blotter;
    private Long helpBlotter;
    private Long commission;
    private Integer allBalance;
    private Integer usefulBalance;
    private String extensionUrl;
    private String extensionQrCode;
    private Integer type;

    /**
     * 下线数
     */
    private Map<Integer, Integer> underlingCount;
    
    /**
     * 累计收入
     */
    private Integer totalIncome;

    public Long getBlotter() {
        return blotter;
    }

    public void setBlotter(Long blotter) {
        this.blotter = blotter;
    }

    public Long getHelpBlotter() {
        return helpBlotter;
    }

    public void setHelpBlotter(Long helpBlotter) {
        this.helpBlotter = helpBlotter;
    }

    public Long getCommission() {
        return commission;
    }

    public void setCommission(Long commission) {
        this.commission = commission;
    }

    public Integer getAllBalance() {
        return allBalance;
    }

    public void setAllBalance(Integer allBalance) {
        this.allBalance = allBalance;
    }

    public Integer getUsefulBalance() {
        return usefulBalance;
    }

    public void setUsefulBalance(Integer usefulBalance) {
        this.usefulBalance = usefulBalance;
    }

    public String getExtensionUrl() {
        return extensionUrl;
    }

    public void setExtensionUrl(String extensionUrl) {
        this.extensionUrl = extensionUrl;
    }

    public String getExtensionQrCode() {
        return extensionQrCode;
    }

    public void setExtensionQrCode(String extensionQrCode) {
        this.extensionQrCode = extensionQrCode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<Integer, Integer> getUnderlingCount() {
        return underlingCount;
    }

    public void setUnderlingCount(Map<Integer, Integer> underlingCount) {
        this.underlingCount = underlingCount;
    }

    public Integer getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Integer totalIncome) {
        this.totalIncome = totalIncome;
    }
}
