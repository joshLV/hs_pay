package com.hszsd.webpay.config;

/**
 * 汇潮支付接口配置信息
 * Created by gzhengDu on 2016/7/5.
 */
public class HuichaoConfig {

	// 后台调用成功
	public static String SUCCESS = "ok";

	// 后台调用失败
	public static String FAIL = "fail";

	//第三方支付返回支付结果 88：成功
	public static String RESULT_SUCCESS = "88";

	//订单日期格式
	public static String ORDER_DATE_FORMAT = "yyyyMMddHHmmss";



	public static String[] reqVo = new String[] {
			"MerNo", // 商户号
			"BillNo", // 订单号
			"Amount", // 金额
			"ReturnURL", // 同步通知页面
			"MD5Key" // 密钥
	};

	public static String[] resVo = new String[] {
			"BillNo", // 订单号
			"Amount", // 金额
			"Succeed", // 状态码
			"MD5Key", // 密钥
	};
}
