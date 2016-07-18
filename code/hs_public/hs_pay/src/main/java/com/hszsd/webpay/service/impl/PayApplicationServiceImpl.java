package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.dao.PaymentInterfaceDao;
import com.hszsd.webpay.service.PayApplicationService;
import com.hszsd.webpay.service.PayService;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付上下文服务业务层接口实现
 * Created by gzhengDu on 2016/7/4.
 */
@Service("PayApplicationService")
public class PayApplicationServiceImpl implements ApplicationContextAware, PayApplicationService {
    //支付具体实现集合
    private Map<String, PayService> payServiceMap = new HashMap<String, PayService>();

    //第三方接口信息集合
    private Map<String, PaymentInterfaceDTO> paymentInterfaceMap = new HashMap<String, PaymentInterfaceDTO>();

    @Autowired
    private PaymentInterfaceDao paymentInterfaceDao;

    /**
     * 初始化支付具体实现集合及第三方接口信息集合
     * @param applicationContext spring上下文
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //获取支付实现方式
        Map<String, PayService> payServiceMap = applicationContext.getBeansOfType(PayService.class);
        for(PayService payService: payServiceMap.values()){
            payServiceMap.put(payService.key(), payService);
        }

        //第三方接口信息集合
        List<PaymentInterfaceDTO> paymentInterfaceDTOList = paymentInterfaceDao.selectByCondition(null);
        for(PaymentInterfaceDTO paymentInterfaceDTO: paymentInterfaceDTOList){
            paymentInterfaceMap.put(String.valueOf(paymentInterfaceDTO.getId()), paymentInterfaceDTO);
        }
    }

    /**
     * 根据支付方式创建对应第三方支付服务对象
     * @param payType 支付方式
     * @return PayService 第三方支付服务对象
     */
    @Override
    public PayService createPayService(String payType) {
        return payServiceMap.get(payType);
    }

    /**
     * 根据支付方式创建第三方支付接口业务对象
     * @param payType 支付方式
     * @return PaymentInterfaceDTO 第三方支付业务对象
     */
    @Override
    public PaymentInterfaceDTO createPaymentInterface(String payType) {
        return paymentInterfaceMap.get(payType);
    }
}
