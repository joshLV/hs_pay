package com.hszsd.webpay.web.form;

/**
 * 获取请求绑卡表单对象
 * Created by suocy on 2016/8/5.
 */
public class BindCardForm {

    String transId;//流水号
	String userId;//用户编号
    String cardNo;//卡号
	String phone;//银行预留手机号
	String userIp;//用户ip地址
	String requestId;//绑卡请求号（重新发送验证码的时候）

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Override
	public String toString() {
		return "BindCardForm{" +
				"transId='" + transId + '\'' +
				", userId='" + userId + '\'' +
				", cardNo='" + cardNo + '\'' +
				", phone='" + phone + '\'' +
				", userIp='" + userIp + '\'' +
				", requestId='" + requestId + '\'' +
				'}';
	}
}
