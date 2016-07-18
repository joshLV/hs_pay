package com.hszsd.message.service;

import com.hszsd.common.util.Result;
import com.hszsd.message.dto.SendMailMessage;
import com.hszsd.message.dto.SmsEntity;

/**
 * 邮件发送接口API
 * 
 * @author 艾伍 贵州合石电子商务有限公司
 * @version 1.0.0
 */
public interface MessageSendService {

	/**
	 *  邮件发送
	 * @param msg 发送邮件参数
	 *            
	 * @return
	 */
	public Result sendMail(SendMailMessage msg);

	/**
	 * 
	 * 
	 * @param mobilePhone
	 *            需要发送的手机号实体
	 * @param message
	 *            发送内容
	 * @return
	 */
	/**
	 * 发送手机短信
	 * @param smsEntity 手机发送参数实体
	 * @return
	 */
	public Result sendSms(SmsEntity smsEntity);

}
