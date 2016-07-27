package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.condition.OnlineBankCondition;
import com.hszsd.webpay.condition.PaymentInterfaceCondition;
import com.hszsd.webpay.dao.OnlineBankDao;
import com.hszsd.webpay.dao.PaymentInterfaceDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.service.RechargeBankService;
import com.hszsd.webpay.web.dto.OnlineBankDTO;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 充值银行信息业务逻辑层接口实现
 * Created by gzhengDu on 2016/6/28.
 */
@Service("rechargeBankService")
public class RechargeBankServiceImpl implements RechargeBankService {
    private static final Logger logger = LoggerFactory.getLogger(RechargeBankService.class);

    @Autowired
    private OnlineBankDao onlineBankDao;

    @Autowired
    private PaymentInterfaceDao paymentInterfaceDao;

    /**
     * 查询可用银行信息
     * @return Map<String, Object> 封装可用银行信息列表
     */
    @Override
    public Map<String, Object> queryRechargeBank() {
        logger.info("queryRechargeBank is starting");
        Map<String, Object> map = new HashMap<String, Object>();

        OnlineBankCondition onlineBankCondition = new OnlineBankCondition();
        onlineBankCondition.or().andIsEnableEqualTo(GlobalConstants.COMMON.IS_ENABLE);

        PaymentInterfaceCondition paymentInterfaceCondition = new PaymentInterfaceCondition();
        paymentInterfaceCondition.or().andStateEqualTo(GlobalConstants.COMMON.IS_ENABLE);

        try {
            //可用银行列表
            List<OnlineBankDTO> onlineBankDTOs = onlineBankDao.selectByCondition(onlineBankCondition);
            //可用第三方充值接口列表
            List<PaymentInterfaceDTO> paymentInterfaceDTOs = paymentInterfaceDao.selectByCondition(paymentInterfaceCondition);

            map.put("onlineBankList", onlineBankDTOs);
            map.put("paymentInterfaceList", paymentInterfaceDTOs);
        }catch (Exception e){
            logger.error("queryPayBank occurs an error cause by {}", e.getMessage());
        }
        logger.info("queryRechargeBank is success and map={}", map);
        return map;
    }
}
