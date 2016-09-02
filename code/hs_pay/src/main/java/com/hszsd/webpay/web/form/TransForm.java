package com.hszsd.webpay.web.form;

import com.alibaba.dubbo.common.utils.StringUtils;

/**
 * 封装流水号和支付密码表单对象
 * Created by suocy on 2016/7/17.
 */
public class TransForm {

    String transId;//流水号
    String payPassword;//支付密码
	String operateType;//操作类型
    
    
	public String getTransId() {
		return transId;
	}
	public void setTransId(String transId) {
		this.transId = transId;
	}
	public String getPayPassword() {
		return payPassword;
	}
	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
}
