package com.hszsd.rabbitmq.service;


import com.hszsd.common.util.Result;
import com.hszsd.rabbitmq.dto.RabbitMessageDTO;

/**
 * 消息发送接口<br/>
 * 贵州合石电子商务有限公司
 * 
 * @author yangwenjian
 * @since jdk 1.7 2016-07-22
 * @version V1.0.0
 * 
 */
public interface RabbitmqService {

	/**
	 * 给用户定时发送指定类型的消息<br/>
	 * 
	 * @param rabbitMessage   发送消息实体
	 *            
	 * @return 返回结果信息
	 *
	 */	
	public Result sendMessage(RabbitMessageDTO rabbitMessage);



}