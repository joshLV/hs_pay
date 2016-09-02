package com.hszsd.webpay.web.form;

/**
 * 获取求情快捷卡支付表单对象
 * Created by suocy on 2016/8/5.
 */
public class BindCardPayForm {

    private String transId;//流水号
	private String userId;//用户编号
	private String id;//银行卡Id
	private String cardTop; //卡号前六位
	private String cardLast; //卡号后四位
	private String userIp;//用户ip地址


	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCardLast() {
		return cardLast;
	}

	public void setCardLast(String cardLast) {
		this.cardLast = cardLast;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCardTop() {
		return cardTop;
	}

	public void setCardTop(String cardTop) {
		this.cardTop = cardTop;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Override
	public String toString() {
		return "BindCardPayForm{" +
				"transId='" + transId + '\'' +
				", userId='" + userId + '\'' +
				", id='" + id + '\'' +
				", cardTop='" + cardTop + '\'' +
				", cardLast='" + cardLast + '\'' +
				", userIp='" + userIp + '\'' +
				'}';
	}
}
