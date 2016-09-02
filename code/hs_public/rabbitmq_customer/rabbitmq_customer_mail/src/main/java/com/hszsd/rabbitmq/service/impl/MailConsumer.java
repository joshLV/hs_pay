package com.hszsd.rabbitmq.service.impl;


import com.hszsd.common.util.ObjectAndByte;
import com.hszsd.common.util.RegularValidate;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.string.StringUtil;
import com.hszsd.message.dto.SendMailMessage;
import com.hszsd.message.service.MessageSendService;
import com.hszsd.rabbitmq.dto.RabbitMessageDTO;
import com.hszsd.user.dto.PhoneMailDTO;
import com.hszsd.user.service.UserService;
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

import java.util.*;

/**
 * 消费者接收 <br/>
 * 邮件发送消息
 * @author yangwenjian
 * @version V1.0.0
 */
@Component
public class MailConsumer implements MessageListener {
	
	private Logger logger = LoggerFactory.getLogger(MailConsumer.class);

	@Autowired
	private MessageSendService messageSendServiceImpl;

	@Value("#{settings['CONTROL.MAIL']}")
	private boolean CONTROL;

	private final String DELIMITER=",";

	private final int pageRows=1000;

	@Autowired
	private UserService userServiceImpl;

	@Override
	public void onMessage(Message message) {
		if(!CONTROL){
			logger.error("MailConsumer CONTROL is false");
			return;
		}
		logger.info("MailConsumer receive message:{}",message);
		byte[] body = message.getBody();
        //定义为final 是因为在定时调度没执行之前不能改改变内容
		final RabbitMessageDTO rabbitMessageDTO;

		try {
			rabbitMessageDTO=(RabbitMessageDTO) ObjectAndByte.toObject(body);
		}catch (Exception e){
			logger.error("rabbitMessageDTO Type conversion error ");
			return;
		}
		if(rabbitMessageDTO==null){
			logger.info("rabbitMessageDTO is null ");
			return;
		}
		//打印消息记录
		logger.info("rabbitMessageDTO ={}",rabbitMessageDTO.toString());

		MessageProperties properties = message.getMessageProperties();
		final String content = rabbitMessageDTO.getContent();
		final String title=rabbitMessageDTO.getTitle();
		if(properties==null || StringUtils.isEmpty(content) ||StringUtils.isEmpty(title)){
			logger.info("properties or content or title is null ");
			return;
		}
		logger.info(" SMS-->{}----say:-->{}",properties.getUserId(),rabbitMessageDTO.getContent());
		//当发送时间不为空或者当设置时间大于当前时间时定时发送
		if(rabbitMessageDTO.getSendTime()!=null  && (rabbitMessageDTO.getSendTime()).after(new Date())){

			logger.info("rabbitMessageDTO sendTime={}",rabbitMessageDTO.getSendTime());
			Timer t = new Timer();
			TimerTask timerTask=new TimerTask() {
				@Override
				public void run() {
					sendMail(rabbitMessageDTO);
				}
			};
			t.schedule(timerTask, rabbitMessageDTO.getSendTime());
		}else{
			sendMail(rabbitMessageDTO);
		}
		
	}

	/**
	 * 进行消息发送
	 * @param rabbitMessageDTO
     */
	private void sendMail(RabbitMessageDTO rabbitMessageDTO){
		logger.info("rabbitMessageDTO={}",rabbitMessageDTO.toString());
		//如果前端未传递用户ID，则返回不进行邮件发送
		if(rabbitMessageDTO.getUserIds().size()==0){
			logger.error("userIds  is null,msssage={}","传递用户ID");
			return;
		}
		StringBuilder sb=new StringBuilder();
		for(String userId:rabbitMessageDTO.getUserIds()){
			sb.append(userId).append(DELIMITER);
		}
		//将用户ID拼接为一个字符串
		String userIdArray=sb.toString().substring(0,sb.length()-1);
		//将用户ID进行分页
		List<String> list=StringUtil.getPageIds(userIdArray,pageRows);
		for(String userId:list){
			//将字符串转换为List
			List<String> userIdList=StringUtil.getIdsToList(userId);
			//调用方法获取用户邮箱
			Result result=userServiceImpl.getUserPhoneMail(userIdList);
			if(!result.getResCode().equals(ResultCode.RES_OK)){
				logger.error("userServiceImpl.getUserPhoneMail userId ={0} is error,errCode={1}",userId,ResultCode.RES_OK);
				continue;
			}
			//将返回结果获取出集合
			List<PhoneMailDTO> phoneMailDTOs=(List<PhoneMailDTO>)result.getResult();
			StringBuilder emailList=new StringBuilder();
			for(PhoneMailDTO phoneMailDTO:phoneMailDTOs){
				//读取用户邮箱为空时跳出循环
				if(StringUtils.isEmpty(phoneMailDTO.getEmail())){
					logger.error("userId ={} is error,email is null",userId);
					continue;
				}
				//去除空格
				String str=phoneMailDTO.getEmail().replace(" ","");
				//正则验证邮箱是否正确
				if(!RegularValidate.isValidateEmailRegular(str)){
					logger.error("userId ={} is error,email={}  Validate is error",userId,str);
					continue;
				}
				emailList.append(phoneMailDTO.getEmail()).append(DELIMITER);
			}
			if(StringUtils.isEmpty(emailList.toString())){
				logger.error("emailList  is null");
				return;
			}
			String emailString=emailList.toString().substring(0,emailList.length()-1);
			SendMailMessage sendMailMessage=new SendMailMessage();
			//将发送邮箱List变为数组
			String[] mailArray=(emailString.split(DELIMITER));
			//接收者
			sendMailMessage.setTo(mailArray);
			//发送文本格式
			sendMailMessage.setHtml(true);
			//邮件内容
			sendMailMessage.setText(rabbitMessageDTO.getContent());
			//邮件标题
			sendMailMessage.setSubject(rabbitMessageDTO.getTitle());
			//邮件发送
			Result resultt=new Result();
			try {
				resultt=messageSendServiceImpl.sendMail(sendMailMessage);
				if(resultt.getResCode().equals(ResultCode.RES_OK)){
					if(rabbitMessageDTO.isWriteLogs()){
						logger.warn(" sendMail SUCCESS-------->rabbitMessageDTO={}----->sendMailMessage={} is success",JSONObject.fromObject(rabbitMessageDTO), JSONObject.fromObject(sendMailMessage).toString());
					}
				}else {
					if(rabbitMessageDTO.isWriteLogs()){
						logger.error("sendMail ERROR-------->rabbitMessageDTO={}----->sendMailMessage={} is error",JSONObject.fromObject(rabbitMessageDTO),JSONObject.fromObject(sendMailMessage).toString());
					}

				}
			}catch (Exception e){
				logger.error("sendMail is error");
			}

		}

	}


}
