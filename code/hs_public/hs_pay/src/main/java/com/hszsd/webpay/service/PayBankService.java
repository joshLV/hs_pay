package com.hszsd.webpay.service;

import java.util.Map;

/**
 * 支付银行信息业务层接口
 * Created by gzhengDu on 2016/6/28.
 */
public interface PayBankService {
    /**
     * 查询可用银行信息
     * @return Map<String, Object> 封装可用银行信息列表
     */
    Map<String, Object> queryPayBank();
}
