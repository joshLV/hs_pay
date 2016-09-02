package com.hszsd.account.dao;

import com.hszsd.account.dto.AccountBankDTO;
import com.hszsd.account.dto.AccountQuickBankDTO;
import com.hszsd.account.entity.AccountCommBankExample;
import com.hszsd.account.entity.AccountQuickCommBankExample;
import com.hszsd.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户账户信息公共查询dao
 * @author YangWenJian
 * @version V1.0.0
 */
@Repository
public interface AccountCommMapper extends BaseDao {

    /**
     * 获取用户绑定银行卡信息
     * @param accountExample 查询用户信息实体
     * @return
     */
    List<AccountBankDTO> queryAccountCommBank(AccountCommBankExample accountExample);


    /**
     * 获取用户快捷支付银行卡关系
     * @param accountQuickCommBankExample 查询用户信息实体
     * @return
     */
    List<AccountQuickBankDTO> queryAccountQuickCommBank(AccountQuickCommBankExample accountQuickCommBankExample);
}
