package com.hszsd.user.po;

import java.io.Serializable;
import java.math.BigDecimal;

import com.hszsd.user.dto.User;

public class UserModel extends User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 密码
	 */
	private String password;
	/**
	 * 锁定状态
	 */
	private BigDecimal status;
	/**
	 * 注销状态
	 */
	private BigDecimal islock;
	
	/**
	 * @return the 密码
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param 密码 the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the 锁定状态
	 */
	public BigDecimal getStatus() {
		return status;
	}
	/**
	 * @param 锁定状态 the status to set
	 */
	public void setStatus(BigDecimal status) {
		this.status = status;
	}
	/**
	 * @return the 注销状态
	 */
	public BigDecimal getIslock() {
		return islock;
	}
	/**
	 * @param 注销状态 the islock to set
	 */
	public void setIslock(BigDecimal islock) {
		this.islock = islock;
	}
	
}
