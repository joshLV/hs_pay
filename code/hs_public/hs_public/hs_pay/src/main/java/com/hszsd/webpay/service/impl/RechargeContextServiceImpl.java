package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.service.RechargeApplicationService;
import com.hszsd.webpay.service.RechargeContextService;
import com.hszsd.webpay.service.RechargeService;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 充值业务层接口实现
 * Created by gzhengDu on 2016/6/28.
 */
@Service("rechargeContextService")
public class RechargeContextServiceImpl implements RechargeContextService {

    private static final Logger logger = LoggerFactory.getLogger(RechargeContextService.class);

    @Autowired
    private RechargeApplicationService rechargeApplicationService;

    /**
     * 根据调用模式类型进行不同充值操作
     * @param rechargeInDTO 充值数据对象
     * @param rechargeModel 调用模式
     * @return RechargeOutDTO 充值返回数据
     */
    @Override
    public RechargeOutDTO recharge(RechargeInDTO rechargeInDTO, RechargeModel rechargeModel) {
        RechargeOutDTO out = new RechargeOutDTO();
        //根据充值方式初始化对应充值服务
        RechargeService rechargeService = initRechargeService(rechargeInDTO);

        try{
            switch (rechargeModel){
                case RECHARGE://充值
                    out = rechargeService.recharge(rechargeInDTO);
                    break;
                case FRONT://前台回调
                    out = rechargeService.front(rechargeInDTO);
                    break;
                case BACK://后台回调
                    out = rechargeService.back(rechargeInDTO);
                    break;
                case QUERY://查询接口
                    out = rechargeService.query(rechargeInDTO);
                    break;
            }
        }catch (Exception e){
            logger.error("pay occurs an error and cause by {}", e.getMessage());
            out.setResult(ResultConstants.OPERATOR_FAIL);
        }
        return out;
    }

    /**
     * 根据充值方式初始化成具体充值实现对象(BaoFooRechargeService, HuichaoRechargeService, OnlineRechargeService等)
     * @param rechargeInDTO 充值数据对象
     * @return RechargeService 第三方充值业务层
     */
    @Override
    public RechargeService initRechargeService(RechargeInDTO rechargeInDTO) {
        RechargeService rechargeService = rechargeApplicationService.createPayService(rechargeInDTO.getType());
        if(rechargeService == null){
            //？默认走连连充值
            rechargeService = rechargeApplicationService.createPayService(RechargeType.LIANLIAN.getCode());
            rechargeInDTO.setBankAddr(rechargeInDTO.getType());
            rechargeInDTO.setType(RechargeType.LIANLIAN.getCode());
        }
        return rechargeService;
    }

    /**
     * 修改充值状态为失败
     * @param rechargeInDTO 充值入参
     * @return RechargeOutDTO 充值返回结果
     */
    @Override
    public RechargeOutDTO saveBackForFail(RechargeInDTO rechargeInDTO) {
        return null;
    }
}
