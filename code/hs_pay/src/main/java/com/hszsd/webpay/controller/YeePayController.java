package com.hszsd.webpay.controller;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.service.YeePayService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.util.UUIDUtils;
import com.hszsd.webpay.validator.YeePayValidator;
import com.hszsd.webpay.web.dto.QuickBankOutDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.BindCardConfirmForm;
import com.hszsd.webpay.web.form.BindCardForm;
import com.hszsd.webpay.web.form.BindCardPayForm;
import com.jiangshikun.parrot.dao.RedisBaseDao;
import com.jiangshikun.parrot.dao.StringDao;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 易宝快捷支付控制器
 * Created by suocy on 2016/7/16.
 */

@Controller
@RequestMapping("/yeePay")
public class YeePayController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);

    private static final String yeePayReqOrder = "YEEPAY_REQ";
    private static final String yeePayResOrder = "YEEPAY_RES";
    /**
     * 绑卡回调地址
     */
    private static final String bindCardCallBackUrl = "/yeePay/toYeePay.do";
    /**
     * 快捷卡支付回调地址
     */
    public static final String yeePayCallBackUrl = "/yeePay/cardPayCallBack.html";
    /**
     * 从配置文件中，根据sourceCode读取商户信息
     */
    private static MerchantUtil merchantUtil = MerchantUtil.getInstance();


    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private YeePayService yeePayService;


    private StringDao stringDao = RedisBaseDao.getStringDao();

    /**
     * 为controller指定校验器
     * @param binder
     */
    @InitBinder
    public void initBinder(DataBinder binder){
        binder.setValidator(new YeePayValidator());
    }


    /**
     * 易宝支付
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/yeePay",method = RequestMethod.POST)
    public ModelAndView yeePay(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        logger.info("execute /yeePay/yeePay , params is : {} ",params.toString());

        Map<String, String> map = new HashMap<String, String>();
        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String proName = params.get("proName");
        String sourceCode = params.get("sourceCode");
        String operateType = params.get("operateType");
        String orderType = params.get("orderType");
        String returnUrl = params.get("returnUrl");
        String noticeUrl = params.get("noticeUrl");
        String requestSign = params.get("MD5Sign");
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(returnUrl)||
                StringUtils.isEmpty(money)|| StringUtils.isEmpty(proName)||StringUtils.isEmpty(operateType)||StringUtils.isEmpty(orderType)
                ||StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(noticeUrl)||StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            if (!StringUtils.isEmpty(returnUrl)) {
                //向请求方返回错误提示
                HttpUtils.sendPostRequest(returnUrl,map,"");
            }
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
        if(!new Hsmd5Util().checkMD5Sign(params, yeePayReqOrder, MD5_KEY, requestSign)){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }

        //获取用户信息
        Result userResult = userService.getUserInfo(userId);
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("yeePay failed and UserResult={}", userResult);
            //向请求方返回错误提示
            map.put("resCode", ResultConstants.SESSION_TIME_OUT.getCode());
            map.put("resMsg",  ResultConstants.SESSION_TIME_OUT.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
        if(GlobalConstants.COMMON.REAL_STATUS_1 != userInfoDTO.getRealStatus()){
            map.put("resCode", ResultConstants.REALSTATUS_ERROR.getCode());
            map.put("resMsg",  ResultConstants.REALSTATUS_ERROR.getMsg());
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }


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
        //查询用户拥有的快捷卡
        ResultInfo queryResult = yeePayService.queryQuickBank(userId);
        if(!queryResult.getIsSuccess()){
            map.put("resCode", createResult.getCode());
            map.put("resMsg", "用户快捷卡信息获取失败");
            HttpUtils.sendPostRequest(returnUrl,map,"");
            return new ModelAndView("/common/500", "map", map);
        }
        List<QuickBankOutDTO> quickBanks = (List<QuickBankOutDTO>)queryResult.getParams();

        Map<String, Object> mapView = new HashMap<String, Object>();
        mapView.put("proName",proName);//订单信息
        mapView.put("orderId", orderId);//订单编号
        mapView.put("money", money);//支付金额
        mapView.put("useMoney",String.valueOf(MathUtil.round(userInfoDTO.getUseMoney(), 2)));//可用余额
        mapView.put("username",userInfoDTO.getUsername());//支付账号
        mapView.put("transId", transId);// 交易流水号
        mapView.put("userId", userId);//用户编号
        mapView.put("quickBanks", quickBanks);//快捷卡信息
        mapView.put("callBackUrl", request.getAttribute("basePath") + bindCardCallBackUrl);//绑卡回调地址
        logger.info("/yeePay/yeePay to View ：wxpay.html, params is : {} ",mapView.toString());
        return  new ModelAndView("/mobile/wxpay", "map", mapView);
    }

    /**
     * 跳转到绑卡页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toBindCard")
    public ModelAndView toBindCard(HttpServletRequest request, HttpServletResponse response){
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        String callBackUrl = params.get("callBackUrl");
        String userId = params.get("userId");
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isEmpty(userId)||StringUtils.isEmpty(callBackUrl)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            JsonUtil.writeJson(map, response);
            return new ModelAndView("/common/500", "map", map);
        }
        stringDao.save(GlobalConstants.COMMON.REDIS_CALLBACKURL_PREFIX + userId,callBackUrl,60*60);
        return new ModelAndView("/mobile/bindCard", "map", params);
    }

    /**
     * 绑快捷卡
     * @param request
     * @param response
     * @param bindCard
     * @param result
     */
    @RequestMapping(value = "/bindCard")
    public void bindCard(HttpServletRequest request, HttpServletResponse response, @Validated BindCardForm bindCard, BindingResult result){
        logger.info("execute /yeePay/bindCard , params is : {} ",bindCard.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("resCode", validatorMsg.getCode());
            map.put("resMsg", validatorMsg.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        //获取用户信息
        Result userResult = userService.getUserInfo(bindCard.getUserId());
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("yeePay failed and UserResult={}", userResult);
            //向请求方返回错误提示
            map.put("resCode", ResultConstants.SESSION_TIME_OUT.getCode());
            map.put("resMsg",  ResultConstants.SESSION_TIME_OUT.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
        String userIp = HttpUtils.getRemoteHost(request);
        bindCard.setUserIp(userIp);
        //绑卡请求
        ResultInfo resultInfo = yeePayService.bindCard(bindCard, userInfoDTO);
        ResultConstants bindCardResult = (ResultConstants)resultInfo.getResult();
        map.put("resCode", bindCardResult.getCode());
        map.put("resMsg",  bindCardResult.getMsg());
        if (resultInfo.getIsSuccess()){
            map.put("requestId", resultInfo.getParams());
        }
        logger.info("/yeePay/bindCard returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);
    }



    /**
     * 跳转到确认绑卡页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toBindCardConfirm")
    public ModelAndView toBindBankCard(HttpServletRequest request, HttpServletResponse response){
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        return new ModelAndView("/mobile/bindCardConfirm", "map", params);
    }

    /**
     * 确认绑快捷卡
     * @param request
     * @param response
     * @param confirmForm
     * @param result
     */
    @RequestMapping(value = "/bindCardConfirm")
    public void bindCardConfirm(HttpServletRequest request, HttpServletResponse response, @Validated BindCardConfirmForm confirmForm, BindingResult result){
        logger.info("execute /yeePay/bindCardConfirm , params is : {} ",confirmForm.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("resCode", validatorMsg.getCode());
            map.put("resMsg", validatorMsg.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        //获取用户信息
        Result userResult = userService.getUserInfo(confirmForm.getUserId());
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("bindCardConfirm failed and UserResult={}", userResult);
            //向请求方返回错误提示
            map.put("resCode", ResultConstants.SESSION_TIME_OUT.getCode());
            map.put("resMsg",  ResultConstants.SESSION_TIME_OUT.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
        String userIp = HttpUtils.getRemoteHost(request);
        confirmForm.setUserIp(userIp);
        //绑卡确认
        ResultInfo resultInfo = yeePayService.bindCardConfirm(confirmForm,userInfoDTO);
        ResultConstants bindCardResult = (ResultConstants)resultInfo.getResult();
        map.put("resCode", bindCardResult.getCode());
        map.put("resMsg",  bindCardResult.getMsg());
        if (resultInfo.getIsSuccess()){
            String callBackUrl = stringDao.getValue(GlobalConstants.COMMON.REDIS_CALLBACKURL_PREFIX + userInfoDTO.getUserId());
            map.put("callBackUrl",  callBackUrl);
        }
        logger.info("/yeePay/bindCardConfirm returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);
    }


    /**
     * 跳回支付界面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/toYeePay")
    public ModelAndView toYeePay(HttpServletRequest request, HttpServletResponse response){
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        String userId = params.get("userId");
        String transId = params.get("transId");
        Map<String, Object> map = new HashMap<String, Object>();
        if(StringUtils.isEmpty(transId)||StringUtils.isEmpty(userId)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            JsonUtil.writeJson(map, response);
            return null;
        }
        //获取用户信息
        Result userResult = userService.getUserInfo(userId);
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
        ResultInfo resultInfo = paymentService.queryTradeRecord(transId);
        TradeRecordDTO tradeRecordDTO = (TradeRecordDTO)resultInfo.getParams();

        //查询用户拥有的快捷卡
        ResultInfo queryResult = yeePayService.queryQuickBank(userId);
        if(!queryResult.getIsSuccess()){
            return new ModelAndView("/common/500");
        }
        List<QuickBankOutDTO> quickBanks = (List<QuickBankOutDTO>)queryResult.getParams();
        Map<String, Object> mapView = new HashMap<String, Object>();
        mapView.put("proName",tradeRecordDTO.getProductName());//订单信息
        mapView.put("orderId", tradeRecordDTO.getOrderId());//订单编号
        mapView.put("money", tradeRecordDTO.getMoney());//支付金额
        mapView.put("useMoney",String.valueOf(MathUtil.round(userInfoDTO.getUseMoney(), 2)));//可用余额
        mapView.put("username",userInfoDTO.getUsername());//支付账号
        mapView.put("transId", transId);// 交易流水号
        mapView.put("userId", userId);//用户编号
        mapView.put("quickBanks", quickBanks);//快捷卡信息
        return  new ModelAndView("/mobile/wxpay", "map", mapView);

    }

    /**
     * 快捷卡支付
     * @param request
     * @param response
     * @param payForm
     * @param result
     */
    @RequestMapping(value = "/bindCardPay")
    public void bindCardPay(HttpServletRequest request, HttpServletResponse response, @Validated BindCardPayForm payForm, BindingResult result){
        logger.info("execute /yeePay/bindCardPay , params is : {} ",payForm.toString());
        Map<String, Object> map = new HashMap<String, Object>();
        //数据校验
        if(result.hasErrors()){
            ResultConstants validatorMsg= (ResultConstants) result.getFieldError().getArguments()[0];
            map.put("resCode", validatorMsg.getCode());
            map.put("resMsg", validatorMsg.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        String userIp = HttpUtils.getRemoteHost(request);
        payForm.setUserIp(userIp);
        String backUrl = request.getAttribute("basePath") + yeePayCallBackUrl;
        String sourceCode = "";
        try{
            //请求支付
            ResultInfo resultInfo = yeePayService.quickCardPay(payForm, backUrl);
            ResultConstants bindCardResult = (ResultConstants)resultInfo.getResult();
            if (!resultInfo.getIsSuccess()){
                map.put("resCode", bindCardResult.getCode());
                map.put("resMsg",  bindCardResult.getMsg());
                JsonUtil.writeJson(map, response);
                return;
            }
            for(int i = 1; i <= 3; i++){
                Thread.currentThread().sleep(1000 * 3);
                ResultInfo queryResult = paymentService.queryTradeRecord(payForm.getTransId());
                TradeRecordDTO tradeRecord  = (TradeRecordDTO) queryResult.getParams();
                map.put("returnUrl", tradeRecord.getReturnUrl());
                map.put("transId", tradeRecord.getTransId());
                map.put("orderId", tradeRecord.getOrderId());
                map.put("money", tradeRecord.getMoney());
                sourceCode = tradeRecord.getSourceCode();

                //判断当前状态
                if(tradeRecord.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_2){
                    map.put("resCode", ResultConstants.OPERATOR_SUCCESS.getCode());
                    map.put("resMsg",  ResultConstants.OPERATOR_SUCCESS.getMsg());
                    map.put("tradeStatus", tradeRecord.getTradeStatus());
                    break;
                }
                if(tradeRecord.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_1  && i == 3){
                    map.put("resCode", ResultConstants.WAIT_OPERATOR.getCode());
                    map.put("resMsg",  ResultConstants.WAIT_OPERATOR.getMsg());
                    map.put("tradeStatus", tradeRecord.getTradeStatus());
                    break;
                }
                if(tradeRecord.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_3 ){
                    map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
                    map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
                    map.put("tradeStatus", tradeRecord.getTradeStatus());
                    break;
                }
            }
            String md5Sign = new Hsmd5Util().generateMD5Sign(map, yeePayResOrder, merchantUtil.getMerchantKey(sourceCode));
            map.put("MD5Sign",md5Sign);
            logger.info("/yeePay/bindCardPay back params : {} ",map.toString());
            JsonUtil.writeJson(map, response);
        }catch (Exception e){
            e.printStackTrace();
            logger.error("bindCardPay-->>支付-->>Exception " + e.getMessage());
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg",  ResultConstants.OPERATOR_FAIL.getMsg());
            JsonUtil.writeJson(map, response);
        }
    }

    /**
     * 快捷卡支付回调
     * @param request
     * @param response
     */
    @RequestMapping(value = "/cardPayCallBack")
    public void cardPayCallBack(HttpServletRequest request, HttpServletResponse response){
        logger.info("YeePayController-->>cardPayCallBack.do-->>开始");
        String data	= request.getParameter("data").toString();
        String encryptKey = request.getParameter("encryptkey").toString();
        boolean callBackFlag = yeePayService.callBack(data, encryptKey);
        if (callBackFlag){
            try {
                response.getWriter().write("SUCCESS");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info("YeePayController-->>cardPayCallBack.do-->>结束，result = {}",callBackFlag);
    }



}
