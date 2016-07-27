package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.condition.TradeRecordCondition;
import com.hszsd.webpay.dao.TradeCallbackDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.TradeRecordPO;
import com.hszsd.webpay.service.TradeRecordService;
import com.hszsd.webpay.util.UUIDUtils;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by gzhengDu on 2016/7/26.
 */
@Service("tradeRecordService")
public class TradeRecordServiceImpl implements TradeRecordService{
    private static final Logger logger = LoggerFactory.getLogger(TradeRecordService.class);

    @Autowired
    TradeRecordDao tradeRecordDao;

    @Autowired
    TradeCallbackDao tradeCallbackDao;

    /**
     * 根据map中的数据创建交易记录信息并保存
     * @param map 封装交易相关信息
     * @return ResultInfo 处理结果对象
     */
    @Override
    public ResultInfo createTradeRecord(Map map) {
        logger.info("createTradeRecord is starting and map={}",map);
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordPO tradeRecordPO = new TradeRecordPO();
        try {
            BeanUtils.copyProperties(tradeRecordPO, map);
        } catch (Exception e){
            logger.error("createTradeRecord occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }
        tradeRecordPO.setTransId(UUIDUtils.getId(6,6));
        tradeRecordPO.setCreateDate(new Date());
        tradeRecordPO.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_1);
        //判断交易记录信息是否保存成功
        if(tradeRecordDao.insertSelective(tradeRecordPO) > 0){
            resultInfo.setIsSuccess(true);
            resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
            resultInfo.setParams(tradeRecordPO.getTransId());
            logger.info("createTradeRecord success");
            return resultInfo;
        }

        resultInfo.setIsSuccess(false);
        resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
        logger.info("createTradeRecord failed");
        return resultInfo;
    }

    /**
     * 交易流水号查询出指定交易记录
     * @param transId 交易流水号
     * @return ResultInfo 处理结果对象
     */
    @Override
    public ResultInfo queryTradeRecordByTransId(String transId) {
        logger.info("queryTradeRecordByTransId is starting and transId={}", transId);
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = null;
        try{
            tradeRecordDTO  = tradeRecordDao.selectByPrimaryKey(transId);
        }catch(Exception e){
            logger.error("queryTradeRecordByTransId occurs an error and cause by {}", e.getMessage());
        }finally {
            if(tradeRecordDTO == null){
                resultInfo.setIsSuccess(false);
                resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
                return resultInfo;
            }
        }

        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        resultInfo.setParams(tradeRecordDTO);
        logger.info("queryTradeRecordByTransId success and ResultInfo={}", resultInfo);
        return resultInfo;
    }

    /**
     * 根据查询条件查询出相关交易记录
     * @param condition 封装查询条件
     * @return ResultInfo 处理结果对象
     */
    @Override
    public ResultInfo queryTradeRecordByCondition(TradeRecordCondition condition) {
        ResultInfo resultInfo = new ResultInfo();
        List<TradeRecordDTO> tradeRecordDTOs = Collections.EMPTY_LIST;
        try{
            tradeRecordDTOs  = tradeRecordDao.selectByCondition(condition);
        }catch(Exception e){
            logger.error("queryTradeRecordByCondition occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        resultInfo.setParams(tradeRecordDTOs);
        return resultInfo;
    }

    /**
     * 根据交易流水号删除交易回调记录
     * @param transId 交易流水号
     */
    @Override
    public void delTradeCallbackByTransId(String transId) {
        tradeCallbackDao.deleteByPrimaryKey(transId);
    }
}
