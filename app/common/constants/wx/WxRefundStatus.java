package common.constants.wx;

public enum WxRefundStatus {
	
	SUCCESS("SUCCESS", "退款成功"),
	FAIL("FAIL", "退款失败"),
	PROCESSING("PROCESSING", "退款处理中"),
	NOTSURE("NOTSURE", "未确定，需要商户原退款单号重新发起"),
	CHANGE("CHANGE", "转入代发，退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败，资金回流到商户的现金帐号，需要商户人工干预，通过线下或者财付通转账的方式进行退款。");
	
	private String type;
	private String desc;
	
	private WxRefundStatus(String type, String desc) {
		this.type = type;
		this.desc = desc;
	}
	
	public String getType() {
		return this.type;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public static WxRefundStatus getRedPackType(String type) {
		for (WxRefundStatus tradeType: WxRefundStatus.values()) {
			if (type.equals(tradeType.getType())) {
				return tradeType;
			}
		}
		
		return null;
	}

}
