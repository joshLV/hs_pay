package com.hszsd.webpay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Json 处理类
 * @author bin
 *
 */
public class JsonUtil {
	private static SerializeConfig mapping = new SerializeConfig();
	
	static{
		mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
	}
	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * includesProperties和excludesProperties两个参数同时使用时 只会序列化includesProperties的属性
	 * @param object 将要序列化的对象
	 * @param includesProperties 需要转换的属性
	 * @param excludesProperties 不需要转换的属性
	 */
	public static void writeJsonByFilter(Object object, String[] includesProperties, String[] excludesProperties, HttpServletResponse response) {
		try {
			FastjsonFilter filter = new FastjsonFilter();
			if (excludesProperties != null && excludesProperties.length > 0) {
				filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));
			}
			if (includesProperties != null && includesProperties.length > 0) {
				filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));
			}
			
			// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
			// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
			String json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);

			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(json);
			
			System.out.println("json:" + json);
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 此方法主要用作不启用web环境下的测试 
	 * @param object 将要序列化的对象
	 * @param includesProperties 需要转换的属性
	 * @param excludesProperties 不需要转换的属性
	 * @return
	 */
	public String writeJsonByFilter(Object object, String[] includesProperties, String[] excludesProperties){
		FastjsonFilter filter = new FastjsonFilter();// excludes优先于includes
		if (excludesProperties != null && excludesProperties.length > 0) {
			filter.getExcludes().addAll(Arrays.<String> asList(excludesProperties));
		}
		if (includesProperties != null && includesProperties.length > 0) {
			filter.getIncludes().addAll(Arrays.<String> asList(includesProperties));
		}
		
		// 使用SerializerFeature.WriteDateUseDateFormat特性来序列化日期格式的类型为yyyy-MM-dd hh24:mi:ss
		// 使用SerializerFeature.DisableCircularReferenceDetect特性关闭引用检测和生成
		String json = JSON.toJSONString(object, filter, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.DisableCircularReferenceDetect);

		return json;
		
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object 将要序列化的对象
	 * @throws IOException
	 */
	public static void writeJson(Object object, HttpServletResponse response) {
		writeJsonByFilter(object, null, null, response);
	}
	
	/**
	 * JSONObject通过param获取Value
	 * 
	 * @param object 将要序列化的对象
	 * @throws IOException
	 */
	public static String jsonGetStringAnalysis(JSONObject json, String param){
		String vaule = null;
		try {
			vaule = json.getString(param);
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
		return vaule;
	}
	/**
	 * 转换成JSONArray
	 * 
	 * @param object 将要序列化的对象
	 * @throws IOException
	 */
	public static JSONArray jsonGetJSONArrayAnalysis(JSONObject json, String param){
		JSONArray vaule = null;
		try {
			vaule = json.getJSONArray(param);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return vaule;
	}
	

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object 将要序列化的对象
	 * @param includesProperties 需要转换的属性
	 */
	public static void writeJsonByIncludesProperties(Object object, String[] includesProperties, HttpServletResponse response) {
		writeJsonByFilter(object, includesProperties, null, response);
	}

	/**
	 * 将对象转换成JSON字符串，并响应回前台
	 * 
	 * @param object 将要序列化的对象
	 * @param excludesProperties 不需要转换的属性
	 */
	public static void writeJsonByExcludesProperties(Object object, String[] excludesProperties, HttpServletResponse response) {
		writeJsonByFilter(object, null, excludesProperties, response);
	}
	
	
	/**
	 * javaBean、list、map convert to json string
	 */
	public static String obj2json(Object obj){
		return JSON.toJSONString(obj,mapping);
	}
	
	/**
	 * json string convert to javaBean、map
	 */
	public static <T> T json2obj(String jsonStr,Class<T> clazz){
		return JSON.parseObject(jsonStr,clazz);
	}
	
	/**
	 * json array string convert to list with javaBean
	 */
	public static <T> List<T> json2list(String jsonArrayStr,Class<T> clazz){
		return JSON.parseArray(jsonArrayStr, clazz);
	}
	
	/**
	 * json string convert to map
	 */
	@SuppressWarnings("unchecked")
	public static <T> Map<String,Object> json2map(String jsonStr){
		return json2obj(jsonStr, Map.class);
	}
	
	/**
	 * json string convert to map with javaBean
	 */
	public static <T> Map<String,T> json2map(String jsonStr,Class<T> clazz){
		Map<String,T> map = JSON.parseObject(jsonStr, new TypeReference<Map<String, T>>() {});
		for (Entry<String, T> entry : map.entrySet()) {
			JSONObject obj = (JSONObject) entry.getValue();
			map.put(entry.getKey(), JSONObject.toJavaObject(obj, clazz));
		}
		return map;
	}
}
