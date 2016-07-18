package com.hszsd.webpay.util;

import com.hszsd.webpay.common.PayMethod;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付工具类
 * @author xk
 *
 */
public class PayUtils {
	
	/**
	 * 组装 自动提交form 默认提交方式POST
	 * @param url
	 * @param paramMap
	 * @return
	 */
	public static String generateAutoSubmitForm(String url, Map<String, String> paramMap){
		return generateAutoSubmitForm(url, paramMap, PayMethod.POST);
	}
	
	/**
	 * 组装 自动提交form
	 * @param url
	 * @param paramMap
	 * @param method 提交方式
	 * @return
	 */
	public static String generateAutoSubmitForm(String url, Map<String, String> paramMap, PayMethod method){
		StringBuilder html = new StringBuilder();
        html.append("<script language=\"javascript\">window.onload=function(){document.pay_form.submit();}</script>\n");
        html.append("<form id=\"pay_form\" name=\"pay_form\" action=\"").append(url)
        	.append("\" method=\"")
        	.append(method.getMethod())
        	.append("\">\n");

        for (String key : paramMap.keySet()) {
            html.append("<input type=\"hidden\" name=\"" + key + "\" id=\"" + key + "\" value=\"" + paramMap.get(key) + "\">\n");
        }
        html.append("</form>\n");
        return html.toString();
	}
	
	/**
	 * 生成地址 默认连接符 &
	 * @param map
	 * @return
	 */
	public static String generateUrl(Map<String, String> map){
		return generateUrl(map, '&');
	}
	
	/**
	 * 生成地址
	 * @param map
	 * @param connector 连接符
	 * @return
	 */
	public static String generateUrl(Map<String, String> map, char connector){
		if(map==null || map.size()<=0) return "";
		StringBuffer b = new StringBuffer();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            b.append(entry.getKey());
            b.append('=');
            if (entry.getValue() != null) {
                b.append(entry.getValue());
            }
            b.append(connector);
        }
        return b.substring(0, b.length()-1);
	}
	
	/**
	 * 生成连接字符串
	 * @param str
	 * @param connector
	 * @return
	 */
	public static String generateStr(String[] str, String connector){
		if(str==null || str.length<=0) return "";
		StringBuffer b = new StringBuffer();
		for(String s : str){
			if(StringUtils.isEmpty(s)){
				b.append("");
			}else{
				b.append(s);
			}
			b.append(connector);
		}
		if(connector!=null && !"".equals(connector)){
			return b.substring(0, b.length()-connector.length());
		}
		return b.toString();
	}
	
	/**
	 * 根据 模板 和 拼接符 生成 字符串
	 * 格式: {map.key}{ch}{map.value}{connector}{map.key}{ch}{map.value}
	 * @param map       取值
	 * @param model		生成顺序模板	
	 * @param ch		值连接符
	 * @param connector 字符拼接符
	 * @return
	 */
	public static String generateSign(Map<String, String> map, String[] model, String ch, String connector){
		if(map==null || map.size()<=0 || model==null || model.length<=0) return "";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<model.length;i++){
			sb.append(model[i]);
			sb.append(ch);
			Object value = map.get(model[i]);
			sb.append(value==null?"":value);
			sb.append(connector);
		}
		if(connector!=null && !"".equals(connector)){
			return sb.substring(0, sb.length()-connector.length());
		}
		return "";
	}

	/**
	 * 根据 模板 和 拼接符 生成 字符串
	 * 格式：{value}{connector}
	 * @param map       取值
	 * @param model		生成顺序模板
	 * @param connector 字符拼接符
	 * @return
	 */
	public static String generateSign(Map<String, String> map, String[] model, String connector){
		if(map==null || map.size()<=0 || model==null || model.length<=0) return "";
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<model.length;i++){
			Object value = map.get(model[i]);
			sb.append(value==null?"":value);
			sb.append(connector);
		}
		if(connector!=null && !"".equals(connector)){
			return sb.substring(0, sb.length()-connector.length());
		}
		return "";
	}


	/**
	 * 获取request 属性 转成map
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> requestToMap(HttpServletRequest request){
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
	 * 获取用户请求IP地址
	 * @param request
	 * @return
	 */
	public static String queryIp(HttpServletRequest request){
		String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip.indexOf(",")>=0){
        	for(String _ip : ip.split(",")){
        		if(_ip == null || _ip.length() == 0 || "unknown".equalsIgnoreCase(_ip)){
        			ip = _ip;
        			break;
        		}
        	}
        }
        return ip;
	}

}
