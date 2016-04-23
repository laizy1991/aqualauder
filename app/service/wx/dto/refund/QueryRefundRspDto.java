package service.wx.dto.refund;

public class QueryRefundRspDto {

    //协议层
    private String return_code = "";
    private String return_msg = "";

    //协议返回的具体数据（以下字段在return_code 为SUCCESS 的时候有返回）
    private String result_code = "";
    private String err_code = "";
    private String err_code_des = "";
    private String appid = "";
    private String mch_id = "";
    private String device_info = "";	//非必填
    private String nonce_str = "";
    private String sign = "";
    private String transaction_id;
    private String out_trade_no;
    private String refund_id;
    private String refund_channel;	//非必填 ORIGINAL—原路退款 BALANCE—退回到余额
    private Integer refund_fee;
    private Integer total_fee;
    private Integer cash_fee;
    private Integer cash_refund_fee;	//非必填
    
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
    
}
