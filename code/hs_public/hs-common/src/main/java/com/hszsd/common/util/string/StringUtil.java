package com.hszsd.common.util.string;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串操作类 <br/>
 * 版权所有：贵州合石电子商务有限公司
 * @author 艾伍
 * @version 1.0.0
 */
public class StringUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5516511898651142445L;

	/**
	 * 功能：将驼峰标识方法的字符串转换为下划线标识法的字符串
	 * 
	 * @param param
	 *            驼峰标识字符串
	 * @return 下划线标识字符串
	 */
	public static String camelToUnderline(String param) {
		if (param == null || param.equals("")) {
			return "";
		}
		Pattern p = Pattern.compile("[A-Z]");
		StringBuilder builder = new StringBuilder(param);
		Matcher mc = p.matcher(param);
		int i = 0;
		while (mc.find()) {
			builder.replace(mc.start() + i, mc.end() + i, "_"
					+ mc.group().toLowerCase());
			i++;
		}
		if ('_' == builder.charAt(0)) {
			builder.deleteCharAt(0);
		}
		return builder.toString();
	}

	/**
	 * 功能：将下划线标识法的字符串转换为驼峰标识方法的字符串，首字母大写
	 * 
	 * @param param
	 *            下划线识字符串
	 * @return 驼峰标标识字符串
	 */
	public static String underlineToCamelFirstUpper(String param) {
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		for (String p : param.split("_")) {
			if (p.length() > 0) {
				p = p.toLowerCase();
				char first = p.charAt(0);
				p = "" + (char) (first - 32) + p.substring(1);
				builder.append(p);
			}
		}
		return builder.toString();
	}

	/**
	 * 功能：将下划线标识法的字符串转换为驼峰标识方法的字符串，首字母小写
	 * 
	 * @param param
	 *            下划线识字符串
	 * @return 驼峰标标识字符串
	 */
	public static String underlineToCamelFirstLower(String param) {
		if (param == null || param.equals("")) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		String params[] = param.split("_");
		for (int i = 0; i < params.length; i++) {
			if (params[i].length() > 0) {
				String p = params[i].toLowerCase();
				char first = p.charAt(0);
				if (i != 0) {
					p = "" + (char) (first - 32) + p.substring(1);
				}
				builder.append(p);
			}
		}
		return builder.toString();
	}

	/**
	 * 将str将多个分隔符进行切分，
	 * 
	 * 示例：StringTokenizerUtils.split("1,2,3,4",","); 返回: ["1","2","3","4"]
	 * 
	 * @param str 如："1,2,3,4",
	 * @param seperators ,切割关键字
	 * @return 返回字符数组
	 */
	@SuppressWarnings("all")
	public static String[] split(String str, String seperators) {
		StringTokenizer tokenlizer = new StringTokenizer(str, seperators);
		List result = new ArrayList();

		while (tokenlizer.hasMoreElements()) {
			Object s = tokenlizer.nextElement();
			result.add(s);
		}
		return (String[]) result.toArray(new String[result.size()]);
	}

	/**
	 * 将字符串转换为List对象
	 * 
	 * @param ids
	 *            格式为a,b,c,d英文下的“,”隔开
	 * @return 返回字符串数组
	 */
	public static List<String> getIdsToList(String ids) {
		List<String> listId = new ArrayList<String>();
		if (null != ids && !"".equals(ids)) {
			String[] _ids = ids.split(",");
			for (String string : _ids) {
				if (null != string && !"".equals(string)) {
					listId.add(string);
				}
			}
		}
		return listId;
	}

	/**
	 * 将list对象转换为用英文“,”隔开的字符串
	 * 
	 * @param list
	 *            字符串集合
	 * @return 返回英文“,”隔开的字符串
	 */
	public static String getListToIds(List<String> list) {
		String ids = null;
		if (null != list && list.size() > 0) {
			StringBuilder sbd = new StringBuilder();
			for (int i = 0; i < list.size(); i++) {
				sbd.append(list.get(i));
				sbd.append(",");
			}
			ids = sbd.toString();
			ids = ids.substring(0, ids.length() - 1);
		}
		return ids;
	}

	/**
	 * 字符串分页
	 * 
	 * @param str
	 *            需要分页的字符串，使用英文的","隔开
	 * @param page
	 *            每页多少条数据
	 * @return 返回分页后的List数据集合
	 */
	public static List<String> getPageIds(String str, int page) {
		List<String> listid = new ArrayList<String>();
		String ids[] = str.split(",");
		int len = ids.length;
		if (len > page) {
			int j = 0;
			String sid = "";
			for (int i = 0; i < len; i++) {
				if (j == page) {
					sid = sid.substring(0, sid.length() - 1);
					listid.add(sid);
					j = 0;
					sid = "";
				}
				sid += ids[i] + ",";
				j++;
				if ((i + 1) == len) {
					sid = sid.substring(0, sid.length() - 1);
					listid.add(sid);
				}
			}
		} else {
			listid.add(str);
		}
		return listid;
	}
}
