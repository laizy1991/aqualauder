package common.constants.wx;

public enum QrCodeType {
	//0临时 1永久
	TMP(0, "临时"),
	LIMIT(1, "永久");
	
	private Integer type;
	private String desc;
	
	private QrCodeType(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
	
	public static QrCodeType getPayStatus(int type) {
		for (QrCodeType pay: QrCodeType.values()) {
			if (type == pay.getType()) {
				return pay;
			}
		}
		
		return null;
	}
}
