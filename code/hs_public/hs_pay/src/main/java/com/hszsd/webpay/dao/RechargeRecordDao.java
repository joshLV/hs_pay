package com.hszsd.webpay.dao;

import com.hszsd.webpay.condition.RechargeRecordCondition;
import com.hszsd.webpay.po.RechargeRecordPO;
import com.hszsd.webpay.web.dto.RechargeRecordDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeRecordDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int countByCondition(RechargeRecordCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int deleteByCondition(RechargeRecordCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int insert(RechargeRecordPO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int insertSelective(RechargeRecordPO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    List<RechargeRecordDTO> selectByCondition(RechargeRecordCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    RechargeRecordPO selectByPrimaryKey(long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int updateByConditionSelective(@Param("record") RechargeRecordPO record, @Param("condition") RechargeRecordCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int updateByCondition(@Param("record") RechargeRecordPO record, @Param("condition") RechargeRecordCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(RechargeRecordPO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_RECHARGE_RECORD_028
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(RechargeRecordPO record);
}