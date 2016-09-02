package com.hszsd.account.service;

import com.hszsd.account.dto.AccountBankCommDTO;
import com.hszsd.account.dto.AccountBankDTO;
import com.hszsd.account.dto.AccountQuickBankDTO;
import com.hszsd.common.util.Result;

/**
 * 用户账户信息service接口
 * @author yangwenjian
 * @version V1.0.0
 */
public interface AccountService {


    /**
     * 获取用户绑定银行卡信息
     * @param accountBankDTO 查询信息实体
     * @return 2000 参数缺失
     *         0000 操作成功
     *         1000 操作失败
     */
    public Result queryAccountBank(AccountBankDTO accountBankDTO);



    /**
     * 获取用户快捷支付银行卡关系表
     * @param accountQuickBankDTO 查询信息实体
     * @return 2000 参数缺失
     *         0000 操作成功
     *         1000 操作失败
     */
    public Result queryAccountQuickBank(AccountQuickBankDTO accountQuickBankDTO);


    /**
     * 用户绑卡
     * @param accountBankCommDTO  用户绑卡实体信息
     * @return 2000 参数为空
     *         7004 用户已存在快捷卡
     *         0000 绑定成功
     *         5013 银行编号不存在
     */
    public Result updateUserAccountBankComm(AccountBankCommDTO accountBankCommDTO);
}
