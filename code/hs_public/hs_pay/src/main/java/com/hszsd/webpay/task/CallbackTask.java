package com.hszsd.webpay.task;

import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.dao.RechargeRecordDao;
import com.hszsd.webpay.dao.TradeCallbackDao;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.TradeCallbackPO;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.web.dto.RechargeRecordDTO;
import com.hszsd.webpay.web.dto.TradeCallbackDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 回调任务定时器
 * Created by gzhengDu on 2016/7/27.
 */
//@Component("callbackTask")
public class CallbackTask {
    @Autowired
    TradeRecordDao tradeRecordDao;

    @Autowired
    TradeCallbackDao tradeCallbackDao;

    @Autowired
    RechargeRecordDao rechargeRecordDao;

    /**
     * 定时扫描交易回调表，并根据交易状态进行相关操作
     * 待处理：不进行异步调用，并将数据从交易回调表中删除
     * 交易成功、交易失败：进行商户异步通知，通知次数减一，当通知次数为0时将数据冲交易回调表中删除，不为0则更新通知次数
     * 充值成功，资金写入失败：进行数据修复，不进行商户异步通知
     */
    @Scheduled(cron = "")
    @Transaction
    public void doCallBack(){
        List<TradeCallbackDTO> tradeCallbackDTOList = tradeCallbackDao.selectByCondition(null);
        List<String> delCallbackRecords = new ArrayList<String>();
        List<TradeCallbackPO> updateCallbackPOs = new ArrayList<TradeCallbackPO>();
        TradeCallbackPO tempCallbackPO = new TradeCallbackPO();
        for(TradeCallbackDTO callbackDTO: tradeCallbackDTOList){
            if(callbackDTO.getCount()>0){
                TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(callbackDTO.getTransId());
                //如果交易记录状态为待处理，则不进行异步调用，并且在回调任务表中删除该条记录
                if(GlobalConstants.TRADE_RECORD.TRADE_STATUS_1 == tradeRecordDTO.getTradeStatus()){
                    delCallbackRecords.add(tradeRecordDTO.getTransId());
                    continue;
                }
                //如果交易记录状态为充值成功、资金写入失败，则进行修复数据，并不进行异步调用
                if(GlobalConstants.TRADE_RECORD.TRADE_STATUS_4 == tradeRecordDTO.getTradeStatus()){
                    repairRechargeData(tradeRecordDTO.getTransId());
                    continue;
                }
                //异步通知商户
                callMerchantBack(tradeRecordDTO);
            }
            if(callbackDTO.getCount()-1 == 0){
                delCallbackRecords.add(callbackDTO.getTransId());
                continue;
            }
            try {
                BeanUtils.copyProperties(tempCallbackPO, callbackDTO);
                tempCallbackPO.setCount(callbackDTO.getCount()-1);
                updateCallbackPOs.add(tempCallbackPO);
            }catch (Exception e){}
        }

        for(String transId: delCallbackRecords){
            tradeCallbackDao.deleteByPrimaryKey(transId);
        }
        for(TradeCallbackPO tradeCallbackPO: updateCallbackPOs){
            tradeCallbackDao.updateByPrimaryKeySelective(tradeCallbackPO);
        }
    }

    /**
     * 修复数据（更新充值记录表、添加资金记录信息、更新交易记录状态）
     * 1. 根据交易流水号查询充值记录并判断充值记录状态是否更改为充值成功,如果否 则调用dubbo“修改”充值记录状态为充值成功
     * 2. 根据交易流水号查询资金子路并判断资金记录有没有写入成功，如果否 则调用dubbo“增加”资金记录信息
     * 3. 判断dubbo是否调用成功，如果否 则返回不执行，等待下次回调时继续修复数据
     * 4. 根据交易流水号更改交易记录状态为成功。
     * @param transId 交易流水号
     */
    @Transaction
    public void repairRechargeData(String transId){
        //1. 查询充值记录并判断充值记录状态是否更改为充值成功,如果否 则调用dubbo“修改”充值记录状态为充值成功
        //2. 查询资金子路并判断资金记录有没有写入成功，如果否 则调用dubbo“增加”资金记录信息
        //3. 判断dubbo是否调用成功，如果否 则返回不执行，等待下次回调时继续修复数据
        //4. 更改交易记录状态为成功。
    }

