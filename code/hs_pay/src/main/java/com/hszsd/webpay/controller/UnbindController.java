package com.hszsd.webpay.controller;

import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.UnbindService;
import com.hszsd.webpay.util.JsonUtil;
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
 * 解绑控制器
 * Created by gzhengDu on 2016/8/15.
 */
@Controller
public class UnbindController {
    private final static Logger logger = LoggerFactory.getLogger(UnbindController.class);

    @Autowired
    private UnbindService unbindService;

    /**
     * 易宝查询绑卡列表接口
     * @param request
     * @param response
     */
    @RequestMapping("yeePayQueryBind")
    public void yeePayQueryBind(HttpServletRequest request, HttpServletResponse response, String userId){
        logger.info("yeePayQueryBind is starting and userId={}", userId);
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isEmpty(userId)){
            logger.info("yeePayQueryBind failed and userId is null");
            map.put("resCode", ResultConstants.USERID_ISNULL.getCode());
            map.put("resMsg", ResultConstants.USERID_ISNULL.getCode());
            JsonUtil.writeJson(map, response);
            return ;
        }

        ResultInfo resultInfo = unbindService.yeePayQueryBind(userId);
        logger.info("yeePayQueryBind resultInfo={}", resultInfo);
        ResultConstants resultConstants = (ResultConstants) resultInfo.getResult();
        map.put("resCode", resultConstants.getCode());
        map.put("resCode", resultConstants.getMsg());
        JsonUtil.writeJson(resultInfo, response);
        return;
    }

    /**
     * 易宝解绑接口
     * @param request
     * @param response
     */
    @RequestMapping("yeePayUnbind")
    public void yeePayUnbind(HttpServletRequest request, HttpServletResponse response, String userId, String bindId, String cardTop, String cardLast){
        logger.info("yeePayUnbind is starting and userId={}", userId);
        Map<String, String> map = new HashMap<String, String>();
        if(StringUtils.isEmpty(userId)){
            logger.info("yeePayUnbind failed and userId is null");
            map.put("resCode", ResultConstants.USERID_ISNULL.getCode());
            map.put("resMsg", ResultConstants.USERID_ISNULL.getCode());
            JsonUtil.writeJson(map, response);
            return ;
        }

        ResultInfo resultInfo = unbindService.yeePayUnbind(userId, bindId, cardTop, cardLast);
        logger.info("yeePayUnbind resultInfo={}", resultInfo);
        ResultConstants resultConstants = (ResultConstants) resultInfo.getResult();
        map.put("resCode", resultConstants.getCode());
        map.put("resCode", resultConstants.getMsg());
        JsonUtil.writeJson(resultInfo, response);
        return;
    }
}
