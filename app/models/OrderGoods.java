package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name="order_goods")
public class OrderGoods extends Model {

    @Column(name="order_id")
    private Long orderId;

    @Column(name="goods_id")
    private Long goodsId;

    @Column(name="goods_title")
    private String goodsTitle;
    
    @Column(name="goods_type")
    private Integer goodsType;
    
    @Column(name="goods_number")
    private Integer goodsNumber;

    @Column(name="goods_icon")
    private String goodsIcon;

    @Column(name="goods_size")
    private String goodsSize;

    @Column(name="goods_color")
    private String goodsColor;

    @Column(name="goods_desc")
    private String goodsDesc;

    @Column(name="goods_identifier")
    private String goodsIdentifier;

    @Column(name="goods_origin_price")
    private Integer goodsOriginPrice;
    
    @Column(name="goods_discount_price")
    private Integer goodsDiscountPrice;

    @Column(name="create_time")
    private Long createTime;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getGoodsSize() {
        return goodsSize;
    }

    public void setGoodsSize(String goodsSize) {
        this.goodsSize = goodsSize;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsColor() {
        return goodsColor;
    }

    public void setGoodsColor(String goodsColor) {
        this.goodsColor = goodsColor;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public Integer getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(Integer goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsTitle() {
        return goodsTitle;
    }

    public void setGoodsTitle(String goodsTitle) {
        this.goodsTitle = goodsTitle;
    }

    public String getGoodsIcon() {
        return goodsIcon;
    }

    public void setGoodsIcon(String goodsIcon) {
        this.goodsIcon = goodsIcon;
    }

    public String getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(String goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public Integer getGoodsOriginPrice() {
        return goodsOriginPrice;
    }

    public void setGoodsOriginPrice(Integer goodsOriginPrice) {
        this.goodsOriginPrice = goodsOriginPrice;
    }

    public Integer getGoodsDiscountPrice() {
        return goodsDiscountPrice;
    }

    public void setGoodsDiscountPrice(Integer goodsDiscountPrice) {
        this.goodsDiscountPrice = goodsDiscountPrice;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getGoodsIdentifier() {
        return goodsIdentifier;
    }

    public void setGoodsIdentifier(String goodsIdentifier) {
        this.goodsIdentifier = goodsIdentifier;
    }
}
