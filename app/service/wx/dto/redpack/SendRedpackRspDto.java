package service.wx.dto.redpack;

public class SendRedpackRspDto {

    //协议层
    private String return_code = "";
    private String return_msg = "";

    //协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    private String sign = "";
    private String result_code = "";
    private String err_code = "";
    private String err_code_des = "";
    
    //以下字段在return_code 和result_code都为SUCCESS的时候有返回
    private String mch_billno = "";
    private String mch_id = "";
    private String detail_id = "";
    private String status = "";
    private String send_type = "";
    private String hb_type = "";
    private Integer total_num = 0;
    private String reason = "";
    private String send_time = "";
    private String refund_time = "";
    private Integer refund_amount = 0;
    private String wishing = "";
    private String remark = "";
    private String act_name = "";
    private String hblist = "";
    private String openid = "";
    private Integer amount = 0;
    private String rcv_time = "";
    
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}
	public String getErr_code() {
		return err_code;
	}
	public void setErr_code(String err_code) {
		this.err_code = err_code;
	}
	public String getErr_code_des() {
		return err_code_des;
	}
	public void setErr_code_des(String err_code_des) {
		this.err_code_des = err_code_des;
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
	public String getDetail_id() {
		return detail_id;
	}
	public void setDetail_id(String detail_id) {
		this.detail_id = detail_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSend_type() {
		return send_type;
	}
	public void setSend_type(String send_type) {
		this.send_type = send_type;
	}
	public String getHb_type() {
		return hb_type;
	}
	public void setHb_type(String hb_type) {
		this.hb_type = hb_type;
	}
	public Integer getTotal_num() {
		return total_num;
	}
	public void setTotal_num(Integer total_num) {
		this.total_num = total_num;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getSend_time() {
		return send_time;
	}
	public void setSend_time(String send_time) {
		this.send_time = send_time;
	}
	public String getRefund_time() {
		return refund_time;
	}
	public void setRefund_time(String refund_time) {
		this.refund_time = refund_time;
	}
	public Integer getRefund_amount() {
		return refund_amount;
	}
	public void setRefund_amount(Integer refund_amount) {
		this.refund_amount = refund_amount;
	}
	public String getWishing() {
		return wishing;
	}
	public void setWishing(String wishing) {
		this.wishing = wishing;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAct_name() {
		return act_name;
	}
	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}
	public String getHblist() {
		return hblist;
	}
	public void setHblist(String hblist) {
		this.hblist = hblist;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getRcv_time() {
		return rcv_time;
	}
	public void setRcv_time(String rcv_time) {
		this.rcv_time = rcv_time;
	}
}
