package com.hszsd.webpay.util;

import com.hszsd.webpay.common.ResultConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("resCode", result.getCode());
		paramMap.put("resMsg", result.getMsg());

		return sendPostRequest(url, paramMap, "");
	}
	/**
	 * 发送post请求
	 * @param url
	 * @param paramMap
	 * @param MD5Sign MD5签名
	 * @return
	 */
	public static String sendPostRequest(String url, Map<String, String> paramMap, String MD5Sign){
		String param = createPostParam(paramMap, MD5Sign);
		OutputStreamWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new OutputStreamWriter(conn.getOutputStream(),"UTF-8");
			// 发送请求参数
			out.write(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("sendPostRequest occurs an error and cause by {}",e.getMessage());
		}
		//使用finally块来关闭输出流、输入流
		finally{
			try{
				if(out!=null){
					out.close();
				}
				if(in!=null){
					in.close();
				}
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		return result;
	}


	/**
	 * 创建post请求的参数  格式：name1=value1&name2=value2&。。。。
	 * @param paramMap  参数Map
	 * @param MD5Sign   MD5签名
	 */
	public static String createPostParam(Map<String, String> paramMap,String MD5Sign) {
		StringBuffer sb = new StringBuffer();
		Set es = paramMap.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			sb.append(k + "=" + v + "&");
		}
		sb.append("MD5Sign=" + MD5Sign);
		return sb.toString();
	}


	/**
	 * 接收request中的参数，并放入map中
	 * @param request
	 * @return
     */
	public static Map<String,String> getParamsFromRequest(HttpServletRequest request){
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}

}
