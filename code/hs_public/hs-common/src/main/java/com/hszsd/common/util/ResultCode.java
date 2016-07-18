package com.hszsd.common.util;

public interface ResultCode {

	/**
	 * 执行成功
	 */
	public static final String RES_OK="001";
	/**
	 * 执行失败
	 */
	public static final String RES_NO="002";
	/**
	 * 参数缺失
	 */
	public static final String RES_NONULL="002_01";
	/**
	 * 系统错误
	 */
	public static final String RES_NOSYS="002_02";
	/**
	 * 最小长度不够
	 */
	public static final String RES_LENGTH_MIN="104_01";
	
	/**
	 * 超出长度
	 */
	public static final String RES_LENGTH_MAX = "104_02";
	
	
	/**
	 * 用户不存在
	 */
	public static final String USER_NO_USER = "100";
	/**
	 * 用户名不存在
	 */
	public static final String USER_NO_USERNAME = "100_01";

	/**
	 * 手机号不存在
	 */
	public static final String USER_NO_PHONE = "100_02";
	/**
	 * 转出用户不存在
	 */
	public static final String USER_NO_TOUSER = "100_03";
	
	/**
	 * 业务单号已经存在
	 * 
	 */
	public static final String ORDER_FORMCODE_YES = "100_08";
	/**
	 * 业务单号不存在
	 * 
	 */
	public static final String ORDER_FORMCODE_NO = "100_09";
	
	
	/**
	 * 用户被冻结
	 */
	public static final String USER_USER_LOCK = "101_01";
	/**
	 * 用户已注销
	 */
	public static final String USER_USER_OUT = "101_02";
	/**
	 * 密码错误
	 * 
	 */
	public static final String USER_PASSWORD_ERROR = "102_01";
	
	/**
	 * 支付密码错误
	 * 
	 */
	public static final String USER_PLYPASSWORD_ERROR = "102_02";
	
	/**
	 * 用户没有设置支付密码
	 * 
	 */
	public static final String USER_PLYPASSWORD_NULL = "102_03";

}
