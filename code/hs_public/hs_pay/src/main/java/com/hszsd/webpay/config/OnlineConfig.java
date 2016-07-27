package com.hszsd.webpay.config;

/**
 * 网银在线支付配置
 * @author xk
 *
 */
public class OnlineConfig {
	
	//后台调用成功
	public static String SUCCESS = "ok";
	
	//后台调用失败
	public static String FAIL = "error";

	//第三方支付返回支付结果 20:支付成功 30:支付失败
	public static String RESULT_SUCCESS = "20";
	public static String RESULT_FAIL = "30";

	//交易币种
	public static String MONEY_TYPE_CNY = "CNY";

	//支付请求参数中交易签名属性字段
	public static String[] reqVo = new String[]{
		"v_amount",     //订单总金额
		"v_moneytype",  //币种
		"v_oid",        //订单编号
		"v_mid",        //商户编号
		"v_url",        //URL地址
		"Md5key"           //秘钥
	};

	//支付返回参数中交易签名属性字段
	public static String[] resVo = new String[]{
		"v_oid",
		"v_pstatus",
		"v_amount",
		"v_moneytype",
		"Md5key"
	};

}
