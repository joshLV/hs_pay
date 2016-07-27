package com.hszsd.system.entity;

/**
 * 消息发送
 * 
 * @author zsd_aw
 *
 */
public enum MessageEnum {
	WEIXIN("微信", 1), SMS("短信", 2), MAIL("邮件", 3), APP("移动端", 4), STATION("站内信", 5), ORTHER("其他", 0);

	private String value;
	private int index;
/*	private MessageEnum() {
		
	}*/
	private MessageEnum(String value, int index) {
		this.value = value;
		this.index = index;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
