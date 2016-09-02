package com.hszsd.webpay.service.impl;

import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.condition.TradeRecordCondition;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.service.CommonService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.util.ObjectUtils;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.StringDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 通用业务逻辑接口
 * Created by gzhengDu on 2016/8/16.
 */
@Service("commonService")
public class CommonServiceImpl implements CommonService {
    private final static Logger logger = LoggerFactory.getLogger(CommonService.class);

    @Autowired
    private TradeRecordDao tradeRecordDao;
    private StringDao stringDao = RedisBaseDao.getStringDao();

    //定义异步通知参数模板
    private String[] rechargeParam = {"transId", "money", "tradeStatus"};
    private String[] balancePayParam = {"transId", "orderId", "money", "tradeStatus"};
    private String[] refundParam = {"transId", "orderId", "money", "tradeStatus"};
    private String[] creditPayParam = {"transId", "orderId", "credit", "tradeStatus"};
    private String[] addCreditParam = {"transId", "orderId", "credit", "tradeStatus"};
    private String[] assemblePayParam = {"transId", "orderId", "money", "credit", "tradeStatus"};
    private String[] assembleRefundParam = {"transId", "orderId", "money", "credit", "tradeStatus"};
    private String[] quickPayParam = {"transId", "orderId", "money", "tradeStatus"};


    /**
     * 实时对商户进行异步通知
     * @param tradeRecordDTO 交易记录信息
     * @return String 商户返回的确认信息
     */
    public String asyncNoticeMerchant(TradeRecordDTO tradeRecordDTO){
        logger.info("asyncNoticeMerchant is starting and tradeRecordDTO={}", tradeRecordDTO);
        //封装返回参数信息
        Map<String, String> paramMap = Collections.EMPTY_MAP;
        String type = null;
        switch (tradeRecordDTO.getOperateType()){
            case GlobalConstants.TRADE_RECORD.RECHARGE_BAOFOO:
                paramMap = initNoticeData(tradeRecordDTO, rechargeParam);
                type = GlobalConstants.COMMON.RECHARGE_RES;
                break;
            case GlobalConstants.TRADE_RECORD.RECHARGE_HUICHAO:
                paramMap = initNoticeData(tradeRecordDTO, rechargeParam);
                type = GlobalConstants.COMMON.RECHARGE_RES;
                break;
            case GlobalConstants.TRADE_RECORD.RECHARGE_ONLINE:
                paramMap = initNoticeData(tradeRecordDTO, rechargeParam);
                type = GlobalConstants.COMMON.RECHARGE_RES;
                break;
            case GlobalConstants.TRADE_RECORD.RECHARGE_YEEPAY:
                paramMap = initNoticeData(tradeRecordDTO, rechargeParam);
                type = GlobalConstants.COMMON.RECHARGE_RES;
                break;
            case GlobalConstants.TRADE_RECORD.BALANCE_PAY:
                paramMap = initNoticeData(tradeRecordDTO, balancePayParam);
                type = GlobalConstants.COMMON.BALANCEPAY_RES;
                break;
            case GlobalConstants.TRADE_RECORD.REFUND:
                paramMap = initNoticeData(tradeRecordDTO, refundParam);
                type = GlobalConstants.COMMON.REFUND_RES;
                break;
            case GlobalConstants.TRADE_RECORD.CREDIT_PAY:
                paramMap = initNoticeData(tradeRecordDTO, creditPayParam);
                type = GlobalConstants.COMMON.CREDITPAY_RES;
                break;
            case GlobalConstants.TRADE_RECORD.ADD_CREDIT:
                paramMap = initNoticeData(tradeRecordDTO, addCreditParam);
                type = GlobalConstants.COMMON.ADDCREDIT_RES;
                break;
            case GlobalConstants.TRADE_RECORD.ASSEMBLE_PAY:
                paramMap = initNoticeData(tradeRecordDTO, assemblePayParam);
                type = GlobalConstants.COMMON.ASSEMBLEPAY_RES;
                break;
            case GlobalConstants.TRADE_RECORD.ASSEMBLE_REFUND:
                paramMap = initNoticeData(tradeRecordDTO, assembleRefundParam);
                type = GlobalConstants.COMMON.ASSEMBLEREFUND_RES;
                break;
            case GlobalConstants.TRADE_RECORD.QUICK_PAY:
                paramMap = initNoticeData(tradeRecordDTO, quickPayParam);
                type = GlobalConstants.COMMON.YEEPAY_RES;
                break;
            default:
                break;
        }

        //生成签名数据
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(paramMap, type, MerchantUtil.getInstance().getMerchantKey(tradeRecordDTO.getSourceCode()));
        return HttpUtils.sendPostRequest(tradeRecordDTO.getNoticeUrl(), paramMap, md5sign);
    }



    /**
     * 根据交易记录信息及所需参数列表初始化返回参数
     * @param tradeRecordDTO 交易记录信息
     * @param params 参数列表
     * @return Map 返回参数
     */
    public Map initNoticeData(TradeRecordDTO tradeRecordDTO, String[] params){
        //logger.info("initNoticeData is starting and tradeRecordDTO={}, params={}", tradeRecordDTO, params);
        Map<String, String> paramMap = new TreeMap<String, String>();
        Map<String, String> objMap = Collections.EMPTY_MAP;
        try {
            objMap = ObjectUtils.objectToMapValue(tradeRecordDTO);
        } catch (Exception e) {
            logger.error("initNoticeData occurs an error and cause by {}", e.getMessage());
            return paramMap;
        }
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
        //logger.info("initNoticeData success and paramMap={}", paramMap);
        return paramMap;
    }

    /**
     * 检查是否有重复订单
     * @param orderId
     * @return
     */
    @Override
    public boolean checkRepeatedOrder(String orderId) {
        //查询该订单历史记录中，是否有成功的记录
        TradeRecordCondition condition = new TradeRecordCondition();
        condition.or().andOrderIdEqualTo(orderId)
                .andTradeStatusEqualTo(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
        List<TradeRecordDTO> tradeRecordDTOs = tradeRecordDao.selectByCondition(condition);
        if(tradeRecordDTOs!=null&&tradeRecordDTOs.size()>0){
            logger.error("checkRepeatedOrder --> The order :{} is repeated... ",orderId);
            return false;
        }
        //查询redis中，该订单是否在处理中
        String value = stringDao.getValue(GlobalConstants.COMMON.REDIS_ORDERID_PREFIX + orderId);
        if(!StringUtils.isEmpty(value)){
            logger.error("checkRepeatedOrder --> The order :{} is dealing... ",orderId);
            return false;
        }
        return true;
    }

}
