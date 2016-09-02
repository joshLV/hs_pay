package com.hszsd.webpay.service;

import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.SelectTradesForm;

import java.util.Map;

/**
 * 交易记录业务层接口
 * Created by suocy on 2016/7/15.
 */
public interface PaymentService {

    /**
     * 创建交易记录
     * @return map
     */
    ResultInfo createTradeRecord(Map<String,String> map);

    /**
     * 订单支付业务处理
     * @param transId 交易序号
     * @param operateType 操作类型
     * @return
     */
    ResultInfo editTradeRecord(String transId, int operateType);

    /**
     * 正宗商城订单支付业务处理
     * 1.订单去重
     * 2.功能服务器接口进行金额或积分增减操作
     * 3.根据操作结果进行数据处理和回调外部系统
     * @param transId 交易序号
     * @return
     */
    ResultInfo editTradeRecordZZSC(String transId);

    /**
     * 中天物业订单缴费业务处理--余额支付
     * 1.订单去重
     * 2.功能服务器接口进行金额操作
     * 3.根据操作结果进行数据处理和回调外部系统
     * @param transId 交易序号
     * @param operateType 操作类型
     * @return
     */
    ResultInfo editTradeRecordZTWY(String transId, int operateType);

    /**
     * 微商城订单支付业务处理--余额支付
     * 1.订单去重
     * 2.功能服务器接口进行金额操作
     * 3.根据操作结果进行数据处理和回调外部系统
     * @param transId 交易序号
     * @param operateType 操作类型
     * @return
     */
    ResultInfo editTradeRecordZTSC(String transId, int operateType);


    /**
     * 通过交易流水号，查询交易记录
     * @param transId
     * @return
     */
    ResultInfo queryTradeRecord(String transId);

    /**
     * 修改交易记录状态，并保存交易异步通知信息
     * @param transId
     * @param tradeStatus
     * @param operateType
     * @return
     */
    boolean editTradeStatusAndCallBack(String transId, int tradeStatus, int operateType);

    /**
     * 根据条件分页查询交易记录
     * @param selectTradesForm 查询条件对象
     * @return
     */
    ResultInfo queryTradeRecords(SelectTradesForm selectTradesForm);

    /**
     * 正宗商城功能服务器接口：操作金额与积分
     * @param tradeRecordDTO
     * @return
     */
    boolean optCoreAccountZZSC(TradeRecordDTO tradeRecordDTO);

    /**
     * 中天物业功能服务器接口：操作金额
     * @param tradeRecordDTO
     * @param operateType
     * @return
     */
    boolean optCoreAccountZTWY(TradeRecordDTO tradeRecordDTO, int operateType);

    /**
     * 微商城（中天商城）功能服务器接口：操作金额
     * @param tradeRecordDTO
     * @param operateType
     * @return
     */
    boolean optCoreAccountZTSC(TradeRecordDTO tradeRecordDTO, int operateType);

}
