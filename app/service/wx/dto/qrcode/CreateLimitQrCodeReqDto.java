package service.wx.dto.qrcode;


public class CreateLimitQrCodeReqDto {
    //每个字段具体的意思请查看API文档
    private String action_name = "QR_LIMIT_SCENE";   //QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
    private ActionInfo action_info  = null;
    
    public CreateLimitQrCodeReqDto(Integer expire_seconds, Integer scene_id){
    	action_info = new ActionInfo(scene_id);
    }

	public String getAction_name() {
		return action_name;
	}

	public void setAction_name(String action_name) {
		this.action_name = action_name;
	}

	public ActionInfo getAction_info() {
		return action_info;
	}

	public void setAction_info(ActionInfo action_info) {
		this.action_info = action_info;
	}
}
