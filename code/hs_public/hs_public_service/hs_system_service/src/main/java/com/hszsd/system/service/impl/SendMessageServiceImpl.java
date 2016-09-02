/*
package com.hszsd.system.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.message.dto.SendMailMessage;
import com.hszsd.message.dto.SmsEntity;
import com.hszsd.message.service.MessageSendService;
import com.hszsd.system.service.SendMessageService;
import com.hszsd.user.dto.UserInfoDTO;
import com.hszsd.user.service.UserService;


*/
/**
 * 消息发送
 * @author 艾伍
 *	贵州合石电子商务有限公司
 * @version 1.0.1
 * 修改查找用户，当不存在时的返回编码
 *//*

@Service(value = "sendMessageServiceImpl")
public class SendMessageServiceImpl implements SendMessageService {
	@Autowired
	private MessageSendService messageSendServiceImpl;
	
	@Autowired
	private UserService userServiceImpl;
	
	*/
/*@Autowired
	private TbUserMapper tbUserMapper;*//*


	*/
/**
	 * 发送手机短信
	 *//*

	@Override
	public Result sendPhoneMessage(String userName, String message) {
		Result res = sendChick(userName, message);
		UserInfoDTO user = (UserInfoDTO) res.getResult();
		if (ResultCode.RES_OK.equals(res.getResCode())) {
			// 检查用户手机号是否存在
			if (!StringUtils.isEmpty(user.getPhone())) {
				SmsEntity smsEntity = new SmsEntity();
				smsEntity.setContent(message);
				smsEntity.setPhoneName(user.getPhone());
				// 发送短信
				res = messageSendServiceImpl.sendSms(smsEntity);
			} else {
				res.setResCode(ResultCode.USER_NO_PHONE);
				return res;
			}
		}
		return res;
	}

	@Override
	public Result sendEmailMessage(String userName, String title, String message) {
		Result res = new Result();
		if (StringUtils.isEmpty(title)) {
			res.setResCode(ResultCode.RES_NONULL);
			return res;
		}else{
			if(title.length()>200){
				res.setResCode(ResultCode.RES_LENGTH_MAX);
				return res;
			}
		}
		res = sendChick(userName, message);
		UserInfoDTO user = (UserInfoDTO) res.getResult();
		if (ResultCode.RES_OK.equals(res.getResCode())) {
			// 检查用户手机号是否存在
			if (!StringUtils.isEmpty(user.getEmail())) {
				SendMailMessage msg = new SendMailMessage();
				msg.setHtml(true);
				msg.setTo(new String[] { user.getEmail() });
				msg.setSubject(title);
				msg.setText(message);
				res = messageSendServiceImpl.sendMail(msg);
			} else {
				res.setResCode(ResultCode.USER_NO_PHONE);
				return res;
			}
		}
		return res;
	}

	private Result sendChick(String userName, String message) {
		Result res = new Result();
		// 判断参数非空
		if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(message)) {
			res.setResCode(ResultCode.RES_NONULL);
			return res;
		}
		// 检查用户是否存在
		UserInfoDTO user = getTbUser(userName);
		if (null == user) {
			res.setResCode(ResultCode.USER_NO_USERNAME);
			return res;
		}
		res.setResCode(ResultCode.RES_OK);
		res.setResult(user);
		return res;
	}
	*/
/**
	 * 获取用户信息
	 * @param userName 用户名
	 * @return
	 *//*

	private UserInfoDTO getTbUser(String userName) {
		UserInfoDTO user = null;
		Result res=userServiceImpl.getUser(userName);
		if(ResultCode.RES_OK.equals(res.getResCode())){
			user=(UserInfoDTO)res.getResult();
		}
		return user;
	}
}
*/
