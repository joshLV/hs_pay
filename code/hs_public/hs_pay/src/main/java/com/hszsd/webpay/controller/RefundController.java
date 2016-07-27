package com.hszsd.webpay.controller;

import com.hszsd.common.util.math.MathUtil;
import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.service.TransIdService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.web.form.TransForm;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 退余额、退积分控制器
 * Created by suocy on 2016/7/16.
 */
@Controller
@RequestMapping("/refund")
public class RefundController {

    private static final Logger logger = LoggerFactory.getLogger(RefundController.class);
    private static final String MD5Sign = "MD5Sign";
    private static final String MD5_KEY = "AF724289725726721FD2374";

    private static final String refundReqOrder = "REFUND_REQ";
    private static final String refundResOrder = "REFUND_RES";
    private static final String addCreditReqOrder = "ADDCREDIT_REQ";
    private static final String addCreditResOrder = "ADDCREDIT_RES";
    private static final String assembleRefundReqOrder = "ASSEMBLEREFUND_REQ";
    private static final String assembleRefundResOrder = "ASSEMBLEREFUND_RES";

//    @Autowired
//    private UserService userService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private TransIdService transIdService;

    /**
     * 加余额接口（余额退款）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"refund"})
    public void refund(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        Map<String, String> map = new HashMap<String, String>();
        //签名验证
        String responseSign = "";
        String requestSign = params.get(MD5Sign);
        String returnUrl = params.get("returnUrl");
        if(StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.MD5SIGN_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return;
        }
        boolean b = new Hsmd5Util().checkMD5Sign(params, refundReqOrder, MD5_KEY, requestSign);
        if(!b){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return;
        }

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String sourceCode = params.get("sourceCode");
        String noticeUrl = params.get("noticeUrl");
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(money)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return ;
        }

        //验证money格式
        if(!MathUtil.verifyFormat(money)){
            map.put("resCode", ResultConstants.AMOUNT_WRONGFORMAT.getCode());
            map.put("resMsg",  ResultConstants.AMOUNT_WRONGFORMAT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return;
        }

        //创建业务数据记录
        params.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.REFUND));
        String transId = transIdService.initTransNo();
        params.put("transId", transId);
        if(paymentService.createTradeRecord(params)){
            TransForm transForm = new TransForm();
            transForm.setTransId(transId);
            ResultInfo resultInfo = paymentService.editTradeRecord(transForm,GlobalConstants.TRADE_RECORD.REFUND);
            ResultConstants result2 = (ResultConstants)resultInfo.getResult();
            map.put("resCode", result2.getCode());
            map.put("resMsg",  result2.getMsg());
            if(resultInfo.getIsSuccess()){
                map.put("transId", transId);
                map.put("orderId", orderId);
                map.put("money", money);
                map.put("tradeStatus",String.valueOf(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2));
                responseSign = new Hsmd5Util().generateMD5Sign(map,refundResOrder,MD5_KEY);
            }
        }else{
            logger.error("refund occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
        }
        HttpUtils.sendPostRequest(returnUrl,map,responseSign);
    }

    /**
     * 增加积分接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"addCredit"})
    public void addCredit(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        Map<String, String> map = new HashMap<String, String>();
        //签名验证
        String responseSign = "";
        String requestSign = params.get(MD5Sign);
        String returnUrl = params.get("returnUrl");
        if(StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.MD5SIGN_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }
        boolean b = new Hsmd5Util().checkMD5Sign(params, addCreditReqOrder, MD5_KEY, requestSign);
        if(!b){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String credit = params.get("credit");
        String sourceCode = params.get("sourceCode");
        String noticeUrl = params.get("noticeUrl");
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(credit)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return ;
        }

        //创建业务数据记录
        params.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.ADD_CREDIT));
        String transId = transIdService.initTransNo();
        params.put("transId", transId);
        if(paymentService.createTradeRecord(params)){
            TransForm transForm = new TransForm();
            transForm.setTransId(transId);
            ResultInfo resultInfo = paymentService.editTradeRecord(transForm,GlobalConstants.TRADE_RECORD.ADD_CREDIT);
            ResultConstants result2 = (ResultConstants)resultInfo.getResult();
            map.put("resCode", result2.getCode());
            map.put("resMsg",  result2.getMsg());
            if(resultInfo.getIsSuccess()){
                map.put("transId", transId);
                map.put("orderId", orderId);
                map.put("credit", credit);
                map.put("tradeStatus",String.valueOf(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2));
                responseSign = new Hsmd5Util().generateMD5Sign(map,addCreditResOrder,MD5_KEY);
            }
        }else{
            logger.error("addCredits occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
        }
        HttpUtils.sendPostRequest(returnUrl,map,responseSign);
    }


    /**
     * 加余额加积分接口（组合退款）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"assembleRefund"})
    public void assembleRefund(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        Map<String, String> map = new HashMap<String, String>();
        //签名验证
        String responseSign = "";
        String requestSign = params.get(MD5Sign);
        String returnUrl = params.get("returnUrl");
        if(StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.MD5SIGN_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }
        boolean b = new Hsmd5Util().checkMD5Sign(params, assembleRefundReqOrder, MD5_KEY, requestSign);
        if(!b){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String credit = params.get("credit");
        String sourceCode = params.get("sourceCode");
        String noticeUrl = params.get("noticeUrl");
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(credit)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(money)||StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return ;
        }

        //验证money格式
        if(!MathUtil.verifyFormat(money)){
            map.put("resCode", ResultConstants.AMOUNT_WRONGFORMAT.getCode());
            map.put("resMsg",  ResultConstants.AMOUNT_WRONGFORMAT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return;
        }


        //创建业务数据记录
        params.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.ASSEMBLE_REFUND));
        String transId = transIdService.initTransNo();
        params.put("transId", transId);
        if(paymentService.createTradeRecord(params)){
            TransForm transForm = new TransForm();
            transForm.setTransId(transId);
            ResultInfo resultInfo = paymentService.editTradeRecord(transForm,GlobalConstants.TRADE_RECORD.ASSEMBLE_REFUND);
            ResultConstants result2 = (ResultConstants)resultInfo.getResult();
            map.put("resCode", result2.getCode());
            map.put("resMsg",  result2.getMsg());
            if(resultInfo.getIsSuccess()){
                map.put("transId", transId);
                map.put("orderId", orderId);
                map.put("money", money);
                map.put("credit", credit);
                map.put("tradeStatus",String.valueOf(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2));
                responseSign = new Hsmd5Util().generateMD5Sign(map,assembleRefundResOrder,MD5_KEY);
            }
        }else{
            logger.error("assembleRefund occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
        }
        HttpUtils.sendPostRequest(returnUrl,map,responseSign);
    }



}
