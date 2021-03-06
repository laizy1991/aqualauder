package common.constants.wx;

public enum RedPackStatus {
	
	SENDING("SENDING", "发放中"),
	SENT("SENT", "已发放待领取"),
	FAILED("FAILED", "发放失败"),
	RECEIVED("RECEIVED", "已领取"),
	REFUND("REFUND", "已退款");
	
	private String type;
	private String desc;
	
	private RedPackStatus(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public static RedPackStatus getRedPackType(String type) {
		for (RedPackStatus tradeType: RedPackStatus.values()) {
			if (type.equals(tradeType.getType())) {
				return tradeType;
			}
		}
		
		return null;
	}

}
