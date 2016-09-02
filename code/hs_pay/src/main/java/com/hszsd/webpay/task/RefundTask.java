package com.hszsd.webpay.task;

import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.condition.TradeRecordCondition;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易记录处理定时器
 * Created by suocy on 2016/8/4
 */

@Component("refundTask")
public class RefundTask {

    private static final Logger logger = LoggerFactory.getLogger(RefundTask.class);

    @Autowired
    TradeRecordDao tradeRecordDao;

    @Autowired
    private PaymentService paymentService;

    /**
     * 定时扫描交易记录表，对等待处理状态的余额退回、积分退回和组合退回三种交易类型进行处理(正宗商城)
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void doRefund(){
        logger.info("RefundTask is Starting.......");
        TradeRecordCondition condition = new TradeRecordCondition();
        ArrayList<Integer> operateTypes = new ArrayList<Integer>();
        operateTypes.add(GlobalConstants.TRADE_RECORD.REFUND);
        operateTypes.add(GlobalConstants.TRADE_RECORD.ADD_CREDIT);
        operateTypes.add(GlobalConstants.TRADE_RECORD.ASSEMBLE_REFUND);
        condition.or().andTradeStatusEqualTo(GlobalConstants.TRADE_RECORD.TRADE_STATUS_1).andOperateTypeIn(operateTypes);
        List<TradeRecordDTO> tradeRecordDTOs = tradeRecordDao.selectByCondition(condition);
        String transId = "";
        for(TradeRecordDTO tradeRecordDTO : tradeRecordDTOs){
            transId = tradeRecordDTO.getTransId();
            ResultInfo editTradeRecord = paymentService.editTradeRecordZZSC(transId);
            //处理成功
            if(editTradeRecord.getIsSuccess()){
                logger.info("RefundTask --------> TransId: {} deal with success",transId);
                continue;
            }
            //处理失败
            ResultConstants editResult = (ResultConstants)editTradeRecord.getResult();
            logger.info("RefundTask --------> TransId: {} deal with failed  because {}",transId,editResult.getMsg());

        }
        logger.info("RefundTask is completed.......");
    }

}
