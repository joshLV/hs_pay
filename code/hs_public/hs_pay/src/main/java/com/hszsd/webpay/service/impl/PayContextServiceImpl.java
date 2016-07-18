package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.common.Model;
import com.hszsd.webpay.common.PayType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.service.PayApplicationService;
import com.hszsd.webpay.service.PayContextService;
import com.hszsd.webpay.service.PayService;
import com.hszsd.webpay.web.dto.PayInDTO;
import com.hszsd.webpay.web.dto.PayOutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 支付业务层接口实现
 * Created by gzhengDu on 2016/6/28.
 */
@Service("PayContextService")
public class PayContextServiceImpl implements PayContextService {

    private static final Logger logger = LoggerFactory.getLogger(PayContextService.class);

    @Autowired
    private PayApplicationService payApplicationService;

    /**
     * 根据调用模式类型进行不同支付操作
     * @param payInDTO 支付数据对象
     * @param model 调用模式
     * @return PayOutDTO 支付返回数据
     */
    @Override
    public PayOutDTO pay(PayInDTO payInDTO, Model model) {
        PayOutDTO out = new PayOutDTO();
        //根据支付方式初始化对应支付服务
        PayService payService = initPayService(payInDTO);

        try{
            switch (model){
                case PAY://支付
                    out = payService.pay(payInDTO);
                    break;
                case FRONT://前台回调
                    out = payService.front(payInDTO);
                    break;
                case BACK://后台回调
                    out = payService.back(payInDTO);
                    break;
                case QUERY://查询接口
                    out = payService.query(payInDTO);
                    break;
            }
        }catch (Exception e){
            logger.error("pay occurs an error and cause by {}", e.getMessage());
            out.setResult(ResultConstants.OPERATOR_FAIL);
        }
        return out;
    }

    /**
     * 根据支付方式初始化成具体支付实现对象(BaoFooPayService, HuichaoPayService, OnlinePayService等)
     * @param payInDTO 支付数据对象
     * @return PayService 第三方支付业务层
     */
    @Override
    public PayService initPayService(PayInDTO payInDTO) {
        PayService payService = payApplicationService.createPayService(payInDTO.getType());
        if(payService == null){
            //？默认走连连支付
            payService = payApplicationService.createPayService(PayType.LIANLIAN.getCode());
            payInDTO.setBankAddr(payInDTO.getType());
            payInDTO.setType(PayType.LIANLIAN.getCode());
        }
        return payService;
    }

    /**
     * 修改支付状态为失败
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付返回结果
     */
    @Override
    public PayOutDTO saveBackForFail(PayInDTO payInDTO) {
        return null;
    }
}
