package com.hszsd.webpay.service;

import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;

/**
 * 充值业务层接口
 * Created by gzhengDu on 2016/6/28.
 */
public interface RechargeContextService {

    /**
     * 根据调用模式类型进行不同充值操作
     * @param rechargeInDTO 充值数据对象
     * @param rechargeModel 调用模式
     * @return RechargeOutDTO 充值返回数据
     */
    RechargeOutDTO recharge(RechargeInDTO rechargeInDTO, RechargeModel rechargeModel);

    /**
     * 根据充值方式初始化成具体充值操作对象(BaoFooRechargeService, HuichaoRechargeService, OnlineRechargeService等)
     * @param rechargeInDTO 充值数据对象
     * @return RechargeService 第三方充值业务层
     */
    RechargeService initRechargeService(RechargeInDTO rechargeInDTO);

}