    /**
     * 根据交易记录信息封装返回参数并对商户进行异步通知
     * @param tradeRecordDTO 交易记录信息
     */
    public void callMerchantBack(TradeRecordDTO tradeRecordDTO){
        //封装返回参数信息
        Map<String, String> paramMap = Collections.EMPTY_MAP;
        String type = null;
        switch (tradeRecordDTO.getOperateType()){
            case GlobalConstants.TRADE_RECORD.RECHARGE:
                paramMap = initCallbackData(tradeRecordDTO, new String[]{"transId", "money", "tradeStatus"});
                type = GlobalConstants.COMMON.RECHARGE_RES;
                break;
            case GlobalConstants.TRADE_RECORD.BALANCE_PAY:
                paramMap = initCallbackData(tradeRecordDTO, new String[]{"transId", "money", "tradeStatus"});
                type = GlobalConstants.COMMON.BALANCEPAY_RES;
                break;
            case GlobalConstants.TRADE_RECORD.REFUND:
                paramMap = initCallbackData(tradeRecordDTO, new String[]{"transId", "money", "tradeStatus"});
                type = GlobalConstants.COMMON.REFUND_RES;
                break;
            case GlobalConstants.TRADE_RECORD.CREDIT_PAY:
                paramMap = initCallbackData(tradeRecordDTO, new String[]{"transId", "money", "tradeStatus"});
                type = GlobalConstants.COMMON.CREDITPAY_RES;
                break;
            case GlobalConstants.TRADE_RECORD.ADD_CREDIT:
                paramMap = initCallbackData(tradeRecordDTO, new String[]{"transId", "money", "tradeStatus"});
                type = GlobalConstants.COMMON.ADDCREDIT_RES;
                break;
            case GlobalConstants.TRADE_RECORD.ASSEMBLE_PAY:
                paramMap = initCallbackData(tradeRecordDTO, new String[]{"transId", "money", "tradeStatus"});
                type = GlobalConstants.COMMON.ASSEMBLEPAY_RES;
                break;
            case GlobalConstants.TRADE_RECORD.ASSEMBLE_REFUND:
                paramMap = initCallbackData(tradeRecordDTO, new String[]{"transId", "money", "tradeStatus"});
                type = GlobalConstants.COMMON.ASSEMBLEREFUND_RES;
                break;
            default:
                break;
        }

        //生成签名数据
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(paramMap, type, MerchantUtil.getInstance().getMerchantKey(tradeRecordDTO.getSourceCode()));
        HttpUtils.sendPostRequest(tradeRecordDTO.getReturnUrl(), paramMap, md5sign);
    }

    /**
     * 根据交易记录信息及所需参数列表初始化返回参数
     * @param tradeRecordDTO 交易记录信息
     * @param params 参数列表
     * @return Map 返回参数
     */
    public Map initCallbackData(TradeRecordDTO tradeRecordDTO, String[] params){
        Map<String, String> paramMap = new TreeMap<String, String>();
        Map<String, String> objMap = BeanUtils.descriptor(tradeRecordDTO);
        for(String param: params){
            paramMap.put(param, objMap.get(param));
        }

        ResultConstants resEnum = ResultConstants.OPERATOR_FAIL;
        switch (tradeRecordDTO.getTradeStatus()){
            case GlobalConstants.TRADE_RECORD.TRADE_STATUS_2:
                resEnum = ResultConstants.OPERATOR_SUCCESS;
                break;
            case GlobalConstants.TRADE_RECORD.TRADE_STATUS_3:
                resEnum = ResultConstants.OPERATOR_FAIL;
                break;
            default:
                break;
        }
        paramMap.put("resCode", resEnum.getCode());
        paramMap.put("resMsg", resEnum.getMsg());
        return paramMap;
    }
}
