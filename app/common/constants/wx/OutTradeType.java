package common.constants.wx;

public enum OutTradeType {
	
	WXPAY(1, "微信支付"),
	ALIPAY(2, "阿里");
	
	private int type;
	private String desc;
	
	private OutTradeType(int type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public int getType() {
		return this.type;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public static OutTradeType resolveType(int type) {
		for (OutTradeType tradeType: OutTradeType.values()) {
			if (type == tradeType.getType()) {
				return tradeType;
			}
		}
		
		return null;
	}

}
