package com.hszsd.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.ArtificialAudit;
import com.hszsd.entity.example.ArtificialAuditExample;

public interface ArtificialAuditMapper extends BaseDao {
    int countByExample(ArtificialAuditExample example);

    int insert(ArtificialAudit record);

    int insertSelective(ArtificialAudit record);

    List<ArtificialAudit> selectByExample(ArtificialAuditExample example);

    ArtificialAudit selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ArtificialAudit record, @Param("example") ArtificialAuditExample example);

    int updateByExample(@Param("record") ArtificialAudit record, @Param("example") ArtificialAuditExample example);

    int updateByPrimaryKeySelective(ArtificialAudit record);

    int updateByPrimaryKey(ArtificialAudit record);
}