package com.hszsd.rabbitmq.service.impl;

import com.hszsd.rabbitmq.dto.RabbitMessageDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 消费者接收 <br/>
 * APP发送消息
 * @author yangwenjian
 * @version V1.0.0
 */
@Component
public class APPConsumer implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(APPConsumer.class);
	@Resource
    ConnectionFactory connectionFactory;

	@Override
	public void onMessage(Message message) {
		logger.info("receive message:{}",message);
		byte[] body = message.getBody();
		RabbitMessageDTO rabbitMessage = (RabbitMessageDTO) SerializationUtils.deserialize(body);
		MessageProperties properties = message.getMessageProperties();
		String content = rabbitMessage.getContent();
		System.out.println("APP-->"+ properties.getUserId() + "发来信息说：" + content);
		
	}
	
}
