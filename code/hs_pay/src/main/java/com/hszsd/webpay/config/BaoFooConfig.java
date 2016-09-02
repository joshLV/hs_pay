package com.hszsd.webpay.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 宝付支付接口配置信息
 * Created by gzhengDu on 2016/7/5.
 */
public class BaoFooConfig {

	//后台调用成功
	public static final String SUCCESS = "ok";
	
	//后台调用失败
	public static final String FAIL = "no";

	//第三方支付返回支付结果 1:成功 0:失败
	public static final String RESULT_SUCCESS = "1";
	public static final String RESULT_FAIL = "0";

	//第三方支付返回订单结果
	public static final String RESULT_DESC_SUCCESS = "01";

	//终端ID
	public static final String TERMINAL_ID = "14673";

	//接口版本号
	public static final String INTERFACE_VERSION = "4.0";

	//加密类型：固定数值 1
	public static final String KEY_TYPE = "1";

	//通知类型：固定数值 1
	public static final String NOTICE_TYPE = "1";

	//订单日期格式
	public static final String TRADE_DATE_FORMAT = "yyyyMMddHHmmss";

	//支付请求参数中交易签名属性字段
	public static final List<String> reqVo = Collections.unmodifiableList(Arrays.asList(
			"MemberID",
			"PayID",
			"TradeDate",
			"TransID",
			"OrderMoney",
			"PageUrl",
			"ReturnUrl",
			"NoticeType",
			"MD5Key"
	));
	//支付返回参数中交易签名属性字段
	public static final List<String> resVo = Collections.unmodifiableList(Arrays.asList(
			"MemberID",
			"TerminalID",
			"TransID",
			"Result",
			"ResultDesc",
			"FactMoney",
			"AdditionalInfo",
			"SuccTime",
			"Md5Sign"
	));
}
