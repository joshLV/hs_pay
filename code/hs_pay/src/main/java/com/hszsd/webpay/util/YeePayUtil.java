package com.hszsd.webpay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hszsd.webpay.config.YeePayConfig;
import com.hszsd.webpay.util.YeePay.AES;
import com.hszsd.webpay.util.YeePay.EncryUtil;
import com.hszsd.webpay.util.YeePay.RSA;
import com.hszsd.webpay.util.YeePay.RandomUtil;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 投资通接口范例--API版
 *
 * @author    ：suocy
 * @since    ：2016-08-05
 */

public class YeePayUtil{

	private static final Logger logger = LoggerFactory.getLogger(YeePayUtil.class);
	/**
	 * 格式化字符串
	 */
	public static String formatString(String text) {
		return (text == null ? "" : text.trim());
	}

	/**
	 * String2Integer
	 */
	public static int string2Int(String text) throws NumberFormatException {
		return text == null ? 0 : Integer.parseInt(text);
	}


	/**
	 * 解析http请求返回
	 */
	public static Map<String, String> parseHttpResponseBody(int statusCode, String responseBody) throws Exception {

		Map<String, String> result = new HashMap<String, String>();
		String customError = "";
		if (statusCode != 200) {
			customError = "会话请求失败，响应状态 : " + statusCode;
			result.put("customError", customError);
			return (result);
		}

		Map<String, String> jsonMap = JSON.parseObject(responseBody,
				new TypeReference<TreeMap<String, String>>() {
				});

		if (jsonMap.containsKey("error_code")) {
			result = jsonMap;
			return (result);
		}

		String dataFromYeepay = formatString(jsonMap.get("data"));
		String encryptkeyFromYeepay = formatString(jsonMap.get("encryptkey"));

		boolean signMatch = EncryUtil.checkDecryptAndSign(dataFromYeepay, encryptkeyFromYeepay,
				YeePayConfig.YEEPAY_PUBLIC_KEY, YeePayConfig.MERCHANT_PRIVATE_KEY);
		if (!signMatch) {
			customError = "数据验证失败";
			result.put("customError", customError);
			return (result);
		}
		String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, YeePayConfig.MERCHANT_PRIVATE_KEY);
		String decryptData = AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
		logger.info("YeePayUtil-->>parseHttpResponseBody-->>decryptData : "  + decryptData);
		result = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {});

		return (result);
	}


	/**
	 * 绑卡请求接口
	 */

	public static Map<String, String> bindBankcard(Map<String, String> params) {
		logger.info("YeePayUtil-->>bindBankcard-->>绑卡请求接口-->>开始");
		Map<String, String> result = new HashMap<String, String>();
		String customError = "";    //自定义，非接口返回
		String merchantAESKey = RandomUtil.getRandom(16);
		String requestid = formatString(params.get("requestid"));
		int    identitytype = 2;//用户标识类型  2：用户ID
		String identityid = formatString(params.get("identityid"));
		String cardno = formatString(params.get("cardno"));
		String idcardtype = "01"; //证件类型  01：身份证
		String idcardno = formatString(params.get("idcardno"));
		String username = formatString(params.get("username"));
		String phone = formatString(params.get("phone"));
		String registerphone = formatString(params.get("registerphone"));
		String registerdate = formatString(params.get("registerdate"));
		String registerip = formatString(params.get("registerip"));
		String registeridcardno = formatString(params.get("registeridcardno"));
		String registercontact = formatString(params.get("registercontact"));
		String os = formatString(params.get("os"));
		String imei = formatString(params.get("imei"));
		String userip = formatString(params.get("userip"));
		String ua = formatString(params.get("ua"));
		String registeridcardtype = formatString(params.get("registeridcardtype"));


		TreeMap<String, Object> dataMap	= new TreeMap<String, Object>();
		dataMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
		dataMap.put("requestid", requestid);
		dataMap.put("identitytype", identitytype);
		dataMap.put("identityid", identityid);
		dataMap.put("cardno", cardno);
		dataMap.put("idcardtype", idcardtype);
		dataMap.put("idcardno", idcardno);
		dataMap.put("username", username);
		dataMap.put("phone", phone);
		dataMap.put("registerphone", registerphone);
		dataMap.put("registerdate", registerdate);
		dataMap.put("registerip", registerip);
		dataMap.put("registeridcardno", registeridcardno);
		dataMap.put("registercontact", registercontact);
		dataMap.put("os", os);
		dataMap.put("imei", imei);
		dataMap.put("userip", userip);
		dataMap.put("ua", ua);
		dataMap.put("registeridcardtype", registeridcardtype);

		String sign = EncryUtil.handleRSA(dataMap, YeePayConfig.MERCHANT_PRIVATE_KEY);
		dataMap.put("sign", sign);

		logger.info("YeePayUtil-->>bindBankcard-->>绑卡请求接口: dataMap =  " + dataMap);
		logger.info("YeePayUtil-->>bindBankcard-->>绑卡请求接口: sign =  " + sign);

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(YeePayConfig.BIND_BANKCARD_URL);

		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, YeePayConfig.YEEPAY_PUBLIC_KEY);
			NameValuePair[] datas = {
					new NameValuePair("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT),
					new NameValuePair("data", data),
					new NameValuePair("encryptkey", encryptkey)
			};
			postMethod.setRequestBody(datas);

			int statusCode = httpClient.executeMethod(postMethod);
			byte[] responseByte = postMethod.getResponseBody();
			String responseBody = new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody);
			logger.info("YeePayUtil-->>bindBankcard-->>绑卡请求接口-->>结束");
		} catch (Exception e) {
			customError = "数据异常:" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
			logger.error("YeePayUtil-->>bindBankcard-->>绑卡请求接口-->>Exception " + e.getMessage());
			return (result);
		} finally {
			postMethod.releaseConnection();
		}
		return (result);
	}


	/**
	 *  确定绑卡接口
	 */

	public static Map<String, String> confirmBindBankcard(Map<String, String> params) {
		logger.info("YeePayUtil-->>confirmBindBankcard-->>确定绑卡接口-->>开始");

		String merchantAESKey = RandomUtil.getRandom(16);
		String requestid = formatString(params.get("requestid"));
		String validatecode = formatString(params.get("validatecode"));

		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
		dataMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
		dataMap.put("requestid", requestid);
		dataMap.put("validatecode", validatecode);

		String sign = EncryUtil.handleRSA(dataMap, YeePayConfig.MERCHANT_PRIVATE_KEY);
		dataMap.put("sign", sign);

		logger.info("YeePayUtil-->>confirmBindBankcard-->>确定绑卡接口: confirmBindBankcardURL" + YeePayConfig.CONFIRM_BIND_BANKCARD_URL);
		logger.info("YeePayUtil-->>confirmBindBankcard-->>确定绑卡接口: dataMap" + dataMap);

		Map<String, String> result = new HashMap<String, String>();
		String customError = "";    //自定义，非接口返回

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(YeePayConfig.CONFIRM_BIND_BANKCARD_URL);

		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, YeePayConfig.YEEPAY_PUBLIC_KEY);

			NameValuePair[] datas = {
					new NameValuePair("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT),
					new NameValuePair("data", data),
					new NameValuePair("encryptkey", encryptkey)
			};

			postMethod.setRequestBody(datas);

			int statusCode = httpClient.executeMethod(postMethod);
			byte[] responseByte = postMethod.getResponseBody();
			String responseBody = new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody);
			logger.info("YeePayUtil-->>confirmBindBankcard-->>确定绑卡接口-->>结束");
		} catch (Exception e) {
			customError = "数据异常:" + e.getMessage();
			result.put("customError", customError);
			logger.error("YeePayUtil-->>confirmBindBankcard-->>确定绑卡接口-->>Exception " + e.getMessage());
			return (result);
		} finally {
			postMethod.releaseConnection();
		}
		return (result);
	}



	/**
	 * 支付接口--不发送短验
	 */

	public static Map<String, String> directBindPay(Map<String, String> params) {
		logger.info("YeePayUtil-->>directBindPay-->>支付接口--不发送短验-->>开始");

		Map<String, String> result = new HashMap<String, String>();
		String customError = "";    //自定义，非接口返回

		String merchantAESKey = RandomUtil.getRandom(16);
		String orderid = formatString(params.get("orderid"));
		String productname = formatString(params.get("productname"));
		String productdesc = formatString(params.get("productdesc"));
		String identityid = formatString(params.get("identityid"));
		String card_top = formatString(params.get("card_top"));
		String card_last = formatString(params.get("card_last"));
		String callbackurl = formatString(params.get("callbackurl"));
		String imei = formatString(params.get("imei"));
		String userip = formatString(params.get("userip"));
		String ua = formatString(params.get("ua"));

		int transtime = (int) (System.currentTimeMillis()/1000);
		int amount = string2Int(params.get("amount"));
		int identitytype = 2;


		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
		dataMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
		dataMap.put("orderid", orderid);
		dataMap.put("transtime", transtime);
	//	dataMap.put("currency", currency);
		dataMap.put("amount", amount);
		dataMap.put("productname", productname);
		dataMap.put("productdesc", productdesc);
		dataMap.put("identityid", identityid);
		dataMap.put("identitytype", identitytype);
		dataMap.put("card_top", card_top);
		dataMap.put("card_last", card_last);
		//dataMap.put("orderexpdate", orderexpdate);
		dataMap.put("callbackurl", callbackurl);
		dataMap.put("imei", imei);
		dataMap.put("userip", userip);
		dataMap.put("ua", ua);

		String sign = EncryUtil.handleRSA(dataMap, YeePayConfig.MERCHANT_PRIVATE_KEY);
		dataMap.put("sign", sign);

		logger.info("YeePayUtil-->>directBindPay-->>支付接口--不发送短验: directBindPayURL = " + YeePayConfig.DIRECT_BIND_PAY_URL);
		logger.info("YeePayUtil-->>directBindPay-->>支付接口--不发送短验: dataMap = " + dataMap);

		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(YeePayConfig.DIRECT_BIND_PAY_URL);

		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, YeePayConfig.YEEPAY_PUBLIC_KEY);

			NameValuePair[] datas = {
					new NameValuePair("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT),
					new NameValuePair("data", data),
					new NameValuePair("encryptkey", encryptkey)
			};

			postMethod.setRequestBody(datas);

			int statusCode = httpClient.executeMethod(postMethod);
			byte[] responseByte = postMethod.getResponseBody();
			String responseBody = new String(responseByte, "UTF-8");

			result = parseHttpResponseBody(statusCode, responseBody);
			logger.info("YeePayUtil-->>directBindPay-->>支付接口--不发送短验: result = " + result);
			logger.info("YeePayUtil-->>directBindPay-->>支付接口--不发送短验-->>结束");
		} catch (Exception e) {
			customError = "数据异常:" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
			logger.error("YeePayUtil-->>directBindPay-->>支付接口--不发送短验-->>Exception" + e.getMessage());
			return (result);

		} finally {
			postMethod.releaseConnection();
		}
		return (result);
	}

	/**
	 * 支付回调验证，解密
	 * @param data
	 * @param encryptkey
     * @return
     */
	public static Map<String, String> decryptCallbackData(String data, String encryptkey) {
		//logger.info("decryptCallbackData ---->开始： data:{} ;  encryptkey: {}", data, encryptkey);
		Map<String, String> callbackResult = new HashMap<String, String>();
		String customError = "";

		try {
			boolean signMatch = EncryUtil.checkDecryptAndSign(data, encryptkey, YeePayConfig.YEEPAY_PUBLIC_KEY, YeePayConfig.MERCHANT_PRIVATE_KEY);

			if (!signMatch) {
				customError = "Sign not match error";
				callbackResult.put("customError", customError);
				return callbackResult;
			}

			String yeepayAESKey = RSA.decrypt(encryptkey, YeePayConfig.MERCHANT_PRIVATE_KEY);
			String decryptData = AES.decryptFromBase64(data, yeepayAESKey);
			callbackResult = JSON.parseObject(decryptData, new TypeReference<TreeMap<String, String>>() {
			});

		} catch (Exception e) {
			customError = "数据异常:" + e.toString();
			callbackResult.put("customError", customError);
			e.printStackTrace();
			logger.error("YeePayUtil-->>directBindPay-->>支付接口--不发送短验-->>Exception" + e.getMessage());

		}
		logger.info("decryptCallbackData ---->callbackResult:{} ", callbackResult);
		return (callbackResult);
	}


	/**
	 * 解绑卡
	 */
	public static Map<String, String> unbindBankcard(Map<String, String> params) throws Exception {
		logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡-->>开始");
		//自定义，非接口返回
		Map<String, String> result = new HashMap<String, String>();
		String customError = "";
		//获取配置参数
		String merchantAESKey = RandomUtil.getRandom(16);
		String identityid = formatString(params.get("identityid"));
		String bindid = formatString(params.get("bindid"));
		int identitytype;
		try {
			if (params.get("identitytype") == null) {
				throw new Exception("identitytype is null!!!!!");
			} else {
				identitytype = string2Int(params.get("identitytype"));
			}
		} catch (Exception e) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("数据格式化异常 : ");
			buffer.append("[identitytype = " + formatString(params.get("identitytype")) + "]. ");
			e.printStackTrace();
			customError = buffer.toString();
			result.put("customError", customError);
			logger.error("YeePayUtil-->>unbindBankcard-->>解绑卡-->>Exception " + e.getMessage());
			return (result);
		}

		//构建请求参数
		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
		dataMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
		dataMap.put("bindid", bindid);
		dataMap.put("identitytype", identitytype);
		dataMap.put("identityid", identityid);
		String sign = EncryUtil.handleRSA(dataMap, YeePayConfig.MERCHANT_PRIVATE_KEY);
		dataMap.put("sign", sign);

		logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡: unbindUrl: " + YeePayConfig.UNBIND_URL);
		logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡: dataMap: " + dataMap);
		logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡: sign: " + sign);

		//请求服务器
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(YeePayConfig.UNBIND_URL);
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, YeePayConfig.YEEPAY_PUBLIC_KEY);
			logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡: merchantaccount: " + YeePayConfig.MERCHANT_ACCOUNT);
			logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡: data: " + data);
			logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡: encryptkey: " + encryptkey);
			NameValuePair[] datas = {new NameValuePair("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT),
					new NameValuePair("data", data),
					new NameValuePair("encryptkey", encryptkey)};

			postMethod.setRequestBody(datas);

			int statusCode = httpClient.executeMethod(postMethod);
			byte[] responseByte = postMethod.getResponseBody();
			String responseBody = new String(responseByte, "UTF-8");
			result = parseHttpResponseBody(statusCode, responseBody);

			logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡-->>result: " + result);
			logger.info("YeePayUtil-->>unbindBankcard-->>解绑卡-->>结束");
		} catch (Exception e) {
			customError = "数据异常:" + e.toString();
			result.put("customError", customError);
			e.printStackTrace();
			logger.error("YeePayUtil-->>unbindBankcard-->>解绑卡-->>Exception " + e.getMessage());
			throw new Exception("YeePayUtil-->>unbindBankcard-->>解绑卡-->>Exception " + e.getMessage());
		} finally {
			postMethod.releaseConnection();
		}
		return (result);
	}


