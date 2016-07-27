package com.hszsd.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.Userlog;
import com.hszsd.entity.example.UserlogExample;

public interface UserlogMapper extends BaseDao {
    int countByExample(UserlogExample example);

    int deleteByExample(UserlogExample example);

    int deleteByPrimaryKey(Long logId);

    int insert(Userlog record);

    int insertSelective(Userlog record);

    List<Userlog> selectByExample(UserlogExample example);

    Userlog selectByPrimaryKey(Long logId);

    int updateByExampleSelective(@Param("record") Userlog record, @Param("example") UserlogExample example);

    int updateByExample(@Param("record") Userlog record, @Param("example") UserlogExample example);

    int updateByPrimaryKeySelective(Userlog record);

    int updateByPrimaryKey(Userlog record);
}