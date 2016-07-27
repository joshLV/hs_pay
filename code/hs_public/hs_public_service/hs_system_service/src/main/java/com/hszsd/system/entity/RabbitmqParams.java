package com.hszsd.system.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 调用rebbitMQ消息队列生产接口的参数载体
 */
public class RabbitmqParams implements Serializable{

	private static final long serialVersionUID = -572816658230219190L;

	private String content; //消息内容  
	
	private MessageEnum[] messageType; //消息类型（通知、微信、邮件、短信、站内信、APP）
	
	private String[] userIds; //指定发送用户编号，可为空
	
	private String title = null; //邮件标题，可为空
	
	private Date sendTime = null; //消息发送时间，为空则立即发送；格式：yyyy-MM-dd HH:mm:ss
	
	private boolean isWriteLogs = true; //是否写入日志
	
	private Map<String, Object> map = null; //其他参数 


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	public String[] getUserIds() {
		return userIds;
	}

	public void setUserIds(String[] userIds) {
		this.userIds = userIds;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public boolean isWriteLogs() {
		return isWriteLogs;
	}

	public void setWriteLogs(boolean isWriteLogs) {
		this.isWriteLogs = isWriteLogs;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	
	

	
}