//	/**
//	 * 支付请求接口--发送短信
//	 */
//
//	public static Map<String, String> payNeedSms(Map<String, String> params) {
//		logger.info("YeePayUtil-->>payNeedSms-->>支付请求接口-->>开始");
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";    //自定义，非接口返回
//
//		String merchantAESKey = RandomUtil.getRandom(16);
//		String orderid = formatString(params.get("orderid"));
//		String productname = formatString(params.get("productname"));
//		String productdesc = formatString(params.get("productdesc"));
//		String identityid = formatString(params.get("identityid"));
//		String card_top = formatString(params.get("card_top"));
//		String card_last = formatString(params.get("card_last"));
//		String callbackurl = formatString(params.get("callbackurl"));
//		String imei = formatString(params.get("imei"));
//		String userip = formatString(params.get("userip"));
//		String ua = formatString(params.get("ua"));
//		int transtime = (int) (System.currentTimeMillis()/1000);
//		int amount = String2Int(params.get("amount"))*100;
//		int identitytype = 2;
//
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
//		dataMap.put("orderid", orderid);
//		dataMap.put("transtime", transtime);
//		//dataMap.put("currency", currency);
//		dataMap.put("amount", amount);
//		dataMap.put("productname", productname);
//		dataMap.put("productdesc", productdesc);
//		dataMap.put("identityid", identityid);
//		dataMap.put("identitytype", identitytype);
//		dataMap.put("card_top", card_top);
//		dataMap.put("card_last", card_last);
//		//dataMap.put("orderexpdate", orderexpdate);
//		dataMap.put("callbackurl", callbackurl);
//		dataMap.put("imei", imei);
//		dataMap.put("userip", userip);
//		dataMap.put("ua", ua);
//
//		String sign = EncryUtil.handleRSA(dataMap, YeePayConfig.MERCHANT_PRIVATE_KEY);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>payNeedSms-->>支付请求接口: payNeedSmsURL = " + YeePayConfig.PAY_NEED_SMS_URL);
//		logger.info("YeePayUtil-->>payNeedSms-->>支付请求接口: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>payNeedSms-->>支付请求接口: sign = " + sign);
//
//		HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod(YeePayConfig.PAY_NEED_SMS_URL);
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, YeePayConfig.YEEPAY_PUBLIC_KEY);
//
//			NameValuePair[] datas = {
//					new NameValuePair("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT),
//					new NameValuePair("data", data),
//					new NameValuePair("encryptkey", encryptkey)
//			};
//
//			postMethod.setRequestBody(datas);
//
//			int statusCode = httpClient.executeMethod(postMethod);
//			byte[] responseByte = postMethod.getResponseBody();
//			String responseBody = new String(responseByte, "UTF-8");
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>payNeedSms-->>支付请求接口-->>result: " + result);
//			logger.info("YeePayUtil-->>payNeedSms-->>支付请求接口-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>payNeedSms-->>支付请求接口-->>Exception " + e.getMessage());
//		} finally {
//			postMethod.releaseConnection();
//		}
//		return (result);
//	}
//
//
//	/**
//	 * 发送短信验证码接口
//	 */
//
//	public static Map<String, String> sendSmsByOrder(String orderid) {
//		logger.info("YeePayUtil-->>sendSmsByOrder-->>发送短信验证码接口-->>开始");
//
//		String merchantAESKey = RandomUtil.getRandom(16);
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
//		dataMap.put("orderid", orderid);
//
//		String sign = EncryUtil.handleRSA(dataMap, YeePayConfig.MERCHANT_PRIVATE_KEY);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>sendSmsByOrder-->>发送短信验证码接口: smsSendURL = " + YeePayConfig.SMS_SEND_URL);
//		logger.info("YeePayUtil-->>sendSmsByOrder-->>发送短信验证码接口: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>sendSmsByOrder-->>发送短信验证码接口: sign = " + sign);
//
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";    // 自定义，非接口返回
//
//		HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod(YeePayConfig.SMS_SEND_URL);
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, YeePayConfig.YEEPAY_PUBLIC_KEY);
//
//			NameValuePair[] datas = {
//					new NameValuePair("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT),
//					new NameValuePair("data", data),
//					new NameValuePair("encryptkey", encryptkey)
//			};
//
//			postMethod.setRequestBody(datas);
//
//			int statusCode = httpClient.executeMethod(postMethod);
//			byte[] responseByte = postMethod.getResponseBody();
//			String responseBody = new String(responseByte, "UTF-8");
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>sendSmsByOrder-->>发送短信验证码接口-->>result:{}",result);
//			logger.info("YeePayUtil-->>sendSmsByOrder-->>发送短信验证码接口-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常: " + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>sendSmsByOrder-->>发送短信验证码接口-->>Exception " + e.getMessage());
//		} finally {
//			postMethod.releaseConnection();
//		}
//		System.out.println("result : " + result);
//
//		return (result);
//	}

