package common.constants;

import utils.StringUtil;

/**
 * 业务操作信息类，根据相应的提示key，在messages文件中取得相应的提示语
 * 其中的msgCode会返回给调用端作为结果码
 */
public enum MessageCode {


	//订单模块   
	GET_ORDER_FAILED("get.order.failed", "查询订单失败"),	
	
	ADD_MECHANT_TRADE_ORDER_FAILED("add.mechant.trade.order.failed", "添加商户外部订单失败"),
	
	//用户模块
	GET_USER_FAILED("get.user.failed", "获取用户信息失败"),
	
	/*
	 * 权限相关的提示语
	 * */
	PERMISSION_DENY("permission.deny", "没有权限"),	
	HANDLE_FAILED("handle.failed", "操作失败"),
	
	SUCCESS("success", "操作成功"),
	INVALID_PARAM("invalid.param", "参数无效");
	
	private String key;
	
	private String msg;
	
	private MessageCode(String key, String msg) {
		this.key = key;
		this.msg = msg;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public static MessageCode getMessageCode(String key) {
		if(StringUtil.isNullOrEmpty(key)) {
			return null;
		}
		for(MessageCode msgCode : MessageCode.values()) {
			if(msgCode.getKey().equals(key)) {
				return msgCode;
			}
		}
		return null;
	}
}
