package common.constants.wx;

public enum PayMode {
	//JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
	JS("JSAPI", "公众号支付"),
	SCAN("NATIVE", "原生扫码支付"),
	APP("APP", "app支付");
	
	private String type;
	private String desc;
	
	private PayMode(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public static PayMode resolveType(String type) {
		for (PayMode tradeType: PayMode.values()) {
			if (type.equals(tradeType.getType())) {
				return tradeType;
			}
		}
		
		return null;
	}

}
