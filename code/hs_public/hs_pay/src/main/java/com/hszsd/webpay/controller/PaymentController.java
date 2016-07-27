package com.hszsd.webpay.controller;

import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.service.TransIdService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.validator.TransValidator;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.TransForm;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 减余额、减积分控制器
 * Created by suocy on 2016/7/16.
 */
@Controller
@RequestMapping("/pay")
public class PaymentController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private static final String MD5Sign = "MD5Sign";

    private static final String balancePayReqOrder = "BALANCEPAY_REQ";
    private static final String balancePayResOrder = "BALANCEPAY_RES";

    private static final String creditPayReqOrder = "CREDITPAY_REQ";
    private static final String creditPayResOrder = "CREDITPAY_RES";

    private static final String assemblePayReqOrder = "ASSEMBLEPAY_REQ";
    private static final String assemblePayResOrder = "ASSEMBLEPAY_RES";

    private static final String MD5_KEY = "AF724289725726721FD2374";


   // private UserService userService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private TransIdService transIdService;

    /**
     * 为controller指定校验器
     * @param binder
     */
    @InitBinder
    public void initBinder(DataBinder binder){
        binder.setValidator(new TransValidator());
    }

    /**
     * 减余额接口（余额支付）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"balancePay"})
    public ModelAndView balancePay(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);

        Map<String, String> map = new HashMap<String, String>();

        //签名验证
        String requestSign = params.get(MD5Sign);
        if(StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.MD5SIGN_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISNULL.getMsg());
            HttpUtils.sendPostRequest(params.get("returnUrl"),map,"");
            return null;
        }
        boolean b = new Hsmd5Util().checkMD5Sign(params, balancePayReqOrder, MD5_KEY, requestSign);
        if(!b){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(params.get("returnUrl"),map,"");
            return null;
        }

//        String username = request.getRemoteUser();
//        if(StringUtils.isEmpty(username)){
//            map.put("operator", ResultConstants.SESSION_TIME_OUT);
//            JsonUtil.writeJson(map, response);
//            return null;
//        }
//        Result userResult = userService.getNameUser(username);
//        if(userResult == null || ResultCode.RES_OK.equals(userResult.getCode())){
//            map.put("operator", ResultConstants.SESSION_TIME_OUT);
//            JsonUtil.writeJson(map, response);
//            return null;
//        }

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String sourceCode = params.get("sourceCode");
        String returnUrl = params.get("returnUrl");
        String noticeUrl = params.get("noticeUrl");
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(money)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return null;
        }
        //验证money格式
        if(!MathUtil.verifyFormat(money)){
            map.put("resCode", ResultConstants.AMOUNT_WRONGFORMAT.getCode());
            map.put("resMsg",  ResultConstants.AMOUNT_WRONGFORMAT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return null;
        }

        //创建业务数据记录
        params.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.BALANCE_PAY));
        String transId = transIdService.initTransNo();
        params.put("transId", transId);
        if(!paymentService.createTradeRecord(params)){
            logger.error("balancePay occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
            HttpUtils.sendPostRequest(params.get("returnUrl"),map,"");
            return null;

        }
        map.put("source"," 贵州正宗商城");
        map.put("useMoney","8888.88");
        map.put("username","username");
        map.put("tradeTime", DateUtil.dateToStringShort(new Date()));
        map.put("transId", transId);
        map.put("money", money);
        map.put("orderId", orderId);
        return  new ModelAndView("/payment/pay", "map", params);
    }


    /**
     * 减余额（余额支付） 获取支付密码
     * @param request
     * @param response
     * @param transForm
     * @param result
     * @return
     */
    @RequestMapping({"balancePayPwd"})
    public void balancePayPwd(HttpServletRequest request, HttpServletResponse response, @Validated TransForm transForm, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();
        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("resCode", validatorMsg.getCode());
            map.put("resMsg", validatorMsg.getMsg());
        }else{
            ResultInfo resultInfo = paymentService.editTradeRecord(transForm,GlobalConstants.TRADE_RECORD.BALANCE_PAY);
            ResultConstants result2 = (ResultConstants)resultInfo.getResult();
            if (result2!=ResultConstants.PAYPASSWORD_ISWRONG){
                map.put("resCode", result2.getCode());
                map.put("resMsg",  result2.getMsg());
            }else{
                map.put("resCode", result2.getCode());
                map.put("resMsg",  resultInfo.getParams());
            }
            if(resultInfo.getIsSuccess()){
                TradeRecordDTO tradeRecordDTO = (TradeRecordDTO) resultInfo.getParams();
                map.put("returnUrl",tradeRecordDTO.getReturnUrl());
                map.put("transId",tradeRecordDTO.getTransId());
                map.put("orderId",tradeRecordDTO.getOrderId());
                map.put("money",tradeRecordDTO.getMoney());
                map.put("tradeStatus",tradeRecordDTO.getTradeStatus());
                String md5Sign = new Hsmd5Util().generateMD5Sign(map, balancePayResOrder, MD5_KEY);
                map.put(MD5Sign,md5Sign);
            }
        }
        JsonUtil.writeJson(map, response);
        return;
    }


    /**
     * 减积分接口（积分支付）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"creditPay"})
    public void creditPay(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        Map<String,String> map = new HashMap<String,String>();
        //应答签名
        String responseSign = "";

        //签名验证
        String requestSign = params.get(MD5Sign);

        if(StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.MD5SIGN_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISNULL.getMsg());
            HttpUtils.sendPostRequest(params.get("returnUrl"),map,responseSign);
            return;
        }
        boolean b = new Hsmd5Util().checkMD5Sign(params, creditPayReqOrder, MD5_KEY, requestSign);
        if(!b){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(params.get("returnUrl"),map,responseSign);
            return;
        }

//        String username = request.getRemoteUser();
//        if(StringUtils.isEmpty(username)){
//            map.put("operator", ResultConstants.SESSION_TIME_OUT);
//            JsonUtil.writeJson(map, response);
//            return ;
//        }
//        Result userResult = userService.getNameUser(username);
//        if(userResult == null || ResultCode.RES_OK.equals(userResult.getCode())){
//            map.put("operator", ResultConstants.SESSION_TIME_OUT);
//            JsonUtil.writeJson(map, response);
//            return ;
//        }
        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String credit = params.get("credit");
        String sourceCode = params.get("sourceCode");
        String returnUrl = params.get("returnUrl");
        String noticeUrl = params.get("noticeUrl");
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(credit)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }

        //创建业务数据记录
        params.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.CREDIT_PAY));
        String transId = transIdService.initTransNo();
        params.put("transId", transId);
        if(paymentService.createTradeRecord(params)){
            TransForm transForm = new TransForm();
            transForm.setTransId(transId);
            ResultInfo resultInfo = paymentService.editTradeRecord(transForm,GlobalConstants.TRADE_RECORD.CREDIT_PAY);
            ResultConstants result2 = (ResultConstants)resultInfo.getResult();
            map.put("resCode", result2.getCode());
            map.put("resMsg",  result2.getMsg());
            if(resultInfo.getIsSuccess()){
                map.put("transId", transId);
                map.put("orderId", orderId);
                map.put("credit", credit);
                map.put("tradeStatus",String.valueOf(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2));
                responseSign = new Hsmd5Util().generateMD5Sign(map,creditPayResOrder,MD5_KEY);
            }
        }else{
            logger.error("creditPay occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
        }
        HttpUtils.sendPostRequest(returnUrl,map,responseSign);

    }




    /**
     * 减余额减积分接口（组合支付）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"assemblePay"})
    public ModelAndView assemblePay(HttpServletRequest request, HttpServletResponse response){
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
            return null;
        }
        boolean b = new Hsmd5Util().checkMD5Sign(params, assemblePayReqOrder, MD5_KEY, requestSign);
        if(!b){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return null;
        }

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String credit = params.get("credit");
        String sourceCode = params.get("sourceCode");
        String noticeUrl = params.get("noticeUrl");
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(money)||StringUtils.isEmpty(credit)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return null;
        }

        //验证money格式
        if(!MathUtil.verifyFormat(money)){
            map.put("resCode", ResultConstants.AMOUNT_WRONGFORMAT.getCode());
            map.put("resMsg",  ResultConstants.AMOUNT_WRONGFORMAT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return null;
        }

        //创建业务数据记录
        params.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.ASSEMBLE_PAY));
        String transId = transIdService.initTransNo();
        params.put("transId", transId);
        if(!paymentService.createTradeRecord(params)){
            logger.error("assemblePay occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return null;
        }
        map.put("source"," 贵州正宗商城");
        map.put("useMoney","8888.88");
        map.put("username","username");
        map.put("tradeTime", DateUtil.dateToStringShort(new Date()));
        map.put("transId", transId);
        map.put("money", money);
        map.put("orderId", orderId);
        return  new ModelAndView("/payment/pay", "map", params);
    }


    /**
     * 减余额减积分（组合支付） 获取支付密码
     * @param request
     * @param response
     * @param transForm
     * @param result
     * @return
     */
    @RequestMapping({"assemblePayPwd"})
    public void assemblePayPwd(HttpServletRequest request, HttpServletResponse response, @Validated TransForm transForm, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();
        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("resCode", validatorMsg.getCode());
            map.put("resMsg", validatorMsg.getMsg());
        }else{
            ResultInfo resultInfo = paymentService.editTradeRecord(transForm,GlobalConstants.TRADE_RECORD.ASSEMBLE_PAY);
            ResultConstants result2 = (ResultConstants)resultInfo.getResult();
            map.put("resCode", result2.getCode());
            map.put("resMsg",  result2.getMsg());
            if(resultInfo.getIsSuccess()){
                TradeRecordDTO tradeRecordDTO = (TradeRecordDTO) resultInfo.getParams();
                map.put("returnUrl",tradeRecordDTO.getReturnUrl());
                map.put("transId",tradeRecordDTO.getTransId());
                map.put("orderId",tradeRecordDTO.getOrderId());
                map.put("money",tradeRecordDTO.getMoney());
                map.put("credit",tradeRecordDTO.getCredit());
                map.put("tradeStatus",tradeRecordDTO.getTradeStatus());
                String md5Sign = new Hsmd5Util().generateMD5Sign(map, assemblePayResOrder, MD5_KEY);
                map.put(MD5Sign,md5Sign);
            }
        }
        JsonUtil.writeJson(map, response);
        return;
    }



    /**
     * 查询支付信息接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"queryPayment"})
    public void queryPayment(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = new HashMap<String, Object>();
        String transId = (String) request.getAttribute("transId");
        if(StringUtils.isEmpty(transId)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        ResultInfo resultInfo = paymentService.queryTradeRecord(transId);

        map.put("transId",transId);
        if(resultInfo.getIsSuccess()){
            TradeRecordDTO tradeRecordDTO = (TradeRecordDTO) resultInfo.getParams();
            map.put("orderId",tradeRecordDTO.getOrderId());
            map.put("money",tradeRecordDTO.getMoney());
            map.put("credit",tradeRecordDTO.getCredit());
            map.put("tradeStatus",tradeRecordDTO.getTradeStatus());
            map.put("resCode",ResultConstants.OPERATOR_SUCCESS.getCode());
            map.put("resMsg",ResultConstants.OPERATOR_SUCCESS.getMsg());
        }else{
            ResultConstants errorMsg = (ResultConstants) resultInfo.getResult();
            map.put("resCode",errorMsg.getCode());
            map.put("resMsg",errorMsg.getMsg());
        }
        JsonUtil.writeJson(map, response);
    }






}
