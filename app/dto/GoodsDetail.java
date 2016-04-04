package dto;

import java.util.HashMap;
import java.util.Map;

import models.Goods;
import models.GoodsStock;

import org.apache.commons.collections.map.HashedMap;

public class GoodsDetail {
    private String title;
    private Integer goodsType;
    private String goodsDesc;
    private Integer price;
    private String warmPrompt;
    private Integer state;
    private Map<Integer, Map<Integer, Stock>> stocks = new HashedMap();
    
    public GoodsDetail(){}
    
    public GoodsDetail(Goods goods) {
        if(goods == null) {
            return;
        }
        
        title = goods.getTitle();
        goodsType = goods.getGoodsType();
        goodsDesc = goods.getGoodsDesc();
        price = goods.getPrice();
        warmPrompt = goods.getWarmPrompt();
        state = goods.getState();
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getWarmPrompt() {
        return warmPrompt;
    }

    public void setWarmPrompt(String warmPrompt) {
        this.warmPrompt = warmPrompt;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Map<Integer, Map<Integer, Stock>> getStocks() {
        return stocks;
    }

    public void setStocks(Map<Integer, Map<Integer, Stock>> stocks) {
        this.stocks = stocks;
    }

    public void addStock(GoodsStock stock) {
        if(stock == null || stock.getGoodsSize() <= 0) {
            return;
        }
        
        Stock item = new Stock();
        item.setAmount(stock.getAmount());
        item.setDesc(stock.getStockDesc());
        Map<Integer, Stock> sizeStock = new HashMap<Integer, GoodsDetail.Stock>();
        if(stock.getGoodsColor() <= 0) {
            sizeStock.put(-1, item);
        } else {
            sizeStock.put(stock.getGoodsColor(), item);
        }
        stocks.put(stock.getGoodsSize(), sizeStock);
    }
    
    public class Stock {
        private String desc;
        private int amount;
        public String getDesc() {
            return desc;
        }
        public void setDesc(String desc) {
            this.desc = desc;
        }
        public int getAmount() {
            return amount;
        }
        public void setAmount(int amount) {
            this.amount = amount;
        }
        
    }
    
}
