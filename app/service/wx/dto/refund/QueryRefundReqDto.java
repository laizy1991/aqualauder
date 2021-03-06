package service.wx.dto.refund;

import service.wx.common.Configure;
import service.wx.common.RandomStringGenerator;
import service.wx.common.Signature;
import service.wx.service.BaseService;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class QueryRefundReqDto {
    //每个字段具体的意思请查看API文档
    private String appid = "";
    private String mch_id = "";
    private String device_info = "";
    private String nonce_str = "";
    private String sign = "";
    private String transaction_id = "";	//以下四选一，暂选微信订单号
    //微信订单号		transaction_id	String(32)		微信订单号
    //商户订单号		out_trade_no	String(32)		商户系统内部的订单号
    //商户退款单号	out_refund_no	String(32)		商户侧传给微信的退款单号
    //微信退款单号	refund_id		String(28)		微信生成的退款单号，在申请退款接口有返回
    
    /**
     * 请求退款查询服务
     * @param device_info 微信支付分配的终端设备号，与下单一致
     */
    public QueryRefundReqDto(String transaction_id) {

        //微信分配的公众号ID（开通公众号之后可以获取到）
        setAppid(Configure.getAppid());
        
        //微信支付分配的商户号ID（开通公众号的微信支付功能之后可以获取到）
        setMch_id(Configure.getMchid());
        setDevice_info(device_info);
        setTransaction_id(transaction_id);
        
        //随机字符串，不长于32 位
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

        //根据API给的签名规则进行签名
        String sign = Signature.getSign(toMap());
        setSign(sign);//把签名数据设置到Sign这个属性中
    }
    
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}
}
