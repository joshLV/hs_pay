package com.hszsd.webpay.config;

/**
 * 连连支付接口配置信息
 * Created by gzhengDu on 2016/7/5.
 */
public class LianLianConfig {

	// 银通公钥
	public static final String YT_PUB_KEY     = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCSS/DiwdCf/aZsxxcacDnooGph3d2JOj5GXWi+q3gznZauZjkNP8SKl3J2liP0O6rU/Y/29+IUe+GTMhMOFJuZm1htAtKiu5ekW0GlBMWxf4FPkYlQkPE0FtaoMP3gYfh+OwI+fIRrpW3ySn3mScnc6Z700nU/VYrRkfcSCbSnRwIDAQAB";

    // 商户私钥
    public static final String TRADER_PRI_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMlGNh/WsyZSYnQcHd9t5qUkhcOhuQmozrAY9DM4+7fhpbJenmYee4chREW4RB3m95+vsz9DqCq61/dIOoLK940/XmhKkuVjfPqHJpoyHJsHcMYy2bXCd2fI++rERdXtYm0Yj2lFbq1aEAckciutyVZcAIHQoZsFwF8l6oS6DmZRAgMBAAECgYAApq1+JN+nfBS9c2nVUzGvzxJvs5I5qcYhY7NGhySpT52NmijBA9A6e60Q3Ku7vQeICLV3uuxMVxZjwmQOEEIEvXqauyYUYTPgqGGcwYXQFVI7raHa0fNMfVWLMHgtTScoKVXRoU3re6HaXB2z5nUR//NE2OLdGCv0ApaJWEJMwQJBAPWoD/Cm/2LpZdfh7oXkCH+JQ9LoSWGpBDEKkTTzIqU9USNHOKjth9vWagsR55aAn2ImG+EPS+wa9xFTVDk/+WUCQQDRv8B/lYZD43KPi8AJuQxUzibDhpzqUrAcu5Xr3KMvcM4Us7QVzXqP7sFc7FJjZSTWgn3mQqJg1X0pqpdkQSB9AkBFs2jKbGe8BeM6rMVDwh7TKPxQhE4F4rHoxEnND0t+PPafnt6pt7O7oYu3Fl5yao5Oh+eTJQbyt/fwN4eHMuqtAkBx/ob+UCNyjhDbFxa9sgaTqJ7EsUpix6HTW9f1IirGQ8ac1bXQC6bKxvXsLLvyLSxCMRV/qUNa4Wxu0roI0KR5AkAZqsY48Uf/XsacJqRgIvwODstC03fgbml890R0LIdhnwAvE4sGnC9LKySRKmEMo8PuDhI0dTzaV0AbvXnsfDfp";

    // 签名方式 RSA或MD5
    public static final String SIGN_TYPE_RSA = "RSA";
    public static final String SIGN_TYPE_MD5 = "MD5";

    // 接口版本号，固定1.0
    public static final String VERSION        = "1.0";

    // 业务类型，连连支付根据商户业务为商户开设的业务类型； （101001：虚拟商品销售、109001：实物商品销售、108001：外部账户充值）
    public static final String BUSI_PARTNER_VIRTUAL   = "101001";

    //订单日期格式
    public static final String ORDER_DATE_FORMAT = "yyyyMMddHHmmss";

    //商品名称
    public static final String NAME_GOODS = "ZSD";

    //订单有效时间10080分钟（7天）
    public static final String ORDER_VALIDITY_PERIOD = "10080";

    //第三方支付返回支付结果 SUCCESS:成功
    public static final String RESULT_SUCCESS = "SUCCESS";

    // 后台调用成功
    public static final LianLianRes SUCCESS = LianLianRes.SUCCESS;

    // 后台调用失败
    public static final LianLianRes FAIL = LianLianRes.FAIL;

    /**
     * 连连支付响应对象
     */
    enum LianLianRes{
        SUCCESS("0000", "交易成功"), FAIL("9999", "交易失败");

        String ret_code;
        String ret_msg;

        LianLianRes(String ret_code, String ret_msg) {
            this.ret_code = ret_code;
            this.ret_msg = ret_msg;
        }

        public String getRet_code() {
            return ret_code;
        }

        public String getRet_msg() {
            return ret_msg;
        }
    }
}

