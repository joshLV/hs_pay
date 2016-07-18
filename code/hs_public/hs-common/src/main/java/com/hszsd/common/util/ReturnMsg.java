package com.hszsd.common.util;

import java.io.Serializable;

/**
 * 执行请求返回格式 <br/>
 * 版权所有：贵州合石电子商务有限公司
 * 
 * @author 艾伍
 * @version 1.0.0
 * 
 */
public class ReturnMsg implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 提示信息
	 */
	private String message;
	/**
	 * 执行请求状态
	 */
	private boolean success;
	/**
	 * 额外参数
	 */
	private Object result;

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
	 * @return the 执行请求状态
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param 执行请求状态
	 *            the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the 额外参数
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param 额外参数 the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}
}
