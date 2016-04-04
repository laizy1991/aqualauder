package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="goods_stock")
public class GoodsStock extends Model {

    @Column(name="goods_id")
    private long goodsId;

    @Column(name="goods_size")
    private int goodsSize;

    @Column(name="goods_color")
    private int goodsColor;
    
    @Column(name="stock_desc")
    private String stockDesc;
    
    @Column(name="amount")
    private int amount;
    
    @Column(name="create_time")
    private Long createTime;

    @Column(name="update_time")
    private Long updateTime;

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(int goodsSize) {
        this.goodsSize = goodsSize;
    }

    public int getGoodsColor() {
        return goodsColor;
    }

    public void setGoodsColor(int goodsColor) {
        this.goodsColor = goodsColor;
    }

    public String getStockDesc() {
        return stockDesc;
    }

    public void setStockDesc(String stockDesc) {
        this.stockDesc = stockDesc;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }
}
