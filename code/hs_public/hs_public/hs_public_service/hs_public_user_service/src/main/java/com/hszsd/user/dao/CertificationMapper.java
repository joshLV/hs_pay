package com.hszsd.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.Certification;
import com.hszsd.entity.example.CertificationExample;

public interface CertificationMapper extends BaseDao {
    int countByExample(CertificationExample example);

    int insert(Certification record);

    int insertSelective(Certification record);

    List<Certification> selectByExample(CertificationExample example);

    Certification selectByPrimaryKey(String userId);

    int updateByExampleSelective(@Param("record") Certification record, @Param("example") CertificationExample example);

    int updateByExample(@Param("record") Certification record, @Param("example") CertificationExample example);

    int updateByPrimaryKeySelective(Certification record);

    int updateByPrimaryKey(Certification record);
}