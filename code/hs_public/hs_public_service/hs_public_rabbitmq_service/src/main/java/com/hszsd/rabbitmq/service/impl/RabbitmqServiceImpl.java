package com.hszsd.rabbitmq.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import com.hszsd.user.dto.PhoneMailDTO;
import com.hszsd.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.rabbitmq.dto.MessageEnumDTO;
import com.hszsd.rabbitmq.dto.RabbitMessageDTO;
import com.hszsd.rabbitmq.service.RabbitmqService;

/**
 * 消息发送接口实现类<br/>
 * 贵州合石电子商务有限公司
 * 
 * @author yangwenjian
 * @since 2016-07-08
 * @version 1.0.0
 * 
 */

@Service(value = "rabbitmqServiceImpl")
public class RabbitmqServiceImpl implements RabbitmqService {

	private Logger logger = LoggerFactory.getLogger(RabbitmqServiceImpl.class);

	@Resource
	private AmqpTemplate amqpTemplate;

	@Autowired
	private UserService userService;

	private static String RABBITMQ_USERNAME;

	@Value("#{settings['RABBITMQ_USERNAME']}")
	public void setRabbitmqUsername(String rabbitmqUsername) {
		RABBITMQ_USERNAME = rabbitmqUsername;
	}

	/**
	 * 给用户定时发送指定类型的消息<br/>
	 * 
	 * @param rabbitMessageDTO
	 *            发送消息实体
	 * 
	 * @return 返回结果信息
	 * 
	 */
	@Override
	public Result sendMessage(RabbitMessageDTO rabbitMessageDTO) {
		logger.info("sendMessage rabbitMessageDTO={}",
				rabbitMessageDTO.toString());
		Result result = new Result();
		// 消息内容
		String content = rabbitMessageDTO.getContent();
		// 消息类型
		List<MessageEnumDTO> messageType = rabbitMessageDTO.getMessageType();
		// 校验参数
		if (StringUtils.isEmpty(content) || messageType == null
				|| messageType.size() == 0) {
			logger.info("sendMessage content or messageType or messageType..size() or sendTime is null");
			result.setResCode(ResultCode.RES_NONULL);
			return result;
		}
		//没有查到的用户集合
		List<String> userIdNoList = new ArrayList<String>();

		//当传入用户ID时
		if(rabbitMessageDTO.getUserIds().size()>0) {

			Result rs=userService.getUserPhoneMail(rabbitMessageDTO.getUserIds());
			if(!rs.getResCode().equals(ResultCode.RES_OK)){
				logger.error("sendMessage userService.getUserPhoneMail is error");
				return rs;
			}
			List<PhoneMailDTO> listPM=(List<PhoneMailDTO>)rs.getResult();
			if(listPM.size()!=rabbitMessageDTO.getUserIds().size()){
				//总的用户List
				List<String> userIdList = rabbitMessageDTO.getUserIds();
				//存在的用户集合
				List<String> userIdYesList = new ArrayList<String>();
				for (String userId : userIdList) {
					Result rsUser = userService.getUserInfo(userId);
					if (rsUser.getResCode().equals(ResultCode.USER_NO_USER)) {
						userIdNoList.add(userId);
						continue;
					}else if (rsUser.getResCode().equals(ResultCode.RES_OK)) {
						userIdYesList.add(userId);
						continue;
					}else {
						logger.error("sendMessage userService.getUserInfo is error");
						return rs;
					}
				}
				rabbitMessageDTO.setUserIds(userIdYesList);
			}

		}
		//如果传入类型中有邮件则进行标题验证
		if(!(messageType.indexOf(MessageEnumDTO.MAIL)==-1)){
			if(StringUtils.isEmpty(rabbitMessageDTO.getTitle())) {
				logger.info("sendMessage type mail title is null");
				result.setResCode(ResultCode.RES_NONULL);
				result.setResMsg("type mail,title is null");
				return result;
			}
		}
		try {
			result.setResCode(ResultCode.RES_OK);
			if(rabbitMessageDTO.getUserIds()!=null&&rabbitMessageDTO.getUserIds().size()>0){
				logger.info("sendMessage is success");
				// 发送消息队列
				producer(rabbitMessageDTO);
			}
			//系统不存在的ID
			if(userIdNoList.size()>0){
				result.setResMsg("sendMessage System without this userId");
				result.setResult(userIdNoList);
			}
			return result;
		} catch (Exception e) {
			logger.error("sendMessage link rabbitMQ service error=",
					e.getMessage());
			result.setResCode(ResultCode.RES_NO);
			return result;
		}
	}

	/**
	 * 消息生产
	 * 
	 * @param message
	 * @throws Exception
	 */
	private void producer(RabbitMessageDTO message) throws Exception {
		logger.info("to send message:{}", message);

		MessagePostProcessor processor = new MessagePostProcessor() {
			@Override
			public Message postProcessMessage(Message message)
					throws AmqpException {
				// 此userId是消息生产者的账号
				message.getMessageProperties().setUserId(RABBITMQ_USERNAME);
				return message;
			}

		};

		for (MessageEnumDTO messageEnumDTO : message.getMessageType()) {
			amqpTemplate.convertAndSend(messageEnumDTO.toString(), message,
					processor);
		}

	}

}
