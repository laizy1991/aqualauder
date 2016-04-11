package common.constants.wx;

public enum PayType {
	//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
	JS("JSAPI", "公众号支付"),
	SCAN("NATIVE", "原生扫码支付"),
	APP("APP", "app支付");
	
	private String type;
	private String desc;
	
	private PayType(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public static PayType resolveType(String type) {
		for (PayType tradeType: PayType.values()) {
			if (type.equals(tradeType.getType())) {
				return tradeType;
			}
		}
		
		return null;
	}

}
