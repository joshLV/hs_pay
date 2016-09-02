package com.hszsd.account.dao;

import com.hszsd.account.po.AccountBankPO;
import com.hszsd.dao.BaseDao;
import java.util.List;

import com.hszsd.entity.example.AccountBankExample;
import org.apache.ibatis.annotations.Param;

public interface AccountBankMapper extends BaseDao {
    int countByExample(AccountBankExample example);

    int deleteByExample(AccountBankExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AccountBankPO record);

    int insertSelective(AccountBankPO record);

    List<AccountBankPO> selectByExample(AccountBankExample example);

    AccountBankPO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AccountBankPO record, @Param("example") AccountBankExample example);

    int updateByExample(@Param("record") AccountBankPO record, @Param("example") AccountBankExample example);

    int updateByPrimaryKeySelective(AccountBankPO record);

    int updateByPrimaryKey(AccountBankPO record);
}