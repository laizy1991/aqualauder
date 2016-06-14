package service.wx.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import play.Play;

public class Configure {
//这个就是自己要保管好的私有Key了（切记只能放在自己的后台代码里，不能放在任何可能被看到源代码的客户端程序中）
	// 每次自己Post数据给API的时候都要用这个key来对所有字段进行签名，生成的签名会放在Sign这个字段，API收到Post数据的时候也会用同样的签名算法对Post过来的数据进行签名和验证
	// 收到API的返回的时候也要用这个key来对返回的数据算下签名，跟API的Sign数据进行比较，如果值不一致，有可能数据被第三方给篡改

	public static String getWxServertoken() {
		return wxServertoken;
	}

	public static void setWxServertoken(String wxServertoken) {
		Configure.wxServertoken = wxServertoken;
	}

	private static String key = "";

	//微信分配的公众号ID（开通公众号之后可以获取到）
	private static String appID = "";

	//微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
	private static String mchID = "";

	//受理模式下给子商户分配的子商户号
	private static String subMchID = "";

	//HTTPS证书的本地路径
	private static String certLocalPath = "";

	//HTTPS证书密码，默认密码等于商户号MCHID
	private static String certPwdPath = null;

	//是否使用异步线程的方式来上报API测速，默认为异步模式
	private static boolean useThreadToDoReport = true;
	
	private static String wxServertoken = "";

	//机器IP
	private static String ip = "";
	
	static {
		key = Play.configuration.getProperty("wx.config.key", "726Ujis98wJ93S8hv634Hj934f92424j");
		appID = Play.configuration.getProperty("wx.config.appid", "wxcec16984044e8658");
		mchID = Play.configuration.getProperty("wx.config.mchid", "1326679501");
		certLocalPath = Play.configuration.getProperty("wx.config.sslcert.path");
		certPwdPath = Play.configuration.getProperty("wx.config.sslkey.path");
		wxServertoken = Play.configuration.getProperty("wx.config.token");
	}

	//以下是API的路径：
	//1) 统一下单API
	public static String UNIFIED_ORDER_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	//2) 发送现金红包API
	public static String SEND_REDPACK_API = "https://api.mch.weixin.qq.com/mmpaymkttransfers/sendredpack";
	//3) 查询现金红包API
	public static String QUERY_REDPACK_API = "https://api.mch.weixin.qq.com/mmpaymkttransfers/gethbinfo";
	//4) 查询订单
	public static String ORDER_QUERY_API = "https://api.mch.weixin.qq.com/pay/orderquery";
	//5) 创建二维码
	public static String CREATE_QRCODE_API = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=%s";
	//6) 下载二维码
	public static String DOWNLOAD_QRCODE_API = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=%s";
	//7) 请求退款
	public static String SEND_REFUND_API = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	//8) 查询退款
	public static String QUERY_REFUND_API = "https://api.mch.weixin.qq.com/pay/refundquery";
	//9) 创建二维码
	public static String SEND_TPL_MSG_API = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

	public static boolean isUseThreadToDoReport() {
		return useThreadToDoReport;
	}

	public static void setUseThreadToDoReport(boolean useThreadToDoReport) {
		Configure.useThreadToDoReport = useThreadToDoReport;
	}

	//HTTPS请求器
	public static String HttpsRequestClassName = "service.wx.common.HttpsRequest";

	public static void setKey(String key) {
		Configure.key = key;
	}

	public static void setAppID(String appID) {
		Configure.appID = appID;
	}

	public static void setMchID(String mchID) {
		Configure.mchID = mchID;
	}

	public static void setSubMchID(String subMchID) {
		Configure.subMchID = subMchID;
	}

	public static void setCertLocalPath(String certLocalPath) {
		Configure.certLocalPath = certLocalPath;
	}

	public static void setIp(String ip) {
		Configure.ip = ip;
	}

	public static String getKey(){
		return key;
	}
	
	public static String getAppid(){
		return appID;
	}
	
	public static String getMchid(){
		return mchID;
	}

	public static String getSubMchid(){
		return subMchID;
	}
	
	public static String getCertLocalPath(){
		return certLocalPath;
	}
	
	public static String getCertPwdPath(){
		return certPwdPath;
	}

	public static String getIP(){
		return ip;
	}

	public static void setHttpsRequestClassName(String name){
		HttpsRequestClassName = name;
	}

}
