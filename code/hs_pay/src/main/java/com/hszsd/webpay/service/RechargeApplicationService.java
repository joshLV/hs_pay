package com.hszsd.webpay.service;

import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;

/**
 * 充值上下文服务业务层接口
 * Created by gzhengDu on 2016/7/4.
 */
public interface RechargeApplicationService {

    /**
     * 根据充值方式创建对应第三方充值服务对象
     * @param rechargeType 充值方式
     * @return RechargeService 第三方充值服务
     */
    RechargeService createPayService(String rechargeType);

    /**
     * 根据充值方式创建第三方充值接口业务对象
     * @param rechargeType 充值方式
     * @return PaymentInterfaceDTO 第三方充值业务对象
     */
    PaymentInterfaceDTO createPaymentInterface(String rechargeType);
}
