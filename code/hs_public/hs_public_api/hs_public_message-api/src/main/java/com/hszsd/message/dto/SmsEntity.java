package com.hszsd.message.dto;

import java.io.Serializable;

/**
 * 短信信息类
 * 
 * @author 艾伍
 * @date 2015-05-27
 * @version 1.0.0
 */
public class SmsEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1902831854033004279L;
	/**
	 * 短信的文本内容
	 * 
	 */
	private String content;
	/**
	 * 接收手机号 ,多个之间使用;分割
	 */
	private String phoneName;

	/**
	 * @return 短信的文本内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param 短信的文本内容
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return 接收手机号多个之间使用;分割
	 */
	public String getPhoneName() {
		return phoneName;
	}

	/**
	 * @param 接收手机号多个之间使用
	 *            ;分割 the phoneName to set
	 */
	public void setPhoneName(String phoneName) {
		this.phoneName = phoneName;
	}

}
