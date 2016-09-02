package com.hszsd.account.dao;

import com.hszsd.account.po.AccountQuickBankPO;
import com.hszsd.dao.BaseDao;

import com.hszsd.entity.example.AccountQuickBankExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AccountQuickBankMapper extends BaseDao {
    int countByExample(AccountQuickBankExample example);

    int deleteByExample(AccountQuickBankExample example);

    int deleteByPrimaryKey(String id);

    int insert(AccountQuickBankPO record);

    int insertSelective(AccountQuickBankPO record);

    List<AccountQuickBankPO> selectByExample(AccountQuickBankExample example);

    AccountQuickBankPO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") AccountQuickBankPO record, @Param("example") AccountQuickBankExample example);

    int updateByExample(@Param("record") AccountQuickBankPO record, @Param("example") AccountQuickBankExample example);

    int updateByPrimaryKeySelective(AccountQuickBankPO record);

    int updateByPrimaryKey(AccountQuickBankPO record);
}