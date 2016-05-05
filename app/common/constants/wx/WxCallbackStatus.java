package common.constants.wx;

public enum WxCallbackStatus {
	
	INIT(10, "初始状态"),
	CALLBACK_FAIL(20, "回调失败"),
	CALLBACK_SUCC(30, "回调成功");
	
	private Integer status;
	private String desc;
	
	private WxCallbackStatus(Integer status, String desc) {
		this.status = status;
		this.desc = desc;
	}

	public Integer getStatus() {
		return status;
	}

	public String getDesc() {
		return desc;
	}
	
	public static WxCallbackStatus getCallbackStatus(Integer status) {
		for (WxCallbackStatus cb: WxCallbackStatus.values()) {
			if (status == cb.getStatus()) {
				return cb;
			}
		}
		return null;
	}
}
