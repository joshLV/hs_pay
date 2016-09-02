package com.hszsd.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.Artificialaudit;
import com.hszsd.entity.example.ArtificialauditExample;

public interface ArtificialauditMapper extends BaseDao {
    int countByExample(ArtificialauditExample example);

    int insert(Artificialaudit record);

    int insertSelective(Artificialaudit record);

    List<Artificialaudit> selectByExample(ArtificialauditExample example);

    Artificialaudit selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Artificialaudit record, @Param("example") ArtificialauditExample example);

    int updateByExample(@Param("record") Artificialaudit record, @Param("example") ArtificialauditExample example);

    int updateByPrimaryKeySelective(Artificialaudit record);

    int updateByPrimaryKey(Artificialaudit record);
}