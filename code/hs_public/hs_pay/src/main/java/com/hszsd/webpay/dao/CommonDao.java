package com.hszsd.webpay.dao;

/**
 * 常用查询
 * Created by gzhengDu on 2016/7/5.
 */
public interface CommonDao {
    /**
     * 获取交易序号
     * 对应序列：HSPRD.SEQ_TRANS_ID
     * @return String 交易序号
     */
    String selectTransNo();
}
