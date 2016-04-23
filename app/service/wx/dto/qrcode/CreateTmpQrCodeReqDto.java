package service.wx.dto.qrcode;


public class CreateTmpQrCodeReqDto {
    //每个字段具体的意思请查看API文档
    private Integer expire_seconds = 2592000;  //默认为30天
    private String action_name = "QR_SCENE";   //QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
    private ActionInfo action_info  = null;
    
    public CreateTmpQrCodeReqDto(Integer expire_seconds, Integer scene_id){
    	this.expire_seconds = expire_seconds;
    	action_info = new ActionInfo(scene_id);
    }

	public Integer getExpire_seconds() {
		return expire_seconds;
	}

	public void setExpire_seconds(Integer expire_seconds) {
		this.expire_seconds = expire_seconds;
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
