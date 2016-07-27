package com.hszsd.webpay.task;

import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.dao.TradeCallbackDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.web.dto.TradeCallbackDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 定时任务管理
 * Created by gzhengDu on 2016/7/27.
 */
//@Component("scheduledTaskManager")
public class ScheduledTaskManager {
    @Autowired
    TradeRecordDao tradeRecordDao;

    @Autowired
    TradeCallbackDao tradeCallbackDao;

    /**
     * yib
     */
    @Scheduled(cron = "")
    public void doCallBack(){
        List<TradeCallbackDTO> tradeCallbackDTOList = tradeCallbackDao.selectByCondition(null);
        for(TradeCallbackDTO callbackDTO: tradeCallbackDTOList){
            if(callbackDTO.getCount()>0){
                TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(callbackDTO.getTransId());
                ResultConstants resEnum = ResultConstants.WAIT_OPERATOR;
                switch (tradeRecordDTO.getTradeStatus()){
                    case GlobalConstants.TRADE_RECORD.TRADE_STATUS_1:
                        resEnum = ResultConstants.WAIT_OPERATOR;
                        break;
                    case GlobalConstants.TRADE_RECORD.TRADE_STATUS_2:
                        resEnum = ResultConstants.OPERATOR_SUCCESS;
                        break;
                    case GlobalConstants.TRADE_RECORD.TRADE_STATUS_3:
                        resEnum = ResultConstants.OPERATOR_FAIL;
                        break;
                    default:
                        break;
                }

                //如果交易记录状态为待处理，则不进行异步回调
                if(ResultConstants.WAIT_OPERATOR.equals(resEnum)){
                    continue;
                }

                //封装返回参数信息
                Map<String, String> paramMap = new TreeMap<String, String>();
                paramMap.put("transId", tradeRecordDTO.getTransId());
                paramMap.put("money", String.valueOf(tradeRecordDTO.getMoney()));
                paramMap.put("tradeStatus", String.valueOf(tradeRecordDTO.getTradeStatus()));
                paramMap.put("resCode", resEnum.getCode());
                paramMap.put("resMsg", resEnum.getMsg());

                //生成签名数据
                String md5sign = Hsmd5Util.getInstance().generateMD5Sign(paramMap, GlobalConstants.COMMON.RECHARGE_RES, MerchantUtil.getInstance().getMerchantKey(tradeRecordDTO.getSourceCode()));
                HttpUtils.sendPostRequest(tradeRecordDTO.getReturnUrl(), paramMap, md5sign);
            }
        }

    }

    @Scheduled(cron = "")
    public void doRepairRechargeData(){

    }
}
