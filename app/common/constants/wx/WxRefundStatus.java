package common.constants.wx;

import common.constants.RefundStatus;

public enum WxRefundStatus {
	
	SUCCESS("SUCCESS", RefundStatus.SUCCESS.getCode()),
	FAIL("FAIL", RefundStatus.FAIL.getCode()),
	PROCESSING("PROCESSING", RefundStatus.ING.getCode()),
	NOTSURE("NOTSURE", RefundStatus.NOTSURE.getCode()),
	CHANGE("CHANGE", RefundStatus.CHANGE.getCode());
	
	private String type;
	private Integer status;
	
	private WxRefundStatus(String type, Integer status) {
		this.type = type;
		this.status = status;
	}
	
	public String getType() {
		return this.type;
	}
	
	public Integer getStatus() {
		return status;
	}

	public static WxRefundStatus getRefundStatus(String type) {
		for (WxRefundStatus refundStatus: WxRefundStatus.values()) {
			if (type.equals(refundStatus.getType())) {
				return refundStatus;
			}
		}
		
		return null;
	}

}
