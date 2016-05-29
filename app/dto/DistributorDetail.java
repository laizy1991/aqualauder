package dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class DistributorDetail {
    private Map<Integer,List<String>> underling = new HashMap<Integer, List<String>>();
    private Map<Integer, Map<Integer, Long>> underlingBlotter = new HashMap<Integer, Map<Integer, Long>>();
    private int orderSuccessCount;
    private long orderSuccessAmount;
    private int orderFailCount;
    private long orderFailAmount;
    private int allBalance;
    private int usefulBalance;
    private int unpayWealth;
    private int payWealth;
    private int receWealth;
    private String extensionUrl;
    private String extensionQrCode;
    private long saleAmount = 0l;
    private int type;
    private String superior;
    
    /**
     * 累计收入
     */
    private int totalIncome;
    public Map<Integer, List<String>> getUnderling() {
        return underling;
    }
    public void setUnderling(Map<Integer, List<String>> underling) {
        this.underling = underling;
    }
    public Long getSaleAmount() {
        return saleAmount;
    }
    public void setSaleAmount(Long saleAmount) {
        this.saleAmount = saleAmount;
    }
    public Map<Integer, Map<Integer, Long>> getUnderlingBlotter() {
        return underlingBlotter;
    }
    public void setUnderlingBlotter(Map<Integer, Map<Integer, Long>> underlingBlotter) {
        this.underlingBlotter = underlingBlotter;
    }

    public int getOrderSuccessCount() {
        return orderSuccessCount;
    }
    public void setOrderSuccessCount(int orderSuccessCount) {
        this.orderSuccessCount = orderSuccessCount;
    }
    public long getOrderSuccessAmount() {
        return orderSuccessAmount;
    }
    public void setOrderSuccessAmount(long orderSuccessAmount) {
        this.orderSuccessAmount = orderSuccessAmount;
    }
    public int getOrderFailCount() {
        return orderFailCount;
    }
    public void setOrderFailCount(int orderFailCount) {
        this.orderFailCount = orderFailCount;
    }
    public long getOrderFailAmount() {
        return orderFailAmount;
    }
    public void setOrderFailAmount(long orderFailAmount) {
        this.orderFailAmount = orderFailAmount;
    }
    public int getAllBalance() {
        return allBalance;
    }
    public void setAllBalance(int allBalance) {
        this.allBalance = allBalance;
    }
    public int getUsefulBalance() {
        return usefulBalance;
    }
    public void setUsefulBalance(int usefulBalance) {
        this.usefulBalance = usefulBalance;
    }
    public int getUnpayWealth() {
        return unpayWealth;
    }
    public void setUnpayWealth(int unpayWealth) {
        this.unpayWealth = unpayWealth;
    }
    public int getPayWealth() {
        return payWealth;
    }
    public void setPayWealth(int payWealth) {
        this.payWealth = payWealth;
    }
    public int getReceWealth() {
        return receWealth;
    }
    public void setReceWealth(int receWealth) {
        this.receWealth = receWealth;
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
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getSuperior() {
        return superior;
    }
    public void setSuperior(String superior) {
        this.superior = superior;
    }
    public Integer getTotalIncome() {
        return totalIncome;
    }
    public void setTotalIncome(Integer totalIncome) {
        this.totalIncome = totalIncome;
    }
    public void setSaleAmount(long saleAmount) {
        this.saleAmount = saleAmount;
    }
    public void addUnpayWealth(int wealth) {
        unpayWealth += wealth;
    }
    public void addPayWealth(int wealth) {
        payWealth += wealth;
    }
    public void addReceWealth(int wealth) {
        receWealth += wealth;
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
        
        saleAmount += blotter;
        
        Long userBlotter = blotters.get(userId);
        if(userBlotter == null) {
            userBlotter = blotter;
        } else {
            userBlotter += blotter;
        }
        blotters.put(userId, userBlotter);
    }
    
    public int getUnderlingCount(int level) {
        int count = 0;
        for(Entry<Integer, List<String>> entry : underling.entrySet()) {
            
            if(level <= 0 || entry.getKey().intValue() == level) {
                count += entry.getValue().size();
            }
        }
        
        return count;
    }
    

    public int getUnderlingValuableCount(int level) {
        int count = 0;
        for(Entry<Integer, Map<Integer, Long>> entry : underlingBlotter.entrySet()) {
            if(level <= 0 || entry.getKey().intValue() == level) {
                count += entry.getValue().size();
            }
        }
        return count;
    }
    
    public int getOrderCount() {
        int count = orderFailCount + orderSuccessCount;
        return count;
    }
    
    public long getOrderAmount() {
        return orderFailAmount + orderSuccessAmount;
    }
    
    public int cashAmount() {
        return totalIncome - usefulBalance;
    }
    
    public int wealth() {
        return unpayWealth + payWealth + receWealth + totalIncome;
    }
}
