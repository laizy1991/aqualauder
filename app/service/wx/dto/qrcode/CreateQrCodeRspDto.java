package service.wx.dto.qrcode;

public class CreateQrCodeRspDto {

	private boolean success = false;
    private String ticket = "";
    private Integer expire_seconds = 0;
    private String url = "";
    private String picRelPath = "";
    
    private Integer errcode = null;
    private String errmsg = "";
    
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public Integer getExpire_seconds() {
		return expire_seconds;
	}
	public void setExpire_seconds(Integer expire_seconds) {
		this.expire_seconds = expire_seconds;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPicRelPath() {
		return picRelPath;
	}
	public void setPicRelPath(String picRelPath) {
		this.picRelPath = picRelPath;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
