package com.hszsd.webpay.service;

/**
 * 交易序号业务层接口
 * Created by gzhengDu on 2016/7/5.
 */
public interface TransIdService {
    /**
     * 初始化交易序号
     * @return String 交易序号
     */
    String initTransNo();
}
