package com.hszsd.webpay.service.impl;

import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.po.TradeRecordPO;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.TransForm;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.StringDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 交易记录业务层接口实现
 * Created by suocy on 2016/7/15.
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private TradeRecordDao tradeRecordDao;

    private StringDao stringDao = RedisBaseDao.getStringDao();


    @Override
    public boolean createTradeRecord(Map<String,String> map){
        TradeRecordPO tradeRecord = new TradeRecordPO();
        tradeRecord.setTransId(map.get("transId"));
        tradeRecord.setUserId(map.get("userId"));
        String money = map.get("money");
        if(!StringUtils.isEmpty(money)){
            tradeRecord.setMoney(new BigDecimal(money).setScale(2,BigDecimal.ROUND_HALF_UP));
        }
        String credit = map.get("credit");
        if(!StringUtils.isEmpty(money)){
            tradeRecord.setCredit(new BigDecimal(credit).setScale(0,BigDecimal.ROUND_HALF_UP));
        }
        tradeRecord.setMobile(map.get("mobile"));
        tradeRecord.setOrderId(map.get("orderId"));
        tradeRecord.setSourceCode(map.get("sourceCode"));
        tradeRecord.setOperateType(Integer.valueOf(map.get("operateType")));
        tradeRecord.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_1);
        tradeRecord.setCreateDate(new Date());
        tradeRecord.setReturnUrl(map.get("returnUrl"));
        tradeRecord.setNoticeUrl(map.get("noticeUrl"));
        tradeRecord.setRemark(map.get("remark"));

        if(tradeRecordDao.insert(tradeRecord)>0){
            return true;
        }
        return false;
    }

    @Override
    public ResultInfo editTradeRecord(TransForm transForm, int operateType) {
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey(transForm.getTransId());
        if(tradeRecordDTO==null){
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            logger.error("editTradeRecord occurs an error, Because there is not the transId:{}",transForm.getTransId());
            return resultInfo;
        }
        //验证支付密码
        if(operateType==GlobalConstants.TRADE_RECORD.BALANCE_PAY||operateType==GlobalConstants.TRADE_RECORD.ASSEMBLE_PAY){
            String errorMsg = this.getErrorMsg(tradeRecordDTO.getUserId());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.PAYPASSWORD_ISWRONG);
            resultInfo.setParams(errorMsg);
            return resultInfo;
        }

        //修改交易记录状态
        TradeRecordPO tradeRecordPO = new TradeRecordPO();
        tradeRecordPO.setTransId(transForm.getTransId());
        tradeRecordPO.setTradeStatus(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
        tradeRecordPO.setUpdateDate(new Date());
        try{
            tradeRecordDao.updateByPrimaryKeySelective(tradeRecordPO);
        }catch(Exception e){
            logger.error("editTradeRecord occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

        //调DUBBOX接口


        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        resultInfo.setParams(tradeRecordDTO);
        return resultInfo;
    }

    @Override
    public ResultInfo queryTradeRecord(String transId) {
        ResultInfo resultInfo = new ResultInfo();
        TradeRecordDTO tradeRecordDTO;
        try{
            tradeRecordDTO  = tradeRecordDao.selectByPrimaryKey(transId);
        }catch(Exception e){
            logger.error("queryTradeRecord occurs an error and cause by {}", e.getMessage());
            resultInfo.setIsSuccess(false);
            resultInfo.setResult(ResultConstants.OPERATOR_FAIL);
            return resultInfo;
        }

       // stringDao.save();

        resultInfo.setIsSuccess(true);
        resultInfo.setResult(ResultConstants.OPERATOR_SUCCESS);
        resultInfo.setParams(tradeRecordDTO);
        return resultInfo;
    }


    /**
     *
     * 当支付密码验证错误时，将错误次数存在redis中
     * 键：“payPwd”+ userId； 值：错误次数
     * 返回页面提示语
     *
      */
    private static final int time = 24*60*60;
    private String getErrorMsg(String userId){
        String value = stringDao.getValue("psyPwd" + userId);
        int count = 1;
        String errorMsg = "";
        if(StringUtils.isEmpty(value)){
            stringDao.save("psyPwd" + userId, String.valueOf(count), time);
            errorMsg = "支付密码输入错误！您今天还有" + String.valueOf(5-count)+"次输入机会";
        }else{
            if(Integer.parseInt(value)> 4){
                errorMsg = "由于支付密码输入错误5次，您的账户已被锁定，请24小时以后再试！";
            }else{
                count = Integer.parseInt(value)+ 1;
                stringDao.save("psyPwd" + userId, String.valueOf(count), time);
                errorMsg = "支付密码输入错误！您今天还有" + String.valueOf(5-count)+"次输入机会";
            }
        }
        return errorMsg;

    }
}
