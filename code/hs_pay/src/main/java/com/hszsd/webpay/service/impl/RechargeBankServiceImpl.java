package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.condition.BankInfoCondition;
import com.hszsd.webpay.condition.PaymentInterfaceCondition;
import com.hszsd.webpay.dao.PaymentInterfaceDao;
import com.hszsd.webpay.service.RechargeBankService;
import com.hszsd.webpay.web.dto.PaymentInterfaceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 充值银行信息业务逻辑层接口实现
 * Created by gzhengDu on 2016/6/28.
 */
@Service("rechargeBankService")
public class RechargeBankServiceImpl implements RechargeBankService {
    private static final Logger logger = LoggerFactory.getLogger(RechargeBankService.class);

    @Autowired
    private PaymentInterfaceDao paymentInterfaceDao;

    @Value("#{onlineBankProps}")
    private Properties onlineBanks;

    /**
     * 查询可用银行信息
     * @return Map<String, Object> 封装可用银行信息列表
     */
    @Override
    public Map<String, Object> queryRechargeBank() {
        logger.info("queryRechargeBank is starting");
        Map<String, Object> map = new HashMap<String, Object>();

        BankInfoCondition onlineBankCondition = new BankInfoCondition();
        onlineBankCondition.or().andIsEnablePcEqualTo(GlobalConstants.COMMON.IS_ENABLE);

        PaymentInterfaceCondition paymentInterfaceCondition = new PaymentInterfaceCondition();
        paymentInterfaceCondition.or().andIsEnableEqualTo(GlobalConstants.COMMON.IS_ENABLE);

        try {
            //可用第三方充值接口列表
            List<PaymentInterfaceDTO> paymentInterfaceDTOs = paymentInterfaceDao.selectByCondition(paymentInterfaceCondition);

            map.put("onlineBankList", onlineBanks.entrySet());
            map.put("paymentInterfaceList", paymentInterfaceDTOs);
        }catch (Exception e){
            logger.error("queryPayBank occurs an error cause by {}", e.getMessage());
        }
        logger.info("queryRechargeBank is success and map={}", map);
        return map;
    }
}
