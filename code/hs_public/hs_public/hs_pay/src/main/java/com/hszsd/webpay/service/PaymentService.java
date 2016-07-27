package com.hszsd.webpay.service;

import com.hszsd.common.util.Result;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.TransForm;

import java.util.Map;

/**
 * 交易记录业务层接口
 * Created by suocy on 2016/7/15.
 */
public interface PaymentService {

    /**
     * 创建交易记录
     * @return String 交易序号
     */
    boolean createTradeRecord(Map<String,String> map);

    /**
     * 修改交易记录状态，调用dubbox接口进行金额或积分增减操作
     * @return String 交易序号
     */
    ResultInfo editTradeRecord(TransForm transForm, int operateType);


    /**
     * 通过交易流水号，查询交易记录
     * @param transId
     * @return
     */
    ResultInfo queryTradeRecord(String transId);
}
