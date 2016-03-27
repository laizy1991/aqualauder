package common.constants;

public enum RegType {
		
		WeiXin(1); //微信
		
		private int value;
		
		private RegType(int value) {
			this.value = value;
		}
		
		public int getValue() {
			return value;
		}
		
		public static RegType getGoodsTag(int tag) {
	        for (RegType t : RegType.values()) {
	            if (t.getValue() == tag) {
	                return t;
	            }
	        }
	        return null;
	    }
	}