package service;

import java.text.SimpleDateFormat;
import java.util.Date;

import utils.IdGenerator;

/**
 * 商户交易订单编号
 * 编号格式：YYYYMMddHHmm{IdGenerator}
 *
 */
public class OutTradeNo {
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static String getOutTradeNo() {
		StringBuilder sb = new StringBuilder();
		sb.append(sdf.format(new Date()));
		sb.append(IdGenerator.getRandNumByLength(4));
		return sb.toString();
	}
	
}
