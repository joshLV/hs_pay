package com.hszsd.rabbitmq.dto;

import java.io.Serializable;
import java.util.*;

/**
 * 调用rebbitMQ消息队列生产接口的消息载体
 */
public class RabbitMessageDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5703179946659660423L;

	private String content; // 消息内容

	private List<MessageEnumDTO> messageType; // 消息类型（通知、微信、邮件、短信、站内信、APP）

	private List<String> userIds; // 指定发送用户编号，可为空

	private String title = null; // 邮件标题，可为空

	private Date sendTime = null; // 消息发送时间，为空则立即发送；格式：yyyy-MM-dd HH:mm:ss

	private boolean isWriteLogs = true; // 是否写入日志

	private Map<String, Object> map = null; // 其他参数

	/**
	 * 消息 发送目标
	 */
	private String target;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<MessageEnumDTO> getMessageType() {
		return messageType;
	}

	public void setMessageType(List<MessageEnumDTO> messageType) {
		this.messageType = messageType;
	}

	public List<String> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<String> userIds) {
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

	public String getTarget() {
		return target;
	}

	@Override
	public String toString() {
		return "RabbitMessageDTO [content=" + content + ", messageType="
				+ messageType + ", userIds=" + userIds + ", title=" + title
				+ ", sendTime=" + sendTime + ", isWriteLogs=" + isWriteLogs
				+ ", map=" + map + ", target=" + target + "]";
	}

	public void setTarget(String target) {
		this.target = target;
	}
}
