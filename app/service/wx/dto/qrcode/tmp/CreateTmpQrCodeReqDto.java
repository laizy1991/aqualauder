package service.wx.dto.qrcode.tmp;



public class CreateTmpQrCodeReqDto {
    //每个字段具体的意思请查看API文档
    private Integer expire_seconds = 0;  
    private String action_name = "QR_SCENE";   //QR_SCENE为临时,QR_LIMIT_SCENE为永久,QR_LIMIT_STR_SCENE为永久的字符串参数值
    private TmpActionInfo action_info  = null;
    
    /**
     * 创建临时二维码请求req
     * @param expire_seconds 过期时间，以秒为单位，不填，则默认有效期为30秒。
     * @param scene_id 场景ID，临时二维码用户userID代替(32位非0整型)
     */
    public CreateTmpQrCodeReqDto(Integer expire_seconds, Integer scene_id) {
    	this.expire_seconds = expire_seconds;
    	this.action_info = new TmpActionInfo(scene_id);
    }
    
    public CreateTmpQrCodeReqDto(Integer scene_id) {
    	this.expire_seconds = 2592000;	//默认为30天
    	this.action_info = new TmpActionInfo(scene_id);
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

	public TmpActionInfo getAction_info() {
		return action_info;
	}

	public void setAction_info(TmpActionInfo action_info) {
		this.action_info = action_info;
	}
}
