package com.hszsd.message.util;

import java.io.Serializable;

/**
 * 短发发送配置
 * 
 * @author 艾伍
 *  贵州合石电子商务有限公司
 * @version 1.0.0
 */

public class SmsConfig implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7187600755398751827L;
	/**
	 * 发送地址
	 */
	private String smsUrl;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 账号对应的登录密码
	 */
	private String password;
	/**
	 * 默认长度
	 */
	private int maxLength = 70;
	/**
	 * 短信签名
	 */
	private String autograph;

	/**
	 * @return 发送地址
	 */
	public String getSmsUrl() {
		return smsUrl;
	}

	/**
	 * @param 发送地址
	 *            the smsUrl to set
	 */
	public void setSmsUrl(String smsUrl) {
		this.smsUrl = smsUrl;
	}

	/**
	 * @return 账号
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param 账号
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return 账号对应的登录密码
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param 账号对应的登录密码
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return 默认长度
	 */
	public int getMaxLength() {
		return maxLength;
	}

	/**
	 * @param 默认长度
	 *            the maxLength to set
	 */
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	/**
	 * @return 短信签名
	 */
	public String getAutograph() {
		return autograph;
	}

	/**
	 * @param 短信签名
	 *            the autograph to set
	 */
	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

}
