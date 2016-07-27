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
	private String resCode;
	/**
	 * 提示信息
	 */
	private String resMsg;
	/**
	 * 业务数据
	 */
	private Object result;

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getResMsg() {
		return resMsg;
	}

	public void setResMsg(String resMsg) {
		this.resMsg = resMsg;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}
