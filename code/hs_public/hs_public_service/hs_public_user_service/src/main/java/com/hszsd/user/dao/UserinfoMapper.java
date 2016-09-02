package com.hszsd.user.dao;

import com.hszsd.dao.BaseDao;

import java.util.List;

import com.hszsd.entity.Userinfo;
import com.hszsd.entity.example.UserinfoExample;
import org.apache.ibatis.annotations.Param;

public interface UserinfoMapper extends BaseDao {
    int countByExample(UserinfoExample example);

    int deleteByExample(UserinfoExample example);

    int deleteByPrimaryKey(String userId);

    int insert(Userinfo record);

    int insertSelective(Userinfo record);

    List<Userinfo> selectByExample(UserinfoExample example);

    Userinfo selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") Userinfo record, @Param("example") UserinfoExample example);

    int updateByExample(@Param("record") Userinfo record, @Param("example") UserinfoExample example);

    int updateByPrimaryKeySelective(Userinfo record);

    int updateByPrimaryKey(Userinfo record);
}