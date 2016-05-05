package service.wx.dto.qrcode.limit;



public class CreateLimitQrCodeReqDto {
    //每个字段具体的意思请查看API文档
    private String action_name = "QR_LIMIT_STR_SCENE";   //QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
    private LimitActionInfo action_info  = null;
    
    public CreateLimitQrCodeReqDto(String scene_str){
    	this.action_info = new LimitActionInfo(scene_str);
    }

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public LimitActionInfo getAction_info() {
		return action_info;
	}

	public void setAction_info(LimitActionInfo action_info) {
		this.action_info = action_info;
	}
}
