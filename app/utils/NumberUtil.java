package utils;

import java.util.Random;

public class NumberUtil {
	
	/**
	 * 10位数字随机串
	 * @param strLength
	 * @return
	 */
    public static String getRandomNumberString(int strLength) {
        
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }
}
