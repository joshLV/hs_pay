package com.hszsd.common.util;

import java.io.Serializable;

/**
 * 代码执行返回公共类
 * 
 * @author 艾伍
 * @version 1.0.0
 * 
 */
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7069833770848017493L;
	/**
	 * 返回代号
	 */
	private String code;
	/**
	 * 提示信息
	 */
	private String message;
	/**
	 * 业务数据
	 */
	private Object result;

	/**
	 * @return the 返回代号
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param 返回代号
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the 提示信息
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param 提示信息
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the 业务数据
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param 业务数据
	 *            the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

}
