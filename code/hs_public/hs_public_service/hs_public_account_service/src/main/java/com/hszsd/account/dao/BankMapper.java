package com.hszsd.account.dao;

import com.hszsd.dao.BaseDao;
import java.util.List;

import com.hszsd.entity.Bank;
import com.hszsd.entity.example.BankExample;
import org.apache.ibatis.annotations.Param;

public interface BankMapper extends BaseDao {
    int countByExample(BankExample example);

    int deleteByExample(BankExample example);

    int deleteByPrimaryKey(String id);

    int insert(Bank record);

    int insertSelective(Bank record);

    List<Bank> selectByExample(BankExample example);

    Bank selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Bank record, @Param("example") BankExample example);

    int updateByExample(@Param("record") Bank record, @Param("example") BankExample example);

    int updateByPrimaryKeySelective(Bank record);

    int updateByPrimaryKey(Bank record);
}