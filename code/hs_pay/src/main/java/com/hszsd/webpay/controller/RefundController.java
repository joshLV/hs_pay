package com.hszsd.webpay.controller;

import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.PaymentService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.util.UUIDUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

    private static final String refundReqOrder = "REFUND_REQ";
    private static final String addCreditReqOrder = "ADDCREDIT_REQ";
    private static final String assembleRefundReqOrder = "ASSEMBLEREFUND_REQ";

    /**
     * 从配置文件中，根据sourceCode读取商户信息
     */
    private static MerchantUtil merchantUtil = MerchantUtil.getInstance();

    @Autowired
    private PaymentService paymentService;


    /**
     * 加余额接口（余额退款）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/refund", method = RequestMethod.POST)
    public void refund(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        logger.info("execute /refund/refund , params is : {} ",params.toString());
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
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(money)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(operateType)||StringUtils.isEmpty(orderType)||
                StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)||StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //签名验证
        String MD5_KEY = merchantUtil.getMerchantKey(sourceCode);
        if(StringUtils.isEmpty(MD5_KEY)){
            map.put("resCode", ResultConstants.SOURCECODE_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.SOURCECODE_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        boolean checkMD5Sign = new Hsmd5Util().checkMD5Sign(params, refundReqOrder, MD5_KEY, requestSign);
        if(!checkMD5Sign){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //创建业务数据记录
        String transId = UUIDUtils.getId(6,6);
        params.put("transId", transId);
        ResultInfo createInfo = paymentService.createTradeRecord(params);
        ResultConstants createResult = (ResultConstants)createInfo.getResult();
        if(!createInfo.getIsSuccess()){
            logger.error("refund occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", createResult.getCode());
            map.put("resMsg",  createResult.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        map.put("resCode", ResultConstants.WAIT_OPERATOR.getCode());
        map.put("resMsg",  ResultConstants.WAIT_OPERATOR.getMsg());
        logger.info("/refund/refund returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);

    }

    /**
     * 增加积分接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/addCredit", method = RequestMethod.POST)
    public void addCredit(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        logger.info("execute /refund/addCredit , params is : {} ",params.toString());
        Map<String, String> map = new HashMap<String, String>();

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
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(credit)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(operateType)||StringUtils.isEmpty(orderType)||
                StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)||StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //签名验证
        String MD5_KEY = merchantUtil.getMerchantKey(sourceCode);
        if(StringUtils.isEmpty(MD5_KEY)){
            map.put("resCode", ResultConstants.SOURCECODE_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.SOURCECODE_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        boolean checkMD5Sign = new Hsmd5Util().checkMD5Sign(params, addCreditReqOrder, MD5_KEY, requestSign);
        if(!checkMD5Sign){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }


        //创建业务数据记录
        String transId = UUIDUtils.getId(6,6);
        params.put("transId", transId);
        ResultInfo createInfo = paymentService.createTradeRecord(params);
        ResultConstants createResult = (ResultConstants)createInfo.getResult();
        if(!createInfo.getIsSuccess()){
            logger.error("addCredits occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", createResult.getCode());
            map.put("resMsg",  createResult.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        map.put("resCode", ResultConstants.WAIT_OPERATOR.getCode());
        map.put("resMsg",  ResultConstants.WAIT_OPERATOR.getMsg());
        logger.info("/refund/addCredit returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);

    }


    /**
     * 加余额加积分接口（组合退款）
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/assembleRefund", method = RequestMethod.POST)
    public void assembleRefund(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        logger.info("execute /refund/assembleRefund , params is : {} ",params.toString());
        Map<String, String> map = new HashMap<String, String>();

        //数据校验
        String orderId = params.get("orderId");
        String userId = params.get("userId");
        String mobile = params.get("mobile");
        String money = params.get("money");
        String credit = params.get("credit");
        String sourceCode = params.get("sourceCode");
        String operateType = params.get("operateType");
        String orderType = params.get("orderType");
        String returnUrl = params.get("returnUrl");
        String noticeUrl = params.get("noticeUrl");
        String requestSign = params.get(MD5Sign);
        if(StringUtils.isEmpty(orderId)||StringUtils.isEmpty(userId)||StringUtils.isEmpty(mobile)||StringUtils.isEmpty(credit)||
                StringUtils.isEmpty(sourceCode)||StringUtils.isEmpty(money)||StringUtils.isEmpty(operateType)||StringUtils.isEmpty(orderType)||
                StringUtils.isEmpty(returnUrl)||StringUtils.isEmpty(noticeUrl)||StringUtils.isEmpty(requestSign)){
            map.put("resCode", ResultConstants.PARAMETERS_ISNULL.getCode());
            map.put("resMsg",  ResultConstants.PARAMETERS_ISNULL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //签名验证
        String MD5_KEY = merchantUtil.getMerchantKey(sourceCode);
        if(StringUtils.isEmpty(MD5_KEY)){
            map.put("resCode", ResultConstants.SOURCECODE_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.SOURCECODE_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        boolean checkMD5Sign = new Hsmd5Util().checkMD5Sign(params, assembleRefundReqOrder, MD5_KEY, requestSign);
        if(!checkMD5Sign){
            map.put("resCode", ResultConstants.MD5SIGN_ISWRONG.getCode());
            map.put("resMsg",  ResultConstants.MD5SIGN_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //创建业务数据记录
        String transId = UUIDUtils.getId(6,6);
        params.put("transId", transId);
        ResultInfo createInfo = paymentService.createTradeRecord(params);
        ResultConstants createResult = (ResultConstants)createInfo.getResult();
        if(!createInfo.getIsSuccess()){
            logger.error("assembleRefund occurs an error: createTradeRecord is failed；orderId is {}",orderId);
            map.put("resCode", createResult.getCode());
            map.put("resMsg",  createResult.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        map.put("resCode", ResultConstants.WAIT_OPERATOR.getCode());
        map.put("resMsg",  ResultConstants.WAIT_OPERATOR.getMsg());
        logger.info("/refund/assembleRefund returned params is : {} ",map.toString());
        JsonUtil.writeJson(map, response);

    }



}
