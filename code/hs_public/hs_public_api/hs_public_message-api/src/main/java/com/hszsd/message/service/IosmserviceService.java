package com.hszsd.message.service;

import com.hszsd.common.util.Result;
import com.hszsd.message.dto.SmsEntity;

public interface IosmserviceService {
	/**
	 * 发送短信
	 * @param smsEntity
	 * @return
	 */
	public Result SendSMS(SmsEntity smsEntity);
	
}
