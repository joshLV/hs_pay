package com.hszsd.webpay.service;

import com.hszsd.webpay.common.Model;
import com.hszsd.webpay.web.dto.PayInDTO;
import com.hszsd.webpay.web.dto.PayOutDTO;

/**
 * 支付业务层接口
 * Created by gzhengDu on 2016/6/28.
 */
public interface PayContextService {

    /**
     * 根据调用模式类型进行不同支付操作
     * @param payInDTO 支付数据对象
     * @param model 调用模式
     * @return PayOutDTO 支付返回数据
     */
    PayOutDTO pay(PayInDTO payInDTO, Model model);

    /**
     * 根据支付方式初始化成具体支付操作对象(BaoFooPayService, HuichaoPayService, OnlinePayService等)
     * @param payInDTO 支付数据对象
     * @return PayService 第三方支付业务层
     */
    PayService initPayService(PayInDTO payInDTO);

    /**
     * 修改支付状态为失败
     * @param payInDTO 支付入参
     * @return PayOutDTO 支付返回结果
     */
    PayOutDTO saveBackForFail(PayInDTO payInDTO);
}
