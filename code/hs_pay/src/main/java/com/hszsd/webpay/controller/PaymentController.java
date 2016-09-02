package com.hszsd.webpay.controller;

import com.github.pagehelper.PageInfo;
import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.date.DateUtil;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.PasswordCheckService;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.util.UUIDUtils;
import com.hszsd.webpay.validator.TransValidator;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.SelectTradesForm;
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
import org.springframework.web.bind.annotation.RequestMethod;
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

    /**
     * 从配置文件中，根据sourceCode读取商户信息
     */
    private static MerchantUtil merchantUtil = MerchantUtil.getInstance();


    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PasswordCheckService passwordCheckService;

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
    @RequestMapping(value = "/balancePay", method = RequestMethod.POST)
    public ModelAndView balancePay(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        logger.info("execute /pay/balancePay , params is : {} ",params.toString());

        Map<String, String> map = new HashMap<String, String>();
        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String sourceCode = params.get("sourceCode");
        String operateType = params.get("operateType");
        String orderType = params.get("orderType");
        String returnUrl = params.get("returnUrl");
        String noticeUrl = params.get("noticeUrl");
        String requestSign = params.get(MD5Sign);
        if(StringUtils.isEmpty(returnUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            return new ModelAndView("/common/500", "map", map);
        }

        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||
                StringUtils.isEmpty(money)||StringUtils.isEmpty(operateType)||StringUtils.isEmpty(orderType)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(noticeUrl)||StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }

        //签名验证
        String MD5_KEY = merchantUtil.getMerchantKey(sourceCode);
        if(StringUtils.isEmpty(MD5_KEY)){
            map.put("resCode", ResultConstants.SOURCECODE_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.SOURCECODE_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }

        boolean checkMD5Sign = new Hsmd5Util().checkMD5Sign(params, balancePayReqOrder, MD5_KEY, requestSign);
        if(!checkMD5Sign){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return  new ModelAndView("/common/500", "map", map);
        }

        //获取用户信息
        Result userResult = userService.getUserInfo(userId);
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("balancePay failed and UserResult={}", userResult);
            //向请求方返回错误提示
            map.put("resCode", ResultConstants.SESSION_TIME_OUT.getCode());
            map.put("resMsg",  ResultConstants.SESSION_TIME_OUT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();

        //创建业务数据记录
        String transId = UUIDUtils.getId(6,6);
        params.put("transId", transId);
        ResultInfo resultInfo = paymentService.createTradeRecord(params);
        ResultConstants createResult = (ResultConstants)resultInfo.getResult();
        if(!resultInfo.getIsSuccess()){
            map.put("resCode", createResult.getCode());
            map.put("resMsg",  createResult.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }

        map.put("source",merchantUtil.getMerchantName(sourceCode));
        map.put("useMoney",String.valueOf(MathUtil.round(userInfoDTO.getUseMoney(), 2)));
        map.put("username",userInfoDTO.getUsername());
        map.put("tradeTime", DateUtil.dateToStringShort(new Date()));
        map.put("transId", transId);
        map.put("money", money);
        map.put("orderId", orderId);
        map.put("submitUrl", "/pay/balancePayPwd");
        return  new ModelAndView("/payment/pay", "map", map);
    }

    /**
     * 减余额（余额支付） 获取支付密码
     * @param request
     * @param response
     * @param transForm
     * @param result
     */
    @RequestMapping(value = "/balancePayPwd", method = RequestMethod.POST)
    public void balancePayPwd(HttpServletRequest request, HttpServletResponse response, @Validated TransForm transForm, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();
        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("resCode", validatorMsg.getCode());
            map.put("resMsg", validatorMsg.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //验证支付密码
        ResultInfo queryInfo = paymentService.queryTradeRecord(transForm.getTransId());
        ResultConstants queryResult = (ResultConstants) queryInfo.getResult();
        if(!queryInfo.getIsSuccess()){
            map.put("resCode", queryResult.getCode());
            map.put("resMsg", queryResult.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        TradeRecordDTO tradeRecordDTO  = (TradeRecordDTO) queryInfo.getParams();
        String userId = tradeRecordDTO.getUserId();
        ResultInfo checkPwd = passwordCheckService.checkPayPassWord(userId, transForm.getPayPassword());
        if(!checkPwd.getIsSuccess()){
            map.put("resCode", ResultConstants.PAYPASSWORD_ISWRONG.getCode());
            map.put("resMsg", checkPwd.getResult());
            JsonUtil.writeJson(map, response);
            return;
        }

        //修改交易状态
        int operateType = 0;
        if(!StringUtils.isEmpty(transForm.getOperateType())){
            operateType = Integer.parseInt(transForm.getOperateType());
        }
        ResultInfo resultInfo = paymentService.editTradeRecord(transForm.getTransId(),operateType);
        ResultConstants result2 = (ResultConstants)resultInfo.getResult();
        map.put("resCode", result2.getCode());
        map.put("resMsg",  result2.getMsg());
        map.put("returnUrl",tradeRecordDTO.getReturnUrl());
        map.put("transId",tradeRecordDTO.getTransId());
        map.put("orderId",tradeRecordDTO.getOrderId());
        map.put("money",tradeRecordDTO.getMoney());
        if(resultInfo.getIsSuccess()){
            map.put("tradeStatus",GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
        }else{
            map.put("tradeStatus",GlobalConstants.TRADE_RECORD.TRADE_STATUS_3);
        }
        String md5Sign = new Hsmd5Util().generateMD5Sign(map, balancePayResOrder, merchantUtil.getMerchantKey(tradeRecordDTO.getSourceCode()));
        map.put(MD5Sign,md5Sign);
        logger.info("/pay/balancePay returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);
        return;
    }


    /**
     * 减积分接口（积分支付）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/creditPay", method = RequestMethod.POST)
    public void creditPay(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        logger.info("execute /pay/creditPay , params is : {} ",params.toString());
        Map<String,String> map = new HashMap<String,String>();
        //应答签名
        String responseSign = "";

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String credit = params.get("credit");
        String sourceCode = params.get("sourceCode");
        String operateType = params.get("operateType");
        String orderType = params.get("orderType");
        String returnUrl = params.get("returnUrl");
        String noticeUrl = params.get("noticeUrl");
        String requestSign = params.get(MD5Sign);
        if(StringUtils.isEmpty(returnUrl)){
            return;
        }
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(credit)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(operateType)||StringUtils.isEmpty(orderType)||
                StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)||StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }

        //签名验证
        String MD5_KEY = merchantUtil.getMerchantKey(sourceCode);
        if(StringUtils.isEmpty(MD5_KEY)){
            map.put("resCode", ResultConstants.SOURCECODE_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.SOURCECODE_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }
        boolean checkFlag = new Hsmd5Util().checkMD5Sign(params, creditPayReqOrder, MD5_KEY, requestSign);
        if(!checkFlag){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }

        //获取用户信息
        Result userResult = userService.getUserInfo(userId);
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("creditPay failed and UserResult={}", userResult);
            //向请求方返回错误提示
            map.put("resCode", ResultConstants.SESSION_TIME_OUT.getCode());
            map.put("resMsg",  ResultConstants.SESSION_TIME_OUT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return;
        }

        //创建业务数据记录
        String transId = UUIDUtils.getId(6,6);
        params.put("transId", transId);
        ResultInfo createInfo = paymentService.createTradeRecord(params);
        ResultConstants createResult = (ResultConstants)createInfo.getResult();
        if(!createInfo.getIsSuccess()){
            map.put("resCode", createResult.getCode());
            map.put("resMsg", createResult.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return;
        }
        //调用dubbox接口，修改交易状态
        ResultInfo editInfo = paymentService.editTradeRecord(transId,0);
        ResultConstants editResult = (ResultConstants)editInfo.getResult();
        map.put("resCode", editResult.getCode());
        map.put("resMsg",  editResult.getMsg());
        if(editInfo.getIsSuccess()){
            map.put("transId", transId);
            map.put("orderId", orderId);
            map.put("credit", credit);
            map.put("tradeStatus",String.valueOf(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2));
            responseSign = new Hsmd5Util().generateMD5Sign(map,creditPayResOrder,MD5_KEY);
        }
        logger.info("/pay/creditPay returned params is : {} ",map.toString());
        HttpUtils.sendPostRequest(returnUrl,map,responseSign);
        return;

    }




    /**
     * 减余额减积分接口（组合支付）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/assemblePay", method = RequestMethod.POST)
    public ModelAndView assemblePay(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        logger.info("execute /pay/assemblePay , params is {} ",params.toString());
        Map<String, String> map = new HashMap<String, String>();
        //应答签名
        String responseSign = "";

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String credit = params.get("credit");
        String sourceCode = params.get("sourceCode");
        String operateType = params.get("operateType");
        String orderType = params.get("orderType");
        String noticeUrl = params.get("noticeUrl");
        String returnUrl = params.get("returnUrl");
        String requestSign = params.get(MD5Sign);
        if(StringUtils.isEmpty(returnUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            return new ModelAndView("/common/500", "map", map);
        }
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(money)||
                StringUtils.isEmpty(credit)||StringUtils.isEmpty(operateType)||StringUtils.isEmpty(orderType)||StringUtils.isEmpty(sourceCode)||
                StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)||StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return new ModelAndView("/common/500", "map", map);
        }

        //签名验证
        String MD5_KEY = merchantUtil.getMerchantKey(sourceCode);
        if(StringUtils.isEmpty(MD5_KEY)){
            map.put("resCode", ResultConstants.SOURCECODE_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.SOURCECODE_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return new ModelAndView("/common/500", "map", map);
        }
        boolean checkMD5Sign = new Hsmd5Util().checkMD5Sign(params, assemblePayReqOrder, MD5_KEY, requestSign);
        if(!checkMD5Sign){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return new ModelAndView("/common/500", "map", map);
        }

        //获取用户信息
        Result userResult = userService.getUserInfo(userId);
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("assemblePay failed and UserResult={}", userResult);
            //向请求方返回错误提示dan
            map.put("resCode", ResultConstants.SESSION_TIME_OUT.getCode());
            map.put("resMsg",  ResultConstants.SESSION_TIME_OUT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();

        //创建业务数据记录
        String transId = UUIDUtils.getId(6,6);
        params.put("transId", transId);
        ResultInfo createInfo = paymentService.createTradeRecord(params);
        ResultConstants createResult = (ResultConstants)createInfo.getResult();
        if(!createInfo.getIsSuccess()){
            map.put("resCode", createResult.getCode());
            map.put("resMsg", createResult.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,responseSign);
            return new ModelAndView("/common/500", "map", map);
        }
        map.put("source",merchantUtil.getMerchantName(sourceCode));
        map.put("useMoney",String.valueOf(MathUtil.round(userInfoDTO.getUseMoney(), 2)));
        map.put("username",userInfoDTO.getUsername());
        map.put("tradeTime", DateUtil.dateToStringShort(new Date()));
        map.put("transId", transId);
        map.put("money", money);
        map.put("orderId", orderId);
        map.put("submitUrl", "/pay/assemblePayPwd");
        return  new ModelAndView("/payment/pay", "map", map);
    }


    /**
     * 减余额减积分（组合支付） 获取支付密码
     * @param request
     * @param response
     * @param transForm
     * @param result
     * @return
     */
    @RequestMapping(value = "/assemblePayPwd", method = RequestMethod.POST)
    public void assemblePayPwd(HttpServletRequest request, HttpServletResponse response, @Validated TransForm transForm, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();
        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("resCode", validatorMsg.getCode());
            map.put("resMsg", validatorMsg.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //验证支付密码
        ResultInfo queryInfo = paymentService.queryTradeRecord(transForm.getTransId());
        ResultConstants queryResult = (ResultConstants) queryInfo.getResult();
        if(!queryInfo.getIsSuccess()){
            map.put("resCode", queryResult.getCode());
            map.put("resMsg", queryResult.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        TradeRecordDTO tradeRecordDTO  = (TradeRecordDTO) queryInfo.getParams();
        String userId = tradeRecordDTO.getUserId();
        ResultInfo checkPwd = passwordCheckService.checkPayPassWord(userId, transForm.getPayPassword());
        if(!checkPwd.getIsSuccess()){
            map.put("resCode", ResultConstants.PAYPASSWORD_ISWRONG.getCode());
            map.put("resMsg", checkPwd.getResult());
            JsonUtil.writeJson(map, response);
            return;
        }

        //修改交易状态
        int operateType = 0;
        if(!StringUtils.isEmpty(transForm.getOperateType())){
            operateType = Integer.parseInt(transForm.getOperateType());
        }
        ResultInfo resultInfo = paymentService.editTradeRecord(transForm.getTransId(),operateType);
        ResultConstants result2 = (ResultConstants)resultInfo.getResult();
        map.put("resCode", result2.getCode());
        map.put("resMsg",  result2.getMsg());
        map.put("returnUrl",tradeRecordDTO.getReturnUrl());
        map.put("transId",tradeRecordDTO.getTransId());
        map.put("orderId",tradeRecordDTO.getOrderId());
        map.put("money",tradeRecordDTO.getMoney());
        map.put("credit",tradeRecordDTO.getCredit());
        if(resultInfo.getIsSuccess()){
            map.put("tradeStatus",GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
        }else{
            map.put("tradeStatus",GlobalConstants.TRADE_RECORD.TRADE_STATUS_3);
        }
        String md5Sign = new Hsmd5Util().generateMD5Sign(map, assemblePayResOrder, merchantUtil.getMerchantKey(tradeRecordDTO.getSourceCode()));
        map.put(MD5Sign,md5Sign);

        logger.info("/pay/assemblePay returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);
        return;
    }



    /**
     * 查询支付信息接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryPayment" , method = RequestMethod.POST)
    public void queryPayment(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = new HashMap<String, Object>();
        String transId = request.getParameter("transId");
        logger.info("execute /pay/queryPayment , transId is {} ",transId);
        if(StringUtils.isEmpty(transId)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        //查询交易信息
        ResultInfo resultInfo = paymentService.queryTradeRecord(transId);
        if(!resultInfo.getIsSuccess()){
            ResultConstants errorMsg = (ResultConstants) resultInfo.getResult();
            map.put("resCode",errorMsg.getCode());
            map.put("resMsg",errorMsg.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        TradeRecordDTO tradeRecordDTO = (TradeRecordDTO) resultInfo.getParams();
        map.put("transId",transId);
        map.put("orderId",tradeRecordDTO.getOrderId());
        map.put("money",tradeRecordDTO.getMoney());
        map.put("credit",tradeRecordDTO.getCredit());
        map.put("tradeStatus",tradeRecordDTO.getTradeStatus());
        map.put("resCode",ResultConstants.OPERATOR_SUCCESS.getCode());
        map.put("resMsg",ResultConstants.OPERATOR_SUCCESS.getMsg());
        logger.info("/pay/queryPayment returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);
    }


    /**
     * 查询交易信息接口(分页查询)
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/queryTradeRecords" , method = RequestMethod.POST)
    public void queryTradeRecords(HttpServletRequest request, HttpServletResponse response, @Validated SelectTradesForm selectTradesForm, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();
        logger.info("execute /pay/queryTradeRecords , params is {} ",selectTradesForm.toString());

        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("errorCode", validatorMsg.getCode());
            map.put("errorMsg", validatorMsg.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        //交易信息(分页查询)
        ResultInfo resultInfo = paymentService.queryTradeRecords(selectTradesForm);
        if(!resultInfo.getIsSuccess()){
            map.put("errorCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("errorMsg",ResultConstants.OPERATOR_FAIL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        PageInfo pageInfo = (PageInfo) resultInfo.getParams();
        logger.info("/pay/queryTradeRecords returned params is : {} ",pageInfo.toString());
        JsonUtil.writeJson(pageInfo, response);
    }


}
