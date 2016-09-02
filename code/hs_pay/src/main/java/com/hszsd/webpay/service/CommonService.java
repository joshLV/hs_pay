package com.hszsd.webpay.service;

import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.web.dto.TradeRecordDTO;

import java.util.Map;

/**
 * 通用业务逻辑接口
 * Created by gzhengDu on 2016/8/16.
 */
public interface CommonService {
    /**
     * 实时对商户进行异步通知
     * @param tradeRecordDTO 交易记录信息
     * @return String 商户返回的确认信息
     */
    String asyncNoticeMerchant(TradeRecordDTO tradeRecordDTO);

    /**
     * 根据交易记录信息及所需参数列表初始化返回参数
     * @param tradeRecordDTO 交易记录信息
     * @param params 参数列表
     * @return Map 返回参数
     */
    Map initNoticeData(TradeRecordDTO tradeRecordDTO, String[] params);

    /**
     * 检查是否有重复订单
     * @param orderId
     * @return
     */
    boolean checkRepeatedOrder(String orderId);

}
