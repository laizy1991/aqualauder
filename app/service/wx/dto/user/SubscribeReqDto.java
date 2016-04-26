package service.wx.dto.user;

public class SubscribeReqDto {
	private String ToUserName = ""; 	//开发者微信号
	private String FromUserName	= ""; 	//发送方帐号（一个OpenID）
	private Integer CreateTime	= 0; 	//消息创建时间 （整型）
	private String MsgType	= ""; 	//消息类型，event
	private String Event	= ""; 	//事件类型，subscribe
	private String EventKey	= ""; 	//事件KEY值，qrscene_为前缀，后面为二维码的参数值
	private String Ticket	= ""; 	//二维码的ticket，可用来换取二维码图片
	
	public String getToUserName() {
		return ToUserName;
	}
	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}
	public String getFromUserName() {
		return FromUserName;
	}
	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}
	public Integer getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(Integer createTime) {
		CreateTime = createTime;
	}
	public String getMsgType() {
		return MsgType;
	}
	public void setMsgType(String msgType) {
		MsgType = msgType;
	}
	public String getEvent() {
		return Event;
	}
	public void setEvent(String event) {
		Event = event;
	}
	public String getEventKey() {
		return EventKey;
	}
	public void setEventKey(String eventKey) {
		EventKey = eventKey;
	}
	public String getTicket() {
		return Ticket;
	}
	public void setTicket(String ticket) {
		Ticket = ticket;
	}
}
