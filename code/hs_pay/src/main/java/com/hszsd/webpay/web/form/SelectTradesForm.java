package com.hszsd.webpay.web.form;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易记录查询表单对象
 * Created by suocy on 2016/8/19.
 */
public class SelectTradesForm implements Serializable {

    private static final long serialVersionUID = -1296028520048319172L;
	//当前页
	private int pageNum;
	//每页条数
	private int pageSize;
    //交易记录流水号
	private String transId;
	//用户编号
	private String userId;
	//最小操作金额
	private String minMoney;
	//最大操作金额
	private String maxMoney;
	//最小操作积分
	private String minCredit;
	//最大操作积分
	private String maxCredit;
	//手机号
	private String mobile;
	//来源方订单号
	private String orderId;
	//平台来源（SH:商城 P2P:p2p平台 H5:微信）
	private String sourceCode;
	//操作类型
    private String operateType;
	//订单类型
	private String orderType;
    //交易状态（1:等待处理 2:交易成功 3:交易失败）
    private String tradeStatus;
    //最小交易时间
    private String minTime;
    //最大交易时间
    private String maxTime;
    //排序字段
    private String orderBy;
    //升序asc，降序desc
    private String sort;

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

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

	public String getMinMoney() {
		return minMoney;
	}

	public void setMinMoney(String minMoney) {
		this.minMoney = minMoney;
	}

	public String getMaxMoney() {
		return maxMoney;
	}

	public void setMaxMoney(String maxMoney) {
		this.maxMoney = maxMoney;
	}

	public String getMinCredit() {
		return minCredit;
	}

	public void setMinCredit(String minCredit) {
		this.minCredit = minCredit;
	}

	public String getMaxCredit() {
		return maxCredit;
	}

	public void setMaxCredit(String maxCredit) {
		this.maxCredit = maxCredit;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getSourceCode() {
		return sourceCode;
	}

	public void setSourceCode(String sourceCode) {
		this.sourceCode = sourceCode;
	}

	public String getOperateType() {
		return operateType;
	}

	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	@Override
	public String toString() {
		return "SelectTradesForm{" +
				"pageNum=" + pageNum +
				", pageSize=" + pageSize +
				", transId='" + transId + '\'' +
				", userId='" + userId + '\'' +
				", minMoney='" + minMoney + '\'' +
				", maxMoney='" + maxMoney + '\'' +
				", minCredit='" + minCredit + '\'' +
				", maxCredit='" + maxCredit + '\'' +
				", mobile='" + mobile + '\'' +
				", orderId='" + orderId + '\'' +
				", sourceCode='" + sourceCode + '\'' +
				", operateType='" + operateType + '\'' +
				", orderType='" + orderType + '\'' +
				", tradeStatus='" + tradeStatus + '\'' +
				", minTime='" + minTime + '\'' +
				", maxTime='" + maxTime + '\'' +
				", orderBy='" + orderBy + '\'' +
				", sort='" + sort + '\'' +
				'}';
	}
}
