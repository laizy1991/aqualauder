import service.wx.common.XMLParser;
import service.wx.dto.order.UnifiedOrderRspDto;


public class MainMethodTest {

	public static void main(String[] args) {
		String responseString = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wxcec16984044e8658]]></appid><mch_id><![CDATA[1326679501]]></mch_id><device_info><![CDATA[WEB]]></device_info>" + 
				"<nonce_str><![CDATA[wqJIk5shSXs74JY5]]></nonce_str><sign><![CDATA[A16FE3169A35FCE166D70E21FDCF53F0]]></sign><result_code><![CDATA[SUCCESS]]></result_code><prepay_id><![CDATA[wx20160410123613f52a98d7b60231346296]]></prepay_id><trade_type><![CDATA[JSAPI]]></trade_type></xml>";
		UnifiedOrderRspDto rsp = XMLParser.getObjectFromXML(responseString, UnifiedOrderRspDto.class);
		System.out.println(rsp.getPrepay_id());

	}

}
