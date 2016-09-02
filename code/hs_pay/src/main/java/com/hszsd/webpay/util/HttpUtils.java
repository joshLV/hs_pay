package com.hszsd.webpay.util;

import com.hszsd.webpay.common.ResultConstants;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  http请求工具
 * @author xk
 *
 */
public class HttpUtils {
	private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

	/**
	 * 封装错误信息，并发送post请求
	 * @param url 请求地址
	 * @param result 封装错误信息枚举
	 * @return
	 */
	public static String sendErrorPostRequest(String url, ResultConstants result){
		logger.info("sendErrorPostRequest is starting and url={}, result={}", url, JsonUtil.obj2json(result));
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("resCode", result.getCode());
		paramMap.put("resMsg", result.getMsg());

		return sendPostRequest(url, paramMap, "");
	}


	/**
	 * 接收request中的参数，并放入map中
	 * @param request
	 * @return
	 */
	public static Map<String,String> getParamsFromRequest(HttpServletRequest request){
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<String> names = request.getParameterNames();
		while(names.hasMoreElements()){
			String key = names.nextElement();
			String value = request.getParameter(key);
			if(value == null || value.trim().equals("")){
				continue;
			}
			map.put(key, value);
		}
		return map;
	}

	/**
	 * httpClient建立post请求
	 * @param url 提交地址
	 * @param sParaTemp 请求参数数组
	 * @param MD5Sign MD5签名
	 * @return
	 */
	public static String sendPostRequest(String url, Map<String, String> sParaTemp, String MD5Sign)  {
		logger.info("sendPostRequest is starting and url={}, sParaTemp={}, MD5Sign={}", url, sParaTemp, MD5Sign);
		// 创建参数列表
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		// 封装参数
		List<String> keys = new ArrayList<String>(sParaTemp.keySet());
		for (int i = 0; i < keys.size(); i++) {
			String name = keys.get(i);
			String value =  sParaTemp.get(name);
			list.add(new BasicNameValuePair(name, value));
		}
		list.add(new BasicNameValuePair("MD5Sign", MD5Sign));
		CloseableHttpClient httpClient = getHttpClient();
		//设置请求和传输超时时间
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(5000).setConnectTimeout(5000).build();
		String responseStr = "";
		try {
			HttpPost post = new HttpPost(url);
			post.setConfig(requestConfig);
			// url格式编码
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(list,"UTF-8");
			post.setEntity(uefEntity);
			// 执行请求
			CloseableHttpResponse httpResponse = httpClient.execute(post);
			try {
				HttpEntity entity = httpResponse.getEntity();
				if (null != entity) {
					// 原始数据
					responseStr = EntityUtils.toString(entity);
					logger.info(" response ----------------- :{}",responseStr);
				}
			} finally {
				httpResponse.close();
			}

		} catch (Exception e) {
			logger.error("sendPostRequest occurs an error and cause by {}, url = {}",e.getMessage(),url);
		} finally {
			try {
				closeHttpClient(httpClient);
			} catch (Exception e) {
				logger.error("closeHttpClient occurs an error and cause by {}",e.getMessage());
			}
		}
		return responseStr;
	}

	private static CloseableHttpClient getHttpClient() {
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, new TrustStrategy() {
						//信任所有
						public boolean isTrusted(X509Certificate[] chain,
												 String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		}  catch (Exception e) {
			e.printStackTrace();
		}
		return  HttpClients.createDefault();
	}

	private static void closeHttpClient(CloseableHttpClient client) throws IOException {
		if (client != null) {
			client.close();
		}
	}

	/**
	 * 获取用户端IP
	 * @param request
	 * @return
     */
	public static String getRemoteHost(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}


}