//
//	/**
//	 * smsConfirm() : 4.2.3 确认支付
//	 */
//
//	public static Map<String, String> smsConfirm(Map<String, String> params) {
//		logger.info("YeePayUtil-->>smsConfirm-->>确认支付-->>开始");
//
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String smsConfirmURL = getSmsConfirmURL();
//
//		String orderid = params.get("orderid");
//		String validatecode = params.get("validatecode");
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("orderid", orderid);
//		dataMap.put("validatecode", validatecode);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>smsConfirm-->>确认支付: smsConfirmURL = " + smsConfirmURL);
//		logger.info("YeePayUtil-->>smsConfirm-->>确认支付: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>smsConfirm-->>确认支付: sign = " + sign);
//
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";    // 自定义，非接口返回
//
//		HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod(smsConfirmURL);
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			NameValuePair[] datas = {new NameValuePair("merchantaccount", merchantaccount),
//					new NameValuePair("data", data),
//					new NameValuePair("encryptkey", encryptkey)};
//
//			postMethod.setRequestBody(datas);
//
//			int statusCode = httpClient.executeMethod(postMethod);
//			byte[] responseByte = postMethod.getResponseBody();
//			String responseBody = new String(responseByte, "UTF-8");
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//
//		} catch (Exception e) {
//			customError = "数据异常: " + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>smsConfirm-->>确认支付-->>Exception " + e.getMessage());
//		} finally {
//			postMethod.releaseConnection();
//		}
//
//		System.out.println("result : " + result);
//
//		return (result);
//	}



//
//	/**
//	 * queryByOrder() : 5.1 查询接口
//	 */
//
//	public static Map<String, String> queryByOrder(String orderid, String yborderid) {
//		logger.info("YeePayUtil-->>queryByOrder-->>查询接口-->>开始");
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String paymentQueryURL = getPaymentQueryURL();
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("orderid", orderid);
//		dataMap.put("yborderid", yborderid);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>queryByOrder-->>查询接口: paymentQueryURL = " + paymentQueryURL);
//		logger.info("YeePayUtil-->>queryByOrder-->>查询接口: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>queryByOrder-->>查询接口: sign = " + sign);
//
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";    // 自定义，非接口返回
//
//		HttpClient httpClient = new HttpClient();
//		GetMethod getMethod = new GetMethod();
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			String url = paymentQueryURL +
//					"?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
//					"&data=" + URLEncoder.encode(data, "UTF-8") +
//					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
//
//			System.out.println("url	 : " + url);
//
//			getMethod = new GetMethod(url);
//			int statusCode = httpClient.executeMethod(getMethod);
//			String responseBody = getMethod.getResponseBodyAsString();
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>queryByOrder-->>查询接口: result = " + result);
//			logger.info("YeePayUtil-->>queryByOrder-->>查询接口-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>queryByOrder-->>查询接口-->>Exception " + e.getMessage());
//		} finally {
//			getMethod.releaseConnection();
//		}
//		return (result);
//	}
//
//
//	/**
//	 * withdraw() : 提现接口
//	 */
//
//	public static Map<String, String> withdraw(Map<String, String> params) {
//		logger.info("YeePayUtil-->>withdraw-->>提现接口-->>开始");
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";    //自定义，非接口返回
//
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String withdrawURL = getWithdrawURL();
//
//		String requestid = formatString(params.get("requestid"));
//		String identityid = formatString(params.get("identityid"));
//		String card_top = formatString(params.get("card_top"));
//		String card_last = formatString(params.get("card_last"));
//		String drawtype = formatString(params.get("drawtype"));
//		String imei = formatString(params.get("imei"));
//		String userip = formatString(params.get("userip"));
//		String ua = formatString(params.get("ua"));
//
//		int amount = 0;
//		int identitytype = 0;
//		int currency = 0;
//
//		try {
//			if (params.get("identitytype") == null) {
//				throw new Exception("identitytype is null!!!!!");
//			} else {
//				identitytype = String2Int(params.get("identitytype"));
//			}
//
//			if (params.get("amount") == null) {
//				throw new Exception("amount is null!!!!!");
//			} else {
//				amount = String2Int(params.get("amount"));
//			}
//
//			currency = String2Int(params.get("currency"));
//
//		} catch (Exception e) {
//			StringBuffer buffer = new StringBuffer();
//			buffer.append("error - the following parameters must be int  : ");
//			buffer.append("[amount = " + formatString(params.get("amount")) + "], ");
//			buffer.append("[identitytype = " + formatString(params.get("identitytype")) + "], ");
//			buffer.append("[currency = " + formatString(params.get("currency")) + "]. ");
//			e.printStackTrace();
//			customError = buffer.toString();
//			result.put("customError", customError);
//			logger.error("YeePayUtil-->>withdraw-->>提现接口-->>Exception " + e.getMessage());
//			return (result);
//		}
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("requestid", requestid);
//		dataMap.put("identityid", identityid);
//		dataMap.put("identitytype", identitytype);
//		dataMap.put("card_top", card_top);
//		dataMap.put("card_last", card_last);
//		dataMap.put("amount", amount);
//		dataMap.put("currency", currency);
//		dataMap.put("drawtype", drawtype);
//		dataMap.put("imei", imei);
//		dataMap.put("userip", userip);
//		dataMap.put("ua", ua);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>withdraw-->>查询接口: withdrawURL = " + withdrawURL);
//		logger.info("YeePayUtil-->>withdraw-->>查询接口: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>withdraw-->>查询接口: sign = " + sign);
//
//		HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod(withdrawURL);
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			NameValuePair[] datas = {new NameValuePair("merchantaccount", merchantaccount),
//					new NameValuePair("data", data),
//					new NameValuePair("encryptkey", encryptkey)};
//
//			postMethod.setRequestBody(datas);
//
//			int statusCode = httpClient.executeMethod(postMethod);
//			byte[] responseByte = postMethod.getResponseBody();
//			String responseBody = new String(responseByte, "UTF-8");
//
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>withdraw-->>查询接口: responseBody = " + responseBody);
//			logger.info("YeePayUtil-->>withdraw-->>查询接口: result = " + result);
//			logger.info("YeePayUtil-->>withdraw-->>查询接口-->>结束");
//
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>withdraw-->>查询接口-->>Exception " + e.getMessage());
//		} finally {
//			postMethod.releaseConnection();
//		}
//		return (result);
//	}
//
//
//	/**
//	 * queryWithdraw() : 提现查询接口
//	 */
//
//	public static Map<String, String> queryWithdraw(String requestid, String ybdrawflowid) {
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口-->>开始");
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String queryWithdrawURL = getQueryWithdrawURL();
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("requestid", requestid);
//		dataMap.put("ybdrawflowid", ybdrawflowid);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: requestid = " + requestid);
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: ybdrawflowid = " + ybdrawflowid);
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: merchantaccount = " + merchantaccount);
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: merchantPrivateKey = " + merchantPrivateKey);
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: yeepayPublicKey = " + yeepayPublicKey);
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: queryWithdrawURL = " + queryWithdrawURL);
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: sign = " + sign);
//
//
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";  // 自定义参数，非接口返回。
//
//		HttpClient httpClient = new HttpClient();
//		GetMethod getMethod = new GetMethod();
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			String url = queryWithdrawURL +
//					"?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
//					"&data=" + URLEncoder.encode(data, "UTF-8") +
//					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
//
//			getMethod = new GetMethod(url);
//			int statusCode = httpClient.executeMethod(getMethod);
//			String responseBody = getMethod.getResponseBodyAsString();
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: url = " + url);
//			logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: responseBody = " + responseBody);
//			logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口: result = " + result);
//			logger.info("YeePayUtil-->>queryWithdraw-->>提现查询接口-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>queryWithdraw-->>提现查询接口-->>Exception " + e.getMessage());
//		} finally {
//			getMethod.releaseConnection();
//		}
//		return (result);
//	}
//
//
	/**
	 * queryAuthbindList() : 绑卡查询接口
	 */
	public static Map<String, String> queryAuthbindList(String identityid, int identitytype) throws Exception{
		logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口-->>开始");
		Map<String, String> result = new HashMap<String, String>();
		String customError = "";    //自定义，非接口返回
		String merchantAESKey = RandomUtil.getRandom(16);

		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
		dataMap.put("merchantaccount", YeePayConfig.MERCHANT_ACCOUNT);
		dataMap.put("identityid", identityid);
		dataMap.put("identitytype", identitytype);
		String sign = EncryUtil.handleRSA(dataMap, YeePayConfig.MERCHANT_PRIVATE_KEY);
		dataMap.put("sign", sign);
		logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口: queryAuthbindListURL = " + YeePayConfig.QUERY_AUTH_BIND_URL);
		logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口: dataMap = " + dataMap);
		logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口: sign = " + sign);

		HttpClient httpClient = new HttpClient();
		GetMethod getMethod = new GetMethod();
		try {
			String jsonStr = JSON.toJSONString(dataMap);
			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
			String encryptkey = RSA.encrypt(merchantAESKey, YeePayConfig.YEEPAY_PUBLIC_KEY);
			String url = YeePayConfig.QUERY_AUTH_BIND_URL +
					"?merchantaccount=" + URLEncoder.encode(YeePayConfig.MERCHANT_ACCOUNT, "UTF-8") +
					"&data=" + URLEncoder.encode(data, "UTF-8") +
					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
			getMethod = new GetMethod(url);
			int statusCode = httpClient.executeMethod(getMethod);
			String responseBody = getMethod.getResponseBodyAsString();

			logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口: url = " + url);
			logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口: responseBody = " + responseBody);
			result = parseHttpResponseBody(statusCode, responseBody);
			logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口: result = " + result);
			logger.info("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口-->>结束");
		} catch (Exception e) {
			customError = "数据异常:" + e.toString();
			result.put("customError", customError);
			logger.error("YeePayUtil-->>queryAuthbindList-->>绑卡查询接口-->>Exception " + e.getMessage());
		} finally {
			getMethod.releaseConnection();
		}
		return (result);
	}
//
//
//	/**
//	 * bankCardCheck() : 银行卡信息查询接口
//	 */
//	public static Map<String, String> bankCardCheck(String cardno) {
//		logger.info("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口-->>开始");
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String bankCardCheckURL = getBankCardCheckURL();
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("cardno", cardno);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口: bankCardCheckURL = " + bankCardCheckURL);
//		logger.info("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口: sign = " + sign);
//
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";
//
//		HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod(bankCardCheckURL);
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			NameValuePair[] datas = {new NameValuePair("merchantaccount", merchantaccount),
//					new NameValuePair("data", data),
//					new NameValuePair("encryptkey", encryptkey)};
//
//			postMethod.setRequestBody(datas);
//
//			int statusCode = httpClient.executeMethod(postMethod);
//			byte[] responseByte = postMethod.getResponseBody();
//			String responseBody = new String(responseByte, "UTF-8");
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口: responseBody = " + responseBody);
//			logger.info("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口: result = " + result);
//			logger.info("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>bankCardCheck-->>银行卡信息查询接口-->>Exception " + e.getMessage());
//		} finally {
//			postMethod.releaseConnection();
//		}
//		return (result);
//	}
//
//
//	/**
//	 * getPathOfPayClearData()：获取清算数据
//	 * <p/>
//	 * 返回说明：
//	 * <p/>
//	 * filePath				- 批量查询结果文件的路径
//	 * error_code			- 错误返回码
//	 * error				- 错误信息
//	 * customError			- 自定义，非接口返回
//	 */
//
//	public static Map<String, String> getPathOfPayClearData(String startdate, String enddate, String sysPath) {
//		logger.info("YeePayUtil-->>getPathOfPayClearData-->>获取清算数据-->>开始");
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String payClearDataURL = getPayClearDataURL();
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("startdate", startdate);
//		dataMap.put("enddate", enddate);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		Map<String, String> queryResult = new HashMap<String, String>();
//		String filePath = "";
//		String error_code = "";
//		String error = "";
//		String customError = "";
//
//		HttpClient httpClient = new HttpClient();
//		GetMethod getMethod = new GetMethod();
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			String url = payClearDataURL +
//					"?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
//					"&data=" + URLEncoder.encode(data, "UTF-8") +
//					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
//
//			getMethod = new GetMethod(url);
//
//			int statusCode = httpClient.executeMethod(getMethod);
//
//			if (statusCode != 200) {
//				customError = "Get request failed, response code = " + statusCode;
//				queryResult.put("customError", customError);
//				return (queryResult);
//			}
//
//			InputStream responseStream = getMethod.getResponseBodyAsStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
//			//BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream));
//
//			String line = reader.readLine();
//			if (line.startsWith("{")) {
//				Map<String, Object> jsonMap = JSON.parseObject(line, TreeMap.class);
//
//				if (jsonMap.containsKey("error_code")) {
//					error_code = formatString((String) jsonMap.get("error_code"));
//					error = formatString((String) jsonMap.get("error"));
//				} else {
//					String dataFromYeepay = formatString((String) jsonMap.get("data"));
//					String encryptkeyFromYeepay = formatString((String) jsonMap.get("encryptkey"));
//
//					String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
//					String decryptData = AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
//					Map<String, Object> decryptDataMap = JSON.parseObject(decryptData, TreeMap.class);
//
//					error_code = formatString((String) decryptDataMap.get("error_code"));
//					error = formatString((String) decryptDataMap.get("error"));
//
//					System.out.println("decryptData : " + decryptData);
//				}
//			} else {
//				String outputFilePath = sysPath + File.separator + "clearData";
//				File file = new File(outputFilePath);
//				file.mkdir();
//
//				String time = String.valueOf(System.currentTimeMillis());
//				String fileName = "payClearData_" + startdate + "_" + enddate + "_" + time + ".txt";
//				String absolutePathOfOutputFile = outputFilePath + File.separator + fileName;
//				filePath = absolutePathOfOutputFile;
//
//				File outputFile = new File(absolutePathOfOutputFile);
//				FileWriter fileWriter = new FileWriter(outputFile);
//				BufferedWriter writer = new BufferedWriter(fileWriter);
//
//				System.out.println("filePath : " + filePath);
//
//				writer.write(line);
//				writer.write(System.getProperty("line.separator"));
//				while ((line = reader.readLine()) != null) {
//					writer.write(line);
//					writer.write(System.getProperty("line.separator"));
//				}
//
//				writer.close();
//			}
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>getPathOfPayClearData-->>获取清算数据-->>Exception " + e.getMessage());
//		} finally {
//			getMethod.releaseConnection();
//		}
//
//		queryResult.put("filePath", filePath);
//		queryResult.put("error_code", error_code);
//		queryResult.put("error", error);
//		queryResult.put("customError", customError);
//
//		return (queryResult);
//	}
//
//	/**
//	 * refund() : 单笔退款方法
//	 */
//
//	public static Map<String, String> refund(Map<String, String> params) {
//
//		logger.info("YeePayUtil-->>refund-->>单笔退款方法-->>开始");
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";    // 自定义，非接口返回
//
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String refundURL = getRefundURL();
//
//		String origyborderid = formatString(params.get("origyborderid"));
//		String orderid = formatString(params.get("orderid"));
//		String cause = formatString(params.get("cause"));
//
//		int amount = 0;
//		int currency = 0;
//
//		try {
//			//amount、currency是必填参数
//			if (params.get("amount") == null) {
//				throw new Exception("amount is null!!!!!");
//			} else {
//				amount = String2Int(params.get("amount"));
//			}
//
//			if (params.get("currency") == null) {
//				throw new Exception("currency is null!!!!!");
//			} else {
//				currency = String2Int(params.get("currency"));
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			logger.error("YeePayUtil-->>refund-->>单笔退款方法-->>Exception " + e.getMessage());
//			return (result);
//		}
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("origyborderid", origyborderid);
//		dataMap.put("orderid", orderid);
//		dataMap.put("cause", cause);
//		dataMap.put("amount", amount);
//		dataMap.put("currency", currency);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>refund-->>单笔退款方法: refundURL = " + refundURL);
//		logger.info("YeePayUtil-->>refund-->>单笔退款方法: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>refund-->>单笔退款方法: sign = " + sign);
//
//		HttpClient httpClient = new HttpClient();
//		PostMethod postMethod = new PostMethod(refundURL);
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			logger.info("YeePayUtil-->>refund-->>单笔退款方法: data = " + data);
//			logger.info("YeePayUtil-->>refund-->>单笔退款方法: encryptkey = " + encryptkey);
//
//			NameValuePair[] datas = {new NameValuePair("merchantaccount", merchantaccount),
//					new NameValuePair("data", data),
//					new NameValuePair("encryptkey", encryptkey)};
//
//			postMethod.setRequestBody(datas);
//
//			int statusCode = httpClient.executeMethod(postMethod);
//			byte[] responseByte = postMethod.getResponseBody();
//			String responseBody = new String(responseByte, "UTF-8");
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>refund-->>单笔退款方法: result = " + result);
//			logger.info("YeePayUtil-->>refund-->>单笔退款方法-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>refund-->>单笔退款方法-->>Exception " + e.getMessage());
//		} finally {
//			postMethod.releaseConnection();
//		}
//		return (result);
//	}
//
//
//	/**
//	 * refundQuery() : 退款查询
//	 */
//
//	public static Map<String, String> refundQuery(String orderid) {
//		logger.info("YeePayUtil-->>refundQuery-->>退款查询-->>开始");
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String refundQueryURL = getRefundQueryURL();
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("orderid", orderid);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>refundQuery-->>退款查询: refundQueryURL = " + refundQueryURL);
//		logger.info("YeePayUtil-->>refundQuery-->>退款查询: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>refundQuery-->>退款查询: sign = " + sign);
//
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";
//
//		HttpClient httpClient = new HttpClient();
//		GetMethod getMethod = new GetMethod();
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			String url = refundQueryURL +
//					"?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
//					"&data=" + URLEncoder.encode(data, "UTF-8") +
//					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
//
//			getMethod = new GetMethod(url);
//			int statusCode = httpClient.executeMethod(getMethod);
//			String responseBody = getMethod.getResponseBodyAsString();
//
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>refundQuery-->>退款查询: result = " + result);
//			logger.info("YeePayUtil-->>refundQuery-->>退款查询: responseBody = " + responseBody);
//			logger.info("YeePayUtil-->>refundQuery-->>退款查询-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>refundQuery-->>退款查询-->>Exception " + e.getMessage());
//		} finally {
//			getMethod.releaseConnection();
//		}
//		return (result);
//	}
//
//
//	/**
//	 * getPathOfRefundClearData()
//	 */
//
//	public static Map<String, String> getPathOfRefundClearData(String startdate, String enddate, String sysPath) {
//		logger.info("YeePayUtil-->>getPathOfRefundClearData-->>开始");
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String refundClearDataURL = getRefundClearDataURL();
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("startdate", startdate);
//		dataMap.put("enddate", enddate);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>getPathOfRefundClearData-->>: refundClearDataURL = " + refundClearDataURL);
//		logger.info("YeePayUtil-->>getPathOfRefundClearData-->>: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>getPathOfRefundClearData-->>: sign = " + sign);
//
//		Map<String, String> queryResult = new HashMap<String, String>();
//		String filePath = "";
//		String error_code = "";
//		String error = "";
//		String customError = "";
//
//		HttpClient httpClient = new HttpClient();
//		GetMethod getMethod = new GetMethod();
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			String url = refundClearDataURL +
//					"?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
//					"&data=" + URLEncoder.encode(data, "UTF-8") +
//					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
//
//			getMethod = new GetMethod(url);
//			int statusCode = httpClient.executeMethod(getMethod);
//
//			if (statusCode != 200) {
//				customError = "Get request failed, response code = " + statusCode;
//				queryResult.put("customError", customError);
//				return (queryResult);
//			}
//
//			InputStream responseStream = getMethod.getResponseBodyAsStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream, "UTF-8"));
//			//BufferedReader	reader		= new BufferedReader(new InputStreamReader(responseStream));
//
//			String line = reader.readLine();
//			if (line.startsWith("{")) {
//				Map<String, Object> jsonMap = JSON.parseObject(line, TreeMap.class);
//
//				if (jsonMap.containsKey("error_code")) {
//					error_code = formatString((String) jsonMap.get("error_code"));
//					error = formatString((String) jsonMap.get("error"));
//				} else {
//					String dataFromYeepay = formatString((String) jsonMap.get("data"));
//					String encryptkeyFromYeepay = formatString((String) jsonMap.get("encryptkey"));
//
//					String yeepayAESKey = RSA.decrypt(encryptkeyFromYeepay, merchantPrivateKey);
//					String decryptData = AES.decryptFromBase64(dataFromYeepay, yeepayAESKey);
//					Map<String, Object> decryptDataMap = JSON.parseObject(decryptData, TreeMap.class);
//
//					error_code = formatString((String) decryptDataMap.get("error_code"));
//					error = formatString((String) decryptDataMap.get("error"));
//
//					System.out.println("decryptData : " + decryptData);
//				}
//			} else {
//				String outputFilePath = sysPath + File.separator + "clearData";
//				File file = new File(outputFilePath);
//				file.mkdir();
//
//				String time = String.valueOf(System.currentTimeMillis());
//				String fileName = "refundClearData_" + startdate + "_" + enddate + "_" + time + ".txt";
//				String absolutePathOfOutputFile = outputFilePath + File.separator + fileName;
//				filePath = absolutePathOfOutputFile;
//
//				File outputFile = new File(absolutePathOfOutputFile);
//				FileWriter fileWriter = new FileWriter(outputFile);
//				BufferedWriter writer = new BufferedWriter(fileWriter);
//
//				System.out.println("filePath : " + filePath);
//
//				writer.write(line);
//				writer.write(System.getProperty("line.separator"));
//				while ((line = reader.readLine()) != null) {
//					writer.write(line);
//					writer.write(System.getProperty("line.separator"));
//				}
//
//				writer.close();
//			}
//			logger.info("YeePayUtil-->>getPathOfRefundClearData-->>结束");
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			e.printStackTrace();
//			logger.error("YeePayUtil-->>getPathOfRefundClearData-->>Exception " + e.getMessage());
//		} finally {
//			getMethod.releaseConnection();
//		}
//
//		queryResult.put("filePath", filePath);
//		queryResult.put("error_code", error_code);
//		queryResult.put("error", error);
//		queryResult.put("customError", customError);
//
//		return (queryResult);
//	}
//
//
//	/**
//	 * payapiQueryByOrderid() : 4.4 支付结果查询
//	 */
//
//	public static Map<String, String> payapiQueryByOrderid(String orderid) {
//		logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询-->>开始");
//		String merchantaccount = getMerchantAccount();
//		String merchantPrivateKey = getMerchantPrivateKey();
//		String merchantAESKey = getMerchantAESKey();
//		String yeepayPublicKey = getYeepayPublicKey();
//		String payapiQueryURL = getPayapiQueryURL();
//
//		TreeMap<String, Object> dataMap = new TreeMap<String, Object>();
//		dataMap.put("merchantaccount", merchantaccount);
//		dataMap.put("orderid", orderid);
//
//		String sign = EncryUtil.handleRSA(dataMap, merchantPrivateKey);
//		dataMap.put("sign", sign);
//
//		logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询: payapiQueryURL = " + payapiQueryURL);
//		logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询: dataMap = " + dataMap);
//		logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询: sign = " + sign);
//
//		Map<String, String> result = new HashMap<String, String>();
//		String customError = "";    // 自定义，非接口返回
//
//		HttpClient httpClient = new HttpClient();
//		GetMethod getMethod = new GetMethod();
//
//		try {
//			String jsonStr = JSON.toJSONString(dataMap);
//			String data = AES.encryptToBase64(jsonStr, merchantAESKey);
//			String encryptkey = RSA.encrypt(merchantAESKey, yeepayPublicKey);
//
//			String url = payapiQueryURL +
//					"?merchantaccount=" + URLEncoder.encode(merchantaccount, "UTF-8") +
//					"&data=" + URLEncoder.encode(data, "UTF-8") +
//					"&encryptkey=" + URLEncoder.encode(encryptkey, "UTF-8");
//
//			System.out.println("url	 : " + url);
//			logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询: url = " + url);
//
//			getMethod = new GetMethod(url);
//			int statusCode = httpClient.executeMethod(getMethod);
//			String responseBody = getMethod.getResponseBodyAsString();
//			result = parseHttpResponseBody(statusCode, responseBody);
//			logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询: responseBody = " + responseBody);
//			logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询: result = " + result);
//		} catch (Exception e) {
//			customError = "数据异常:" + e.toString();
//			result.put("customError", customError);
//			e.printStackTrace();
//			logger.info("YeePayUtil-->>payapiQueryByOrderid-->>支付结果查询-->>Exception " + e.getMessage());
//		} finally {
//			getMethod.releaseConnection();
//		}
//		return (result);
//	}

}
