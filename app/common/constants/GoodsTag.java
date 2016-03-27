package common.constants;

public enum GoodsTag {
		
		NEW(0),		//新品
		SUIT(1),	//整体搭配
		SINGLE(2),	//单品
		SHOE_HAT(3),//鞋帽
		ACCESSORY(4); //饰品
		
		private int value;
		
		private GoodsTag(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static GoodsTag getGoodsTag(int tag) {
	        for (GoodsTag t : GoodsTag.values()) {
	            if (t.getValue() == tag) {
	                return t;
	            }
	        }
	        return null;
	    }
	}