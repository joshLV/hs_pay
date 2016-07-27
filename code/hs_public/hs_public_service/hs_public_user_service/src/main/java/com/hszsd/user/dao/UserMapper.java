package com.hszsd.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.User;
import com.hszsd.entity.example.UserExample;

public interface UserMapper extends BaseDao {
    int countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(String userId);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}