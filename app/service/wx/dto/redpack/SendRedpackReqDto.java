package service.wx.dto.redpack;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import play.Play;
import service.wx.common.Configure;
import service.wx.common.RandomStringGenerator;
import service.wx.common.Signature;
import service.wx.service.BaseService;

public class SendRedpackReqDto {
	private String mch_billno = "";
    private String mch_id = "";
    private String wxappid = "";
    private String send_name = "";	//商户名称
    private String re_openid = "";
    private Integer total_amount = 0;
    private Integer total_num = 0;
    private String wishing = "";
    private String client_ip = "";
    private String act_name = "";
    private String remark = "";
    private String nonce_str = "";
    private String sign = "";
    
    public SendRedpackReqDto(String mch_billno, String re_openid, Integer total_amount) {
    	setWxappid(Configure.getAppid());
    	setMch_id(Configure.getMchid());
    	
    	String sendName = Play.configuration.getProperty("wx.redpack.sendName", "安琪儿");
    	int totalNum = 1;
    	String wishing = Play.configuration.getProperty("wx.redpack.wishing", "感谢您进行提现！");
    	String clientIp = Play.configuration.getProperty("local.host.ip", "127.0.0.1");	
    	String actName = Play.configuration.getProperty("wx.redpack.actName", "微信提现活动");
    	String remark = "感谢您的参与！";
    	
    	setMch_billno(mch_billno);
    	setSend_name(sendName);
    	setRe_openid(re_openid);
    	setTotal_amount(total_amount);
    	setTotal_num(totalNum);
    	setWishing(wishing);
    	setClient_ip(clientIp);
    	setAct_name(actName);
    	setRemark(remark);
    	
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
    
	public String getMch_billno() {
		return mch_billno;
	}
	public void setMch_billno(String mch_billno) {
		this.mch_billno = mch_billno;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}
	public String getWxappid() {
		return wxappid;
	}
	public void setWxappid(String wxappid) {
		this.wxappid = wxappid;
	}
	public String getSend_name() {
		return send_name;
	}
	public void setSend_name(String send_name) {
		this.send_name = send_name;
	}
	public String getRe_openid() {
		return re_openid;
	}
	public void setRe_openid(String re_openid) {
		this.re_openid = re_openid;
	}
	public Integer getTotal_amount() {
		return total_amount;
	}
	public void setTotal_amount(Integer total_amount) {
		this.total_amount = total_amount;
	}
	public Integer getTotal_num() {
		return total_num;
	}
	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}
	public String getWishing() {
		return wishing;
	}
	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	public String getClient_ip() {
		return client_ip;
	}
	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}
	public String getAct_name() {
		return act_name;
	}
	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
}
