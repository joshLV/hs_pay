package com.hszsd.webpay.task;

import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.dao.TradeCallbackDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.TradeCallbackPO;
import com.hszsd.webpay.service.CommonService;
import com.hszsd.webpay.web.dto.TradeCallbackDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 回调任务定时器
 * Created by gzhengDu on 2016/7/27.
 */

@Component("callbackTask")
public class CallbackTask {
    private static final Logger logger = LoggerFactory.getLogger(CallbackTask.class);

    @Autowired
    private CommonService commonService;

    @Autowired
    private TradeRecordDao tradeRecordDao;

    @Autowired
    private TradeCallbackDao tradeCallbackDao;

    /**
     * 定时扫描交易回调表，并根据交易状态进行相关操作
     * 待处理：不进行异步调用，并将数据从交易回调表中删除
     * 交易成功、交易失败：进行商户异步通知，通知次数减一，当通知次数为0时将数据冲交易回调表中删除，不为0则更新通知次数
     * 充值成功，资金写入失败：进行数据修复，不进行商户异步通知
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    @Transactional
    public void doCallBack(){
        logger.info("doCallBack is starting");
        List<TradeCallbackDTO> tradeCallbackDTOList = tradeCallbackDao.selectByCondition(null);
        List<String> delCallbackRecords = new ArrayList<String>();
        List<TradeCallbackPO> updateCallbackPOs = new ArrayList<TradeCallbackPO>();
        TradeCallbackPO tempCallbackPO = new TradeCallbackPO();
        TradeRecordDTO tempTradeRecordDTO;
        String backMessage = null;
        try{
            for(TradeCallbackDTO callbackDTO: tradeCallbackDTOList){
                if(callbackDTO.getCount()>0){
                    tempTradeRecordDTO = tradeRecordDao.selectByPrimaryKey(callbackDTO.getTransId());
                    //如果交易记录不存在或状态为待处理，则不进行异步调用，并且在回调任务表中删除该条记录
                    if(tempTradeRecordDTO == null || GlobalConstants.TRADE_RECORD.TRADE_STATUS_1 == tempTradeRecordDTO.getTradeStatus()){
                        delCallbackRecords.add(callbackDTO.getTransId());
                        continue;
                    }
                    /*//如果交易记录状态为充值成功、资金写入失败，则进行修复数据，并不进行异步调用
                    if(GlobalConstants.TRADE_RECORD.TRADE_STATUS_4 == tradeRecordDTO.getTradeStatus()){
                        repairRechargeData(tradeRecordDTO.getTransId());
                        continue;
                    }*/
                    //异步通知商户
                    backMessage = commonService.asyncNoticeMerchant(tempTradeRecordDTO);
                }
                //商户返回确认消息或回调次数已用完，删除回调记录信息
                if((backMessage!=null && GlobalConstants.COMMON.BACK_MESSAGE.equals(backMessage.toUpperCase()))
                    || callbackDTO.getCount()-1 == 0){
                    delCallbackRecords.add(callbackDTO.getTransId());
                    continue;
                }
                BeanUtils.copyProperties(callbackDTO, tempCallbackPO);
                tempCallbackPO.setCount(callbackDTO.getCount()-1);
                updateCallbackPOs.add(tempCallbackPO);
            }

            for(String transId: delCallbackRecords){
                tradeCallbackDao.deleteByPrimaryKey(transId);
            }
            for(TradeCallbackPO tradeCallbackPO: updateCallbackPOs){
                tradeCallbackDao.updateByPrimaryKeySelective(tradeCallbackPO);
            }
        }catch (Exception e){
            logger.error("doCallBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 修复数据（更新充值记录表、添加资金记录信息、更新交易记录状态）
     * 1. 根据交易流水号查询充值记录并判断充值记录状态是否更改为充值成功,如果否 则调用dubbo“修改”充值记录状态为充值成功
     * 2. 根据交易流水号查询资金记录并判断资金记录有没有写入成功，如果否 则调用dubbo“增加”资金记录信息
     * 3. 判断dubbo是否调用成功，如果否 则返回不执行，等待下次回调时继续修复数据
     * 4. 根据交易流水号更改交易记录状态为成功。
     * @param transId 交易流水号
     */
    @Transactional
    public void repairRechargeData(String transId){
        //1. 查询充值记录并判断充值记录状态是否更改为充值成功,如果否 则调用dubbo“修改”充值记录状态为充值成功
        //2. 查询资金记录并判断资金记录有没有写入成功，如果否 则调用dubbo“增加”资金记录信息
        //3. 判断dubbo是否调用成功，如果否 则返回不执行，等待下次回调时继续修复数据
        //4. 更改交易记录状态为成功。
    }
}
