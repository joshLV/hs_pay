package com.hszsd.message.util;

import com.hszsd.common.util.ResultCode;

/**
 * 邮件返回状态
 * @author 艾伍
 *	贵州合石电子商务有限公司
 * @version 1.0.0
 */
public class ResultMsgCode implements ResultCode {

	/**
	 * 没有收件人
	 */
	public final static String NULL_TO="404_";
	/**
	 * 没有邮件主题
	 */
	public final static String NULL_SUBJECT="404_";
	/**
	 * 别名错误
	 */
	public final static String ERROR_ALIAS="500_";
	/**
	 * 邮件设置错误
	 */
	public final static String ERROR_CONFIG_MAIL="500_";
	
	/**
	 * 附件错误
	 */
	public final static String ERRO_FILE="500_";
	
}
