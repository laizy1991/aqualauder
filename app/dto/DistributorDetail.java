package dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistributorDetail {
    private Map<Integer,List<String>> underling = new HashMap<Integer, List<String>>();
    private Map<Integer, Map<Integer, Long>> underlingBlotter = new HashMap<Integer, Map<Integer, Long>>();
    private Integer allBalance;
    private Integer usefulBalance;
    private String extensionUrl;
    private String extensionQrCode;
    private Integer type;
    /**
     * 累计收入
     */
    private Integer totalIncome;
    public Map<Integer, List<String>> getUnderling() {
        return underling;
    }
    public void setUnderling(Map<Integer, List<String>> underling) {
        this.underling = underling;
    }
    public Map<Integer, Map<Integer, Long>> getUnderlingBlotter() {
        return underlingBlotter;
    }
    public void setUnderlingBlotter(Map<Integer, Map<Integer, Long>> underlingBlotter) {
        this.underlingBlotter = underlingBlotter;
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
    public Integer getTotalIncome() {
        return totalIncome;
    }
    public void setTotalIncome(Integer totalIncome) {
        this.totalIncome = totalIncome;
    }
    
    public void addUnderling(int level, String userName) {
        List<String> underlings = underling.get(level);
        if(underlings == null) {
            underlings = new ArrayList<String>();
            underling.put(level, underlings);
        }
        
        if(underlings.contains(userName)) {
            return;
        }
        
        underlings.add(userName);
        
    }
    
    public void addBlotter(int level, int userId, long blotter) {
        Map<Integer, Long> blotters = underlingBlotter.get(level);
        if(blotters == null) {
            blotters = new HashMap<Integer, Long>();
            underlingBlotter.put(level, blotters);
        }
        
        Long userBlotter = blotters.get(userId);
        if(userBlotter == null) {
            userBlotter = blotter;
        } else {
            userBlotter += blotter;
        }
        blotters.put(userId, userBlotter);
    }
}
