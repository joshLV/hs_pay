package com.hszsd.core.service;

import com.hszsd.entity.Account;
import com.hszsd.entity.AccountLog;
import com.hszsd.entity.Credit;
import com.hszsd.entity.CreditLog;
import com.hszsd.entity.RechargeRecord;

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
	 * 查询交易记录
	 * 
	 * @param tradeNo
	 * @return
	 */
	public RechargeRecord selectRechargeRecordByTradeNo(String tradeNo);

	/**
	 * 插入交易记录
	 * 
	 * @param rechargeRecord
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean insertRechargeRecord(RechargeRecord rechargeRecord) throws Exception;

	/**
	 * 更新交易记录
	 * 
	 * @param rechargeRecord
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean updateRechargeRecord(RechargeRecord rechargeRecord) throws Exception;

	/**
	 * 资金 同步增加： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * 
	 * @param accountLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addUseTotalMoney(AccountLog accountLog) throws Exception;

	/**
	 * 资金 同步增加： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * 
	 * @param accountLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addUseTotalMoney(AccountLog accountLog, String transId) throws Exception;

	/**
	 * 充值 资金 同步增加： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * 
	 * @param accountLog
	 * @param rechargeRecord
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addUseTotalMoney(AccountLog accountLog, RechargeRecord rechargeRecord, String transId) throws Exception;

	/**
	 * 充值 资金 同步增加： 冻结、总额
	 * addAccountLog 需包含 userId type money toUserId remark
	 * moveAccountLog 需包含 userId type money toUserId remark
	 * 
	 * @param accountLog
	 * @param rechargeRecord
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addNoUseTotalMoney(AccountLog addAccountLog, AccountLog moveAccountLog, RechargeRecord rechargeRecord, String transId) throws Exception;

	/**
	 * 资金 同步减少： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * 
	 * @param accountLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean subUseTotalMoney(AccountLog accountLog) throws Exception;

	/**
	 * 资金 同步减少： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * 
	 * @param accountLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean subUseTotalMoney(AccountLog accountLog, String transId) throws Exception;

	/**
	 * 积分 同步增加： 可用、总额
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param creditLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addUseTotalCredit(CreditLog creditLog) throws Exception;

	/**
	 * 积分 同步增加： 可用、总额
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param creditLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addUseTotalCredit(CreditLog creditLog, String transId) throws Exception;

	/**
	 * 积分 同步减少： 可用、总额
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param creditLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean subUseTotalCredit(CreditLog creditLog) throws Exception;

	/**
	 * 积分 同步减少： 可用、总额
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param creditLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean subUseTotalCredit(CreditLog creditLog, String transId) throws Exception;

	/**
	 * 资金 积分 同步增加： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param accountLog
	 * @param creditLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addUseTotal(AccountLog accountLog, CreditLog creditLog) throws Exception;

	/**
	 * 资金 积分 同步增加： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param accountLog
	 * @param creditLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean addUseTotal(AccountLog accountLog, CreditLog creditLog, String transId) throws Exception;

	/**
	 * 资金 积分 同步减少： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param accountLog
	 * @param creditLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean subUseTotal(AccountLog accountLog, CreditLog creditLog) throws Exception;

	/**
	 * 资金 积分 同步减少： 可用、总额
	 * accountLog 需包含 userId type money toUserId remark
	 * creditLog 需包含 userId typeId creditValue remark
	 * 
	 * @param accountLog
	 * @param creditLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean subUseTotal(AccountLog accountLog, CreditLog creditLog, String transId) throws Exception;

	/**
	 * 查询是否存在资金或积分日志记录
	 * 
	 * @param transId
	 * @return 成功
	 */
	public boolean selectTransLog(String transId);

	/**
	 * 同一账户内划转： 可用 --> 冻结
	 * accountLog 需包含 userId type money remark
	 * 
	 * @param accountLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean moveUseToNoUse(AccountLog accountLog) throws Exception;

	/**
	 * 同一账户内划转： 可用 --> 冻结
	 * accountLog 需包含 userId type money remark
	 * 
	 * @param accountLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean moveUseToNoUse(AccountLog accountLog, String transId) throws Exception;

	/**
	 * 同一账户内划转： 冻结 --> 可用
	 * accountLog 需包含 userId type money remark
	 * 
	 * @param accountLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean moveNoUseToUse(AccountLog accountLog) throws Exception;

	/**
	 * 同一账户内划转： 冻结 --> 可用
	 * accountLog 需包含 userId type money remark
	 * 
	 * @param accountLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean moveNoUseToUse(AccountLog accountLog, String transId) throws Exception;

	/**
	 * 跨账户划转： 冻结 --> 可用
	 * fromAccountLog 需包含 userId type money toUserId remark
	 * toAccountLog 需包含 userId type money remark
	 * 
	 * @param fromAccountLog
	 * @param toAccountLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean transeferNoUseToUse(AccountLog fromAccountLog, AccountLog toAccountLog) throws Exception;

	/**
	 * 跨账户划转： 冻结 --> 可用
	 * fromAccountLog 需包含 userId type money toUserId remark
	 * toAccountLog 需包含 userId type money remark
	 * 
	 * @param fromAccountLog
	 * @param toAccountLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean transeferNoUseToUse(AccountLog fromAccountLog, AccountLog toAccountLog, String transId) throws Exception;

	/**
	 * 跨账户划转： 可用 --> 可用
	 * fromAccountLog 需包含 userId type money toUserId remark
	 * toAccountLog 需包含 userId type money remark
	 * 
	 * @param fromAccountLog
	 * @param toAccountLog
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean transeferUseToUse(AccountLog fromAccountLog, AccountLog toAccountLog) throws Exception;

	/**
	 * 跨账户划转： 可用 --> 可用
	 * fromAccountLog 需包含 userId type money toUserId remark
	 * toAccountLog 需包含 userId type money remark
	 * 
	 * @param fromAccountLog
	 * @param toAccountLog
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean transeferUseToUse(AccountLog fromAccountLog, AccountLog toAccountLog, String transId) throws Exception;

	/**
	 * 充值跨账户划转： 可用 --> 可用
	 * addAccountLog 需包含 userId type money remark
	 * fromAccountLog 需包含 userId type money toUserId remark
	 * toAccountLog 需包含 userId type money remark
	 * 
	 * @param addAccountLog
	 * @param fromAccountLog
	 * @param toAccountLog
	 * @param rechargeRecord
	 * @param transId
	 * @return 成功
	 * @throws Exception 
	 */
	public boolean transeferUseToUse(AccountLog addAccountLog, AccountLog fromAccountLog, AccountLog toAccountLog, RechargeRecord rechargeRecord, String transId) throws Exception;
}
