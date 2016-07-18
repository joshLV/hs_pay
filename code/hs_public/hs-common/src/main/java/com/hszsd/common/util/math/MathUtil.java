package com.hszsd.common.util.math;

import java.math.BigDecimal;

/**数学运算工具
 * @author hedayong
 *
 */
public class MathUtil {
	private static final int DEF_DIV_SCALE = 10; 
	 
	public static BigDecimal getMin(BigDecimal[] bigDecimals){
		BigDecimal result = bigDecimals[0];
		for (BigDecimal bigDecimal : bigDecimals) {
			if(result.compareTo(bigDecimal) > 0){
				result = bigDecimal;
			}
		}
		return result;
	}
	public static BigDecimal getMax(BigDecimal[] bigDecimals){
		BigDecimal result = new BigDecimal(0);
		for (BigDecimal bigDecimal : bigDecimals) {
			if(result.compareTo(bigDecimal) < 0){
				result = bigDecimal;
			}
		}
		return result;
	}
	
	 /** 
     * 提供精确的加法运算  
     * @param v1 被加数 
     * @param v2 加数 
     * @return 两个参数的和 
     */  
    public static BigDecimal add(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.add(b2);
    }  
      
    /** 
     * 提供精确的减法运算  
     * @param v1 被减数 
     * @param v2 减数 
     * @return 两个参数的差 
     */  
    public static double substract(double v1, double v2) {  
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.subtract(b2).doubleValue();  
    }  
      
    /** 
     * 提供精确的乘法运算
     * 如果无法识别需要转换的数字类型则默认返回-1
     * @param v1 被乘数 
     * @param v2 乘数 
     * @return 两个参数的积 
     */  
    public static <T> double multiply(T v1, T v2) {
        BigDecimal b1;
        BigDecimal b2;
        double result = -1;
        if(v1 instanceof String){
            b1 = new BigDecimal((String) v1);
            b2 = new BigDecimal((String) v2);
            result = b1.multiply(b2).doubleValue();
            return result;
        }
        if(v1 instanceof BigDecimal){
            b1 = (BigDecimal) v1;
            b2 = (BigDecimal) v2;
            result = b1.multiply(b2).doubleValue();
            return result;
        }
        if(v1 instanceof Double){
            b1 = new BigDecimal((Double) v1);
            b2 = new BigDecimal((Double) v2);
            result = b1.multiply(b2).doubleValue();
            return result;
        }

        return result;
    }  
  
    /** 
     * 提供（相对）精确的除法运算,当发生除不尽的情况时, 
     * 精确到小数点以后10位,以后的数字四舍五入. 
     * @param v1 被除数 
     * @param v2 除数 
     * @return 两个参数的商 
     */  
    public static double divide(double v1, double v2) {  
        return divide(v1, v2, DEF_DIV_SCALE);  
    }  
  
    /** 
     * 提供（相对）精确的除法运算. 
     * 当发生除不尽的情况时,由scale参数指 定精度,以后的数字四舍五入. 
     *  
     * @param v1 被除数 
     * @param v2 除数 
     * @param scale 表示需要精确到小数点以后几位 
     * @return 两个参数的商 
     */  
    public static double divide(double v1, double v2, int scale) {  
        if (scale < 0) {  
            throw new IllegalArgumentException("The scale must be a positive integer or zero");  
        }  
          
        BigDecimal b1 = new BigDecimal(Double.toString(v1));  
        BigDecimal b2 = new BigDecimal(Double.toString(v2));  
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
    }

    /**
     * 提供精确的小数位四舍五入处理
     * 如果无法识别需要转换的数字类型则默认返回-1
     * @param t 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static <T> double round(T t, int scale) {
        if(scale < 0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        double result = -1;
        BigDecimal src;
        BigDecimal one = new BigDecimal("1");

        if(t instanceof String){
            src = new BigDecimal((String) t);
            result = src.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return result;
        }
        if(t instanceof BigDecimal){
            src = (BigDecimal) t;
            result = src.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
            return result;
        }
        if(t instanceof Double) {
            src = new BigDecimal((Double) t);
            result = src.divide(one, scale, BigDecimal.ROUND_UP).doubleValue();
            return result;
        }
        return result;
    }
}
