package com.hszsd.user.dao;

import java.util.List;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.SystemConfig;
import com.hszsd.entity.example.SystemConfigExample;

public interface SystemConfigMapper extends BaseDao {
    int countByExample(SystemConfigExample example);

    int insert(SystemConfig record);

    int insertSelective(SystemConfig record);

    List<SystemConfig> selectByExample(SystemConfigExample example);

    SystemConfig selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SystemConfig record);

    int updateByPrimaryKey(SystemConfig record);
}