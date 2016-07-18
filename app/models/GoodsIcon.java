package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

/**
 * 商品图片
 * @author laizy1991@gmail.com
 * @createDate 2016年4月10日
 *
 */
@Entity
@Table(name="goods_icon")
public class GoodsIcon extends Model {

    @Column(name="goods_id")
    private Long goodsId;

    @Column(name="icon_url")
    private String iconUrl;

    @Column(name="order_by")
    private String orderBy;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
