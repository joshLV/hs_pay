package com.hszsd.webpay.web.form;

/**
 * 获取确认绑卡表单对象
 * Created by suocy on 2016/8/5.
 */
public class BindCardConfirmForm {

    private String transId;//流水号
	private String userId;//用户编号
	private String smsCode;//验证码
	private String requestId;//绑卡请求号
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

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Override
	public String toString() {
		return "BindCardConfirmForm{" +
				"transId='" + transId + '\'' +
				", userId='" + userId + '\'' +
				", smsCode='" + smsCode + '\'' +
				", requestId='" + requestId + '\'' +
				", userIp='" + userIp + '\'' +

				'}';
	}
}
