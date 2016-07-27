package com.hszsd.user.dao;

import com.hszsd.dao.BaseDao;
import com.hszsd.entity.Vip;
import com.hszsd.entity.example.VipExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VipMapper extends BaseDao {
    int countByExample(VipExample example);

    int deleteByExample(VipExample example);

    int deleteByPrimaryKey(Long vipid);

    int insert(Vip record);

    int insertSelective(Vip record);

    List<Vip> selectByExample(VipExample example);

    Vip selectByPrimaryKey(Long vipid);

    int updateByExampleSelective(@Param("record") Vip record, @Param("example") VipExample example);

    int updateByExample(@Param("record") Vip record, @Param("example") VipExample example);

    int updateByPrimaryKeySelective(Vip record);

    int updateByPrimaryKey(Vip record);
}