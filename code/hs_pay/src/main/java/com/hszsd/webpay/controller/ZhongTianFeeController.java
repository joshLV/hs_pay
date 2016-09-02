package com.hszsd.webpay.controller;

import com.hszsd.webpay.service.ZhongTianFeeService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
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
 * 中天缴费支付控制器
 * Created by suocy on 2016/8/25.
 */
@Controller
@RequestMapping("/ztFee")
public class ZhongTianFeeController {

    private static final Logger logger = LoggerFactory.getLogger(ZhongTianFeeController.class);

    @Autowired
    private ZhongTianFeeService zhongTianFeeService;


    /**
     * 中天物业---ERP通知缴费成功
     * @param request
     * @param response
     */
    @RequestMapping(value = "/ztwyFeeSuccess", method = RequestMethod.POST)
    public void ztwyFeeSuccess(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        Map<String, String> map = new HashMap<String, String>();
        logger.info("execute /ztFee/ztwyFeeSuccess , params is {} ",params.toString());
        boolean noticeFlag = zhongTianFeeService.ztwyFeeNotice(params, true);
        map.put("result",String.valueOf(noticeFlag));
        logger.info("/ztFee/ztwyFeeSuccess , result = {} ",noticeFlag);
        JsonUtil.writeJson(map, response);
    }

    /**
     * 中天物业---ERP通知缴费失败
     * @param request
     * @param response
     */
    @RequestMapping(value = "/ztwyFeeFail", method = RequestMethod.POST)
    public void ztwyFeeFail(HttpServletRequest request, HttpServletResponse response){
        //接收的参数
        Map<String,String> params = HttpUtils.getParamsFromRequest(request);
        Map<String, String> map = new HashMap<String, String>();
        logger.info("execute /ztFee/ztwyFeeFail , params is {} ",params.toString());
        boolean noticeFlag = zhongTianFeeService.ztwyFeeNotice(params, false);
        map.put("result",String.valueOf(noticeFlag));
        logger.info("/ztFee/ztwyFeeFail , result = {} ",noticeFlag);
        JsonUtil.writeJson(map, response);
    }




}
