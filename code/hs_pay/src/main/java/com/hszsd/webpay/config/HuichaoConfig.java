package com.hszsd.webpay.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 汇潮支付接口配置信息
 * Created by gzhengDu on 2016/7/5.
 */
public class HuichaoConfig {

	// 后台调用成功
	public static final String SUCCESS = "ok";

	// 后台调用失败
	public static final String FAIL = "fail";

	//第三方支付返回支付结果 88：成功
	public static final String RESULT_SUCCESS = "88";

	//订单日期格式
	public static final String ORDER_DATE_FORMAT = "yyyyMMddHHmmss";

	//支付请求参数中交易签名属性字段
	public static final List<String> reqVo = Collections.unmodifiableList(Arrays.asList(
			"MerNo", // 商户号
			"BillNo", // 订单号
			"Amount", // 金额
			"ReturnURL", // 同步通知页面
			"MD5Key" // 密钥
	));

	//支付返回参数中交易签名属性字段
	public static final List<String> resVo = Collections.unmodifiableList(Arrays.asList(
			"BillNo", // 订单号
			"Amount", // 金额
			"Succeed", // 状态码
			"MD5Key" // 密钥
	));
}
