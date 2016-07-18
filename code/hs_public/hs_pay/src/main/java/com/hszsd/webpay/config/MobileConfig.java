package com.hszsd.webpay.config;

/**
 * 移动支付充值配置
 * @author xk
 *
 */
public class MobileConfig {
	
	//后台调用成功
	public static String SUCCESS = "SUCCESS";
	
	//后台调用失败
	public static String FAIL = "FAILED";

	//字符集 00:GBK 01:GB2312 02:UTF-8 默认00:GBK
	public static String CHARSET_UTF8 = "02";

	//商户请求交易流水号前缀
	public static String REQUESTID_PREFIX = "HSMT";

	//商户系统订单号前缀
	public static String ORDERID_PREFIX = "HSMO";

	//签名方式 RSA或MD5
	public static String SIGN_TYPE_RSA = "RSA";
	public static String SIGN_TYPE_MD5 = "MD5";

	//接口类型
	public static String TYPE = "GWDirectPay";

	//版本号
	public static String VERSION = "2.0.0";

	//现金支付方式 00:CNY
	public static String CURRENCY_CNY = "00";

	//日期格式
	public static String DATE_FORMAT = "yyyyMMdd";

	//有效期数量
	public static String PERIOD = "30";

	//有效期单位 00:分 01:小时 02:日 03:月
	public static String PERIOD_UNIT_MINUTE = "00";
	public static String PERIOD_UNIT_HOUR = "01";
	public static String PERIOD_UNIT_DAY = "02";
	public static String PERIOD_UNIT_MONTH = "03";

	//商品名称
	public static String PRODUCT_NAME = "HSZSD_CS";

	//支付请求参数中交易签名属性字段
	public static String[] reqVo = new String[]{
		"characterSet",
		"callbackUrl", 
		"notifyUrl",
		"ipAddress",	
		"merchantId", 
		"requestId", 
		"signType",	
		"type",
		"version",	
		"amount",
		"bankAbbr",	
		"currency",
		"orderDate", 
		"orderId", 
		"merAcDate", 	
		"period",
		"periodUnit",
		"merchantAbbr",
		"productDesc",
		"productId",
		"productName",
		"productNum",
		"reserved1",	
		"reserved2",	
		"userToken",
		"showUrl",	
		"couponsFlag"
	};

	//支付返回参数中交易签名属性字段
	public static String[] resVo = {
		"merchantId",
		"payNo",
		"returnCode",
		"message",
		"signType",
		"type",
		"version",
		"amount",
		"amtItem",
		"bankAbbr",
		"mobile",
		"orderId",
		"payDate",
		"accountDate",
		"reserved1",
		"reserved2",
		"status",
		"orderDate",
		"fee"
	};

}
