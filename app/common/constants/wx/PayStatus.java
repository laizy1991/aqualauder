package common.constants.wx;

public enum PayStatus {
	//10初始状态 20准备支付 30放弃支付 40支付失败 50支付成功
	INIT(10, "初始状态"),
	PAY_READY(20, "准备支付"),
	PAY_CANCEL(30, "放弃支付"),
	PAY_FAIL(40, "支付失败"),
	PAY_SUCC(50, "支付成功");
	
	private Integer status;
	private String desc;
	
	private PayStatus(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}
	
	public static PayStatus getPayStatus(Integer status) {
		for (PayStatus pay: PayStatus.values()) {
			if (status == pay.getStatus()) {
				return pay;
			}
		}
		
		return null;
	}
}
