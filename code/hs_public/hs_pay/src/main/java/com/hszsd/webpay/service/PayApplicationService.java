package com.hszsd.webpay.service;

import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;

/**
 * 支付上下文服务业务层接口
 * Created by gzhengDu on 2016/7/4.
 */
public interface PayApplicationService {

    /**
     * 根据支付方式创建对应第三方支付服务对象
     * @param payType 支付方式
     * @return PayService 第三方支付服务
     */
    PayService createPayService(String payType);

    /**
     * 根据支付方式创建第三方支付接口业务对象
     * @param payType 支付方式
     * @return PaymentInterfaceDTO 第三方支付业务对象
     */
    PaymentInterfaceDTO createPaymentInterface(String payType);
}
