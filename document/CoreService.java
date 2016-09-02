package com.hszsd.service;

import java.math.BigDecimal;

import com.hszsd.entity.Account;
import com.hszsd.entity.AccountLog;
import com.hszsd.entity.Credit;
import com.hszsd.entity.CreditLog;

public interface CoreService {

	/**
	 * 根据 USER_ID 取得用户账户资金
	 * 
	 * @param userId USER_ID
	 * @return
	 */
	public Account selectAccountByUserId(String userId);

	/**
	 * 根据 USER_ID 取得用户积分
	 * 
	 * @param userId USER_ID
	 * @return
	 */
	public Credit selectCreditByUserId(String userId);

	/**
	 * 资金 同步增加： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * 
	 * @param amount 变动金额
	 * @param userId USER_ID
	 * @param accountLog
	 * @return 成功条数
	 */
	public int addUseTotalMoney(BigDecimal amount, String userId, AccountLog accountLog);

	/**
	 * 资金 同步减少： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * 
	 * @param amount 变动金额
	 * @param accountLog
	 * @param userId USER_ID
	 * @return 成功条数
	 */
	public int subUseTotalMoney(BigDecimal amount, String userId, AccountLog accountLog);
	
	/**
	 * 积分 同步增加： 可用、总额
	 *  creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param amount 变动量
	 * @param userId USER_ID
	 * @param creditLog
	 * @return 成功条数
	 */
	public int addUseTotalCredit(BigDecimal amount, String userId, CreditLog creditLog);

	/**
	 * 积分 同步减少： 可用、总额
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param amount 变动金额
	 * @param userId USER_ID
	 * @param creditLog
	 * @return 成功条数
	 */
	public int subUseTotalCredit(BigDecimal amount, String userId, CreditLog creditLog);

	/**
	 * 资金 积分 同步增加： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param amountMoney 变动资金
	 * @param amountCredit 变动积分
	 * @param userId USER_ID
	 * @param accountLog
	 * @param creditLog
	 * @return 成功条数
	 */
	public int addUseTotal(BigDecimal amountMoney, BigDecimal amountCredit, String userId, AccountLog accountLog, CreditLog creditLog);

	/**
	 * 资金 积分 同步减少： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param amountMoney 变动资金
	 * @param amountCredit 变动积分
	 * @param userId USER_ID
	 * @param accountLog
	 * @param creditLog
	 * @return 成功条数
	 */
	public int subUseTotal(BigDecimal amountMoney, BigDecimal amountCredit, String userId, AccountLog accountLog, CreditLog creditLog);
}
