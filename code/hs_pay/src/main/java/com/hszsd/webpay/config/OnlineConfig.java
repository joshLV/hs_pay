package com.hszsd.webpay.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 网银在线支付配置
 * @author xk
 *
 */
public class OnlineConfig {
	
	//后台调用成功
	public static final String SUCCESS = "ok";
	
	//后台调用失败
	public static final String FAIL = "error";

	//第三方支付返回支付结果 20:支付成功 30:支付失败
	public static final String RESULT_SUCCESS = "20";
	public static final String RESULT_FAIL = "30";

	//交易币种
	public static final String MONEY_TYPE_CNY = "CNY";

	//支付请求参数中交易签名属性字段
	public static final List<String> reqVo = Collections.unmodifiableList(Arrays.asList(
		"v_amount",     //订单总金额
		"v_moneytype",  //币种
		"v_oid",        //订单编号
		"v_mid",        //商户编号
		"v_url",        //URL地址
		"MD5Key"           //秘钥
	));

	//支付返回参数中交易签名属性字段
	public static final List<String> resVo = Collections.unmodifiableList(Arrays.asList(
		"v_oid",
		"v_pstatus",
		"v_amount",
		"v_moneytype",
		"MD5Key"
	));

}
