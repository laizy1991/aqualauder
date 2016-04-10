package common.constants;

/**
 * 
 * @author laizy1991@gmail.com
 * @createDate 2016年4月10日
 *
 */
public enum GoodsType {
	
	GOODS(1, "物品"),
	FREEBIE(2, "赠品"),
	OTHER(99, "其它");
	
	private int type;
	private String desc;
	
	private GoodsType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public static GoodsType resolveType(int type) {
		for (GoodsType goodsType: GoodsType.values()) {
			if (type == goodsType.getType()) {
				return goodsType;
			}
		}
		
		return OTHER;
	}

}
