package com.hszsd.webpay.config;

/**
 * 易宝支付接口配置信息
 * Created by suocy on 2016/8/5.
 */
public class YeePayConfig {
	//后台调用成功
	public static final String SUCCESS = "SUCCESS";

	//后台调用失败
	public static final String FAIL = "FAIL";

	//易宝支付返回支付结果 1:成功 0:失败
	public static final String RESULT_SUCCESS = "1";

	/**
	 * 投资通生产环境下的正式商户编号
	 */
	public static final String MERCHANT_ACCOUNT ="10012471057";

	/**
	 * 投资通测试商户编号
	 */
	public static final String TEST_MERCHANT_ACCOUNT ="10000419568";

	/**
	 * 商户公钥
	 */
	public static final String MERCHANT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDV148eXp4adXn6TxBn82B1upGy8f/jDpD2JwUxdi18ZcqbXGwBgjY/HuQg9azuE/bYlCqLw5eN8uTU+lkA/iM5KR+jALE5Tk8zKKruin75uyrfiqUdYJppc3xK2F1af2mQJ+zyzsHArLyFVaAkb08x6ILhVA8CjDZfjlQ/7JpI0wIDAQAB";

	/**
	 * 测试商户公钥
	 */
	public static final String TEST_MERCHANT_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKxayKB/TqDXtcKaObOPPzVL3r++ghEP45nai9cjG0JQt9m0F5+F8RVygizxS83iBTHd5bJbrMPLDh3GvjGm1bbJhzO4m2bF2fQm2uJ0C3ckdm9AZK8fqzcncpu2dy1zFyucFyHhKIgZryqfW5PS3G9UohS4698qA5j4dceWf5PwIDAQAB";

	/**
	 * 商户私钥
	 */
	public static final String MERCHANT_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANXXjx5enhp1efpPEGfzYHW6kbLx/+MOkPYnBTF2LXxlyptcbAGCNj8e5CD1rO4T9tiUKovDl43y5NT6WQD+IzkpH6MAsTlOTzMoqu6Kfvm7Kt+KpR1gmmlzfErYXVp/aZAn7PLOwcCsvIVVoCRvTzHoguFUDwKMNl+OVD/smkjTAgMBAAECgYEAi2KsWSFXcOuuDGhIy1kqQNLRcZCoOHogJzKcAfeEApffDpGZnQohDde2330DsVO2DMnXSa6+NLpkSia8qRb+IWwTlS8uR9M9iPb7c+TLE+Bw0sDGDHGl847+asFjHXglnBmt8emM1399lX0Defg1EycAqFxe4R7Db/vSKGSLAFECQQD9ykB1zUZA+iC+8gyUydu5jvdztKLraOBEcvR0zAMe93DvT5Z1YP6qZ18Ce0FSGWs5jSgIJmjkoO/UYX16o4xFAkEA17RBVoExty8NbIModR7QY/uCJ5gyf4jhKgnj6nRIe6upymtDcjUi055KtZyhE1DRxQbFaGQRFMHNdUIRHUPuNwJBAOY82PPGEWkbQh/YOR+2bRbPI6CdjHvifiq62xkmh+JM+vCwFEBSiVaQRvpaVS0lEEh6zYlvRxO7CTh0ZUCgJL0CQCQOepmGMRRI5jqMVhf11anQ4fnSZCaTb+gCDTokw/UARfqTIYwWu8KaBC0FiJtc+COyNQ91IrHQj5KZP5kfwNMCQFoDd3tv3+IboGrbGoW4lRdlv4T8lnkm5F9EHEIXMnqt/ke8XZkl/VBenDxhZU6j5In1mDmKhrcSNzYPY5fYP2Y=";

