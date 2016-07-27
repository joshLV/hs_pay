package com.hszsd.system.service;

import com.hszsd.common.util.Result;

/**
 * 发送消息接口
 * @author 艾伍
 * @version 1.0.0
 *
 */
public interface SendMessageService {
	/**
	 * 使用用户名给手机发送短信
	 * 
	 * @param userName
	 *            用户名
	 * @param message
	 *            发送文本，70个汉字以内
	 * @return 001成功<br/>
	 *         002 失败<br/>
	 *         002_01 参数缺失<br/>
	 *         002_02 系统错误<br/>
	 *         100_01 用户名不存在<br/>
	 *         100_02 手机号不存在<br/>
	 *         104_02 超出长度
	 */
	public Result sendPhoneMessage(String userName, String message);

	/**
	 * 使用用户名给邮箱发送邮件，使用Html形式发送
	 * 
	 * @param userName
	 *            用户 名
	 * @param title
	 *            邮件标题 邮件主题200个汉字以内
	 * @param message
	 *            邮件内容
	 * @return 001成功<br/>
	 *         002 失败<br/>
	 *         002_01 参数缺失<br/>
	 *         002_02 系统错误<br/>
	 *         100_01 用户名不存在<br/>
	 *         100_03 邮箱不存在<br/>
	 *         104_02 标题超出长度
	 */
	public Result sendEmailMessage(String userName, String title, String message);
}
