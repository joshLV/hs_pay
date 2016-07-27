package com.hszsd.common.util;

import java.io.Serializable;
import java.security.MessageDigest;

/**
 * MD5码加密 <br/>
 * 版权所有：贵州合石电子商务有限公司
 * @author 艾伍
 * @version 1.0.0
 *
 */
public class MD5Util implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7639486271160103843L;

	/**
	 * 字符串进行MD5加密
	 * 
	 * @param origString
	 *            加密字符串
	 * @return 返回加密后的值
	 */
	public static String getMD5ofStr(String origString) {
		String origMD5 = null;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] result = md5.digest(origString.getBytes());
			origMD5 = byteArray2HexStr(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return origMD5;
	}

	/**
	 * 处理字节数组得到MD5密码的方法
	 * 
	 * @param bs
	 * @return
	 */
	private static String byteArray2HexStr(byte[] bs) {
		StringBuffer sb = new StringBuffer();
		for (byte b : bs) {
			sb.append(byte2HexStr(b));
		}
		return sb.toString();
	}

	/**
	 * 字节标准移位转十六进制方法
	 * 
	 * @param b
	 * @return
	 */
	private static String byte2HexStr(byte b) {
		String hexStr = null;
		int n = b;
		if (n < 0) {
			// 若需要自定义加密,请修改这个移位算法即可
			n = b & 0x7F + 128;
		}
		hexStr = Integer.toHexString(n / 16) + Integer.toHexString(n % 16);
		return hexStr.toUpperCase();
	}

	/**
	 * 提供一个MD5多次加密方法
	 * 
	 * @param origString
	 *            需要加密的字符串
	 * @param times
	 *            加密次数
	 * @return 返回加密后的值
	 */
	public static String getMD5ofStr(String origString, int times) {
		String md5 = getMD5ofStr(origString);
		if (times > 0) {
			for (int i = 0; i < times; i++) {
				md5 = getMD5ofStr(md5);
			}
		}
		return md5;
	}

	/**
	 * 密码验证方法
	 * 
	 * @param inputStr
	 *            待验证的值
	 * @param MD5Code
	 *            MD5的值
	 * @return 成功返回true,失败返回false
	 */
	public static boolean verifyPassword(String inputStr, String MD5Code) {
		return getMD5ofStr(inputStr).equals(MD5Code);
	}

	/**
	 * 多次加密时的密码验证方法
	 * 
	 * @param inputStr
	 *            待验证的值
	 * @param MD5Code
	 *            MD5的值
	 * @param times
	 *            加密次数
	 * @return 成功返回true,失败返回false
	 */
	public static boolean verifyPassword(String inputStr, String MD5Code,
			int times) {
		return getMD5ofStr(inputStr, times).equals(MD5Code);
	}
}
