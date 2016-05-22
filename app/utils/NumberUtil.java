package utils;

import java.math.BigDecimal;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {
	
	/**
	 * 10位数字随机串
	 * @param strLength
	 * @return
	 */
    public static String getRandomNumberString(int strLength) {
        
        Random rm = new Random();
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(rm.nextDouble());
        // 返回固定的长度的随机数
        return fixLenthString.substring(2, strLength + 2);
    }

    public static String fen2yuan(Long fen){
        if( fen == null ){
            return "0.00";
        }
        final int MULTIPLIER = 100;  
        Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");  
        Matcher matcher = pattern.matcher(fen.toString());  
        
        if (matcher.matches()) {  
            return  new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();  
        } else {  
            return "0.00";  
        }  
    }
}
