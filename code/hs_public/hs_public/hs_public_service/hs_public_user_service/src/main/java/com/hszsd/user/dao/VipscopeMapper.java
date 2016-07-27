package com.hszsd.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.Vipscope;
import com.hszsd.entity.example.VipscopeExample;

public interface VipscopeMapper extends BaseDao {
    int countByExample(VipscopeExample example);

    int deleteByExample(VipscopeExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Vipscope record);

    int insertSelective(Vipscope record);

    List<Vipscope> selectByExample(VipscopeExample example);

    Vipscope selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Vipscope record, @Param("example") VipscopeExample example);

    int updateByExample(@Param("record") Vipscope record, @Param("example") VipscopeExample example);

    int updateByPrimaryKeySelective(Vipscope record);

    int updateByPrimaryKey(Vipscope record);
}