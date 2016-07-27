package com.hszsd.webpay.common;

import java.io.Serializable;

/**
 * 执行请求返回格式 <br/>
 *  Created by suocy on 2016/7/19
 * 
 */
public class ResultInfo implements Serializable {

	private static final long serialVersionUID = -8803709797173978773L;
	//操作结果信息
	private Object result;

	//执行请求结果状态
	private boolean isSuccess;

	//额外参数
	private Object params;

	public Object getResult() {
		return this.result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public boolean getIsSuccess() {
		return this.isSuccess;
	}

	public void setIsSuccess(boolean success) {
		this.isSuccess = success;
	}

	public Object getParams() {
		return params;
	}

	public void setParams(Object params) {
		this.params = params;
	}

	@Override
	public String toString() {
		return "ResultInfo{" +
				"result=" + result +
				", isSuccess=" + isSuccess +
				", params=" + params +
				'}';
	}
}
