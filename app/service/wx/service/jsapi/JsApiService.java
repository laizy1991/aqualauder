package service.wx.service.jsapi;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import service.wx.common.Configure;
import service.wx.dto.jspai.JsapiConfig;
import utils.WxJsApiUtil;

public class JsApiService {

    public static JsapiConfig getSign(String url) {
    	if(StringUtils.isBlank(url)) {
    		return null;
    	}
    	
    	String jsapiTicket = WxJsApiUtil.getJsApiTicket();
    	
        String nonceStr = createNonceStr();
        String timestamp = createTimestamp();
        String signStr;
        String signature = "";

        //注意这里参数名必须全部小写，且必须有序
        /****
         * 对 jsapi_ticket、 timestamp 和 nonce 按字典排序 对所有待签名参数按照字段名的 ASCII
         * 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
         * string1。这里需要注意的是所有参数名均为小写字符。 接下来对 string1 作 sha1 加密，字段名和字段值都采用原始值，不进行
         * URL 转义。即 signature=sha1(string1)。
         * **如果没有按照生成的key1=value&key2=value拼接的话会报错
         */
        String[] paramArr = new String[] { "jsapi_ticket=" + jsapiTicket,
                "timestamp=" + timestamp, "noncestr=" + nonceStr, "url=" + url};
        Arrays.sort(paramArr);
        // 将排序后的结果拼接成一个字符串
        signStr = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2])
                .concat("&"+paramArr[3]);
        
        Logger.info("微信jsApi_ticket未签名之前入参为: %s", signStr);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(signStr.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (Exception e) {
        	Logger.error("微信jsApi_ticket签名时发生错误");
            e.printStackTrace();
            signature = null;
        }
        if(null == signature) {
        	return null;
        }
        Logger.info("签名字符串为: %s", signature);

        JsapiConfig config = new JsapiConfig();
        config.setAppId(Configure.getAppid());
        config.setUrl(url);
        config.setTimestamp(timestamp);
        config.setNonceStr(nonceStr);
        config.setSignature(signature);
        
        return config;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    private static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
}
