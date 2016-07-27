package com.hszsd.common.util;

/**
 * common
 */
public interface ResultCode {


	/**
	 * 执行成功
	 */
	public static final String RES_OK="0000";
	/**
	 * 执行失败
	 */
	public static final String RES_NO="1000";
	/**
	 * 参数缺失
	 */
	public static final String RES_NONULL="2000";

	/**
	 * 参数格式错误
	 */
	public static final String RES_NOSYS="2001";


	/**
	 * 用户被冻结
	 */
	public static final String USER_USER_LOCK = "5000";
	/**
	 * 用户已注销
	 */
	public static final String USER_USER_OUT = "5004";

	/**
	 * 用户不存在
	 */
	public static final String USER_NO_USER = "5005";
	/**
	 * 用户名不存在
	 */
	public static final String USER_NO_USERNAME = "5006";

	/**
	 * 手机号不存在
	 */
	public static final String USER_NO_PHONE = "5007";
	/**
	 * 密码错误
	 *
	 */
	public static final String USER_PASSWORD_ERROR = "5008";

	/**
	 * 支付密码错误
	 *
	 */
	public static final String USER_PLYPASSWORD_ERROR = "5009";
	

	/**
	 * 用户没有设置支付密码
	 * 
	 */
	public static final String USER_PLYPASSWORD_NULL = "5010";


	/**
	 * 解密失败
	 */
	public static final String DESP_ERROR="5011";

	/**
	 * 邮箱不存在
	 */
	public static final String USER_NO_EMAIL="5012";

	/**
	 * 标题超出长度
	 */
	public static final String RES_LENGTH_MAX = "6000";

	/**
	 * 最小长度不够
	 */
	public static final String RES_LENGTH_MIN="6001";


	/**
	 * 内容超出长度
	 */
	public static final String CONTENT_LENGTH_MAX = "6003";

	/**
	 * 转出用户不存在
	 */
	public static final String USER_NO_TOUSER = "7000";

	/**
	 * 业务单号已经存在
	 *
	 */
	public static final String ORDER_FORMCODE_YES = "7001";
	/**
	 * 业务单号不存在
	 *
	 */
	public static final String ORDER_FORMCODE_NO = "7002";


	/**
	 * 等待处理
	 */
	public static final String WAIT_PROCESS = "9000";
}
