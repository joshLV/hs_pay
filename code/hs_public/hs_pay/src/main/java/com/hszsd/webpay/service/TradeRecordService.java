package com.hszsd.webpay.service;

import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.condition.TradeRecordCondition;

import java.util.Map;

/**
 * 交易记录业务逻辑层接口
 * Created by gzhengDu on 2016/7/26.
 */
public interface TradeRecordService {
    /**
     * 根据map中的数据创建交易记录信息并保存
     * @param map 封装交易相关信息
     * @return ResultInfo 处理结果对象
     */
    ResultInfo createTradeRecord(Map map);

    /**
     * 交易流水号查询出指定交易记录
     * @param transId 交易流水号
     * @return ResultInfo 处理结果对象
     */
    ResultInfo queryTradeRecordByTransId(String transId);

    /**
     * 根据查询条件查询出相关交易记录
     * @param condition 封装查询条件
     * @return ResultInfo 处理结果对象
     */
    ResultInfo queryTradeRecordByCondition(TradeRecordCondition condition);

    /**
     * 根据交易流水号删除交易回调记录
     * @param transId 交易流水号
     */
    void delTradeCallbackByTransId(String transId);
}
