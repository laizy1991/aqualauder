package models;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="goods")
public class Goods extends Model {

    @Column(name="identifier")
    private String identifier;

    @Column(name="title")
    private String title;

    @Column(name="goods_type")
    private Integer goodsType;

    @Column(name="goods_desc")
    private String goodsDesc;

    @Column(name="price")
    private Integer price;

    @Column(name="warm_prompt")
    private String warmPrompt;

    @Column(name="state")
    private Integer state;

    @Column(name="create_time")
    private Long createTime;

    @Column(name="update_time")
    private Long updateTime;

    @Column(name="order_by")
    private Integer orderBy;

    @OneToMany
    @JoinColumn(name="goods_id", insertable = false, updatable = false)
    private List<GoodsIcon> goodsIcons;

    @OneToMany
    @JoinColumn(name="goods_id", insertable = false, updatable = false)
    private List<GoodsStock> goodsStocks;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public String getWarmPrompt() {
        return warmPrompt;
    }

    public void setWarmPrompt(String warmPrompt) {
        this.warmPrompt = warmPrompt;
    }

    public List<GoodsIcon> getGoodsIcons() {
        return goodsIcons;
    }

    public void setGoodsIcons(List<GoodsIcon> goodsIcons) {
        this.goodsIcons = goodsIcons;
    }

    public List<GoodsStock> getGoodsStocks() {
        return goodsStocks;
    }

    public void setGoodsStocks(List<GoodsStock> goodsStocks) {
        this.goodsStocks = goodsStocks;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(Integer orderBy) {
        this.orderBy = orderBy;
    }
}