	/**
	 * 测试商户私钥
	 */
	public static final String TEST_MERCHANT_PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIrFrIoH9OoNe1wpo5s48/NUvev76CEQ/jmdqL1yMbQlC32bQXn4XxFXKCLPFLzeIFMd3lslusw8sOHca+MabVtsmHM7ibZsXZ9Cba4nQLdyR2b0Bkrx+rNydym7Z3LXMXK5wXIeEoiBmvKp9bk9Lcb1SiFLjr3yoDmPh1x5Z/k/AgMBAAECgYEAgAjVohypOPDraiL40hP/7/e1qu6mQyvcgugVcYTUmvK64U7HYHNpsyQI4eTRq1f91vHt34a2DA3K3Phzifst/RoonlMmugXg/Klr5nOXNBZhVO6i5XQ3945dUeEq7LhiJTTv0cokiCmezgdmrW8n1STZ/b5y5MIOut8Y1rwOkAECQQC+an4ako+nPNw72kM6osRT/qC589AyOav60F1bHonK6NWzWOMiFekGuvtpybgwt4jbpQxXXRPxvJkgBq873fwBAkEAupGaEcuqXtO2j0hJFOG5t+nwwnOaJF49LypboN0RX5v8nop301//P16Bs/irj5F/mAs9lFR4GZ3bxL8zs5r1PwJBALa1MDMHFlf+CcRUddW5gHCoDkjfLZJDzEVp0WoxLz5Hk2X3kFmQdHxExiCHsfjs4qD/CYx6fzyhHrygLVxgcAECQAT8z3maUDuovUCnVgzQ2/4mquEH5h8Cxe/02e46+rPrn509ZmaoMlKnXCBLjYqRATA3XLYSbAODTNS9p8wtYFECQHa/xgB+nYWoevPC/geObOLAP9HMdNVcIAJq2rgeXVI4P7cFXvksRborHmjuy1fltoR0003qlSg82mxzABbzYUs=";

	/**
	 * 易宝公玥
	 */
	public static final String YEEPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDY5GAVtaFKgi8r9Je8AA6fiIeb64xQYDXzQrMu/ctlybvFuY92kriynVnvX0ga+5zI1ZU78qoW/frlbD6VTSyvwk0EzTbNuWwKSKV+wk+o+rSex5dmlfU2cIX9qjOZ1o2gaZTUwecAvcT2WC2nrjfP9Q2DH6cDbNxvMdxb083DbwIDAQAB";

	/**
	 * 测试易宝公钥
	 */
	public static final String TEST_YEEPAY_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCj4k0oTc05UzrvBB6g24wsKawTlIX5995q3CQYrgM5un9mKEQc/NQIsJqqG2RUHyXUIBogMaMqG1F1QPoKMaXeVfVUSYa8ZU7bV9rOMDUT20BxAmPbtLlWdTSXDxXKXQxwkyfUAih1ZgTLI3vYg3flHeUA6cZRdbwDPLqXle8SIwIDAQAB";


	/**
	 * 绑卡请求地址
	 */
	public static final String BIND_BANKCARD_URL = "https://ok.yeepay.com/payapi/api/tzt/invokebindbankcard";
	/**
	 * 绑卡确认请求地址
	 */
	public static final String CONFIRM_BIND_BANKCARD_URL = "https://ok.yeepay.com/payapi/api/tzt/confirmbindbankcard";
	/**
	 * 发送短验-支付请求地址
	 */
	public static final String PAY_NEED_SMS_URL = "https://ok.yeepay.com/payapi/api/tzt/pay/bind/reuqest";
	/**
	 * 发送短信验证码接口请求地址
	 */
	public static final String SMS_SEND_URL = "https://ok.yeepay.com/payapi/api/tzt/pay/validatecode/send";
	/**
	 * 确认支付请求地址
	 */
	public static final String SMS_CONFIRM_URL = "https://ok.yeepay.com/payapi/api/tzt/pay/confirm/validatecode";
	/**
	 * 支付接口--不发送短验请求地址
	 */
	public static final String DIRECT_BIND_PAY_URL = "https://ok.yeepay.com/payapi/api/tzt/directbindpay";

	/**
	 * 解绑请求地址
	 */
	public static final String UNBIND_URL = "https://ok.yeepay.com/payapi/api/bankcard/unbind";

	public static final String QUERY_AUTH_BIND_URL = "https://ok.yeepay.com/payapi/api/bankcard/authbind/list";

}
