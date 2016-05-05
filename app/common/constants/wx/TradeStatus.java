package common.constants.wx;

public enum TradeStatus {

	SUCCESS("SUCCESS", "支付成功"),
	REFUND("REFUND", "转入退款"),
	NOTPAY("NOTPAY", "未支付"),
	CLOSED("CLOSED", "已关闭"),
	REVOKED("REVOKED", "已撤销"),
	USERPAYING("USERPAYING", "用户支付中"),
	PAYERROR("PAYERROR", "支付失败");
	
	private String status;
	private String desc;
	
	private TradeStatus(String status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public String getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}
	
	public static TradeStatus getPayStatus(String status) {
		for (TradeStatus trade: TradeStatus.values()) {
			if (trade.getStatus().equals(status)) {
				return trade;
			}
		}
		return null;
	}
}
