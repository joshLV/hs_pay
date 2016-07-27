package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.dao.CommonDao;
import com.hszsd.webpay.service.TransIdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易序号业务层接口实现
 * Created by gzhengDu on 2016/7/5.
 */
@Service("transIdService")
public class TransIdServiceImpl implements TransIdService {
    private static final Logger logger = LoggerFactory.getLogger(TransIdService.class);

    @Autowired
    private CommonDao commonDao;

    /**
     * 初始化交易序号
     * @return String 交易序号
     */
    @Override
    public String initTransNo() {
        logger.info("initTransNo is starting");
        String transNo = commonDao.selectTransNo();
        return transNo;
    }
}
