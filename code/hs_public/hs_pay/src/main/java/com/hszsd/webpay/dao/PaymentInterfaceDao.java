package com.hszsd.webpay.dao;

import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import com.hszsd.webpay.po.PaymentInterfacePO;
import com.hszsd.webpay.condition.PaymentInterfaceCondition;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PaymentInterfaceDao {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int countByCondition(PaymentInterfaceCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int deleteByCondition(PaymentInterfaceCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int deleteByPrimaryKey(long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int insert(PaymentInterfacePO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int insertSelective(PaymentInterfacePO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    List<PaymentInterfaceDTO> selectByCondition(PaymentInterfaceCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    PaymentInterfacePO selectByPrimaryKey(long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int updateByConditionSelective(@Param("record") PaymentInterfacePO record, @Param("condition") PaymentInterfaceCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int updateByCondition(@Param("record") PaymentInterfacePO record, @Param("condition") PaymentInterfaceCondition condition);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int updateByPrimaryKeySelective(PaymentInterfacePO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table HSPRD.TB_PAYMENT_INTERFACE_035
     *
     * @mbggenerated
     */
    int updateByPrimaryKey(PaymentInterfacePO record);
}