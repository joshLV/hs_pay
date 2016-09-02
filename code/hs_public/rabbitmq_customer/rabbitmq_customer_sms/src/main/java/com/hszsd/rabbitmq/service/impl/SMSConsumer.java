package com.hszsd.rabbitmq.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hszsd.common.util.ObjectAndByte;
import com.hszsd.common.util.RegularValidate;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.string.StringUtil;
import com.hszsd.message.dto.SmsEntity;
import com.hszsd.message.service.IosmserviceService;
import com.hszsd.message.service.MessageSendService;
import com.hszsd.rabbitmq.dto.RabbitMessageDTO;
import com.hszsd.user.dto.PhoneMailDTO;
import com.hszsd.user.service.UserService;

/**
 * 消费者接收 <br/>
 * 短信发送消息
 * 
 * @author yangwenjian
 * @version V1.0.0
 */
@Component
public class SMSConsumer implements MessageListener {

	private Logger logger = LoggerFactory.getLogger(SMSConsumer.class);

	private final String DELIMITER = ",";

	private final String SMS_DELIMITER = ";";

	private final int pageRows = 1000;

	@Value("#{settings['CONTROL.SMS']}")
	private boolean CONTROL;
	@Autowired
	private MessageSendService messageSendService;

	@Autowired
	private IosmserviceService iosmserviceServiceImpl;

	@Autowired
	private UserService userServiceImpl;

	@Override
	public void onMessage(Message message) {
		if (!CONTROL) {
			logger.error("sendSMS CONTROL is false");
			return;
		}
		logger.info("SMSConsumer receive message:{}", message);
		byte[] body = message.getBody();
		// 定义为final 是因为在定时调度没执行之前不能改改变内容
		final RabbitMessageDTO rabbitMessageDTO;
		try {
			rabbitMessageDTO = (RabbitMessageDTO) ObjectAndByte.toObject(body);
		} catch (Exception e) {
			logger.error("rabbitMessageDTO Type conversion error ");
			return;
		}
		if (rabbitMessageDTO == null) {
			logger.error("rabbitMessageDTO is null ");
			return;
		}
		// 打印消息记录
		logger.info("rabbitMessageDTO ={}", rabbitMessageDTO.toString());

		MessageProperties properties = message.getMessageProperties();
		final String content = rabbitMessageDTO.getContent();
		if (properties == null || StringUtils.isEmpty(content)) {
			logger.error("properties or content is null ");
			return;
		}
		logger.info(" SMS-->{}----say:-->{}", properties.getUserId(),
				rabbitMessageDTO.getContent());
		// 当发送时间不为空或者当设置时间大于当前时间时定时发送
		if (rabbitMessageDTO.getSendTime() != null
				&& (rabbitMessageDTO.getSendTime()).after(new Date())) {
			logger.error("rabbitMessageDTO sendTime={}",
					rabbitMessageDTO.getSendTime());
			Timer t = new Timer();
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					sendSMS(rabbitMessageDTO);
				}
			};
			t.schedule(timerTask, rabbitMessageDTO.getSendTime());
		} else {
			sendSMS(rabbitMessageDTO);
		}
	}

	/**
	 * 发送业务逻辑处理
	 * 
	 * @param rabbitMessageDTO
	 *            发送信息实体
	 */
	private void sendSMS(RabbitMessageDTO rabbitMessageDTO) {
		logger.info("rabbitMessageDTO={}", rabbitMessageDTO.toString());
		// 如果前端未传递用户ID，则返回不进行短信发送
		if (rabbitMessageDTO.getUserIds().size() == 0) {
			logger.error("userIds  is null");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String userId : rabbitMessageDTO.getUserIds()) {
			sb.append(userId).append(DELIMITER);
		}
		// 将用户ID拼接为一个字符串
		String userIdArray = sb.toString().substring(0, sb.length() - 1);
		// 将用户ID进行分页
		List<String> list = StringUtil.getPageIds(userIdArray, pageRows);
		for (String userId : list) {
			// 将字符串转换为List
			List<String> userIdList = StringUtil.getIdsToList(userId);
			// 调用方法获取用户手机号
			Result result = userServiceImpl.getUserPhoneMail(userIdList);
			if (!result.getResCode().equals(ResultCode.RES_OK)) {
				logger.error(
						"userServiceImpl.getUserPhoneMail userId ={0} is error,errCode={1}",
						userId, ResultCode.RES_OK);
				continue;
			}
			// 将返回结果获取出集合
			List<PhoneMailDTO> phoneMailDTOs = (List<PhoneMailDTO>) result
					.getResult();
			StringBuilder phoneList = new StringBuilder();
			for (PhoneMailDTO phoneMailDTO : phoneMailDTOs) {
				// 读取用户手机号为空时跳出循环
				if (StringUtils.isEmpty(phoneMailDTO.getPhone())) {
					logger.error("userId ={} is error,phone is null", userId);
					continue;
				}
				// 去除空格
				String str = phoneMailDTO.getPhone().replace(" ", "");
				// 正则验证手机号是否正确
				if (!RegularValidate.isValidatePhoneRegular(str)) {
					logger.error(
							"userId ={} is error,phone={}  Validate is error",
							userId, str);
					continue;
				}
				phoneList.append(phoneMailDTO.getPhone()).append(SMS_DELIMITER);
			}
			if (StringUtils.isEmpty(phoneList.toString())) {
				logger.error("phoneList  is null");
				return;
			}
			String phoneString = phoneList.toString().substring(0,
					phoneList.length() - 1);

			SmsEntity smsEntity = new SmsEntity();
			smsEntity.setPhoneName(phoneString);
			smsEntity.setContent(rabbitMessageDTO.getContent());
			Result resultPhone =null;
			try {
				if ("shift".equals(rabbitMessageDTO.getTarget())) {
					resultPhone = iosmserviceServiceImpl.SendSMS(smsEntity);
				} else {
					resultPhone = messageSendService.sendSms(smsEntity);
				}
				if (resultPhone.getResCode().equals(ResultCode.RES_OK)) {
					if (rabbitMessageDTO.isWriteLogs()) {
						logger.warn(
								" sendSMS SUCCESS-------->rabbitMessageDTO={}----->smsEntity={} is success",
								JSONObject.fromObject(rabbitMessageDTO), JSONObject
										.fromObject(smsEntity).toString());
					}
				} else {
					if (rabbitMessageDTO.isWriteLogs()) {
						logger.error(
								"sendSMS ERROR-------->rabbitMessageDTO={}----->smsEntity={} is error",
								JSONObject.fromObject(rabbitMessageDTO), JSONObject
										.fromObject(smsEntity).toString());
					}
				}
			}catch (Exception e){
				logger.error("sendSMS  is error,msg={}",e.getMessage());
			}

		}
	}
}
