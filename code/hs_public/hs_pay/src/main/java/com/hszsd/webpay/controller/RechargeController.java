package com.hszsd.webpay.controller;

import com.hszsd.user.dto.User;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.config.BaoFooConfig;
import com.hszsd.webpay.service.RechargeBankService;
import com.hszsd.webpay.service.RechargeContextService;
import com.hszsd.webpay.service.TradeRecordService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.validator.RechargeValidator;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import com.hszsd.webpay.web.form.RechargeForm;
import com.hszsd.webpay.web.form.TradeForm;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

/**
 * 账户充值控制器
 * Created by gzhengDu on 2016/6/28.
 */
@Controller
@RequestMapping("/recharge")
public class RechargeController {
    private static final Logger logger = LoggerFactory.getLogger(RechargeController.class);

    @Autowired
    private RechargeBankService rechargeBankService;

    @Autowired
    private RechargeContextService rechargeContextService;

    @Autowired
    private TradeRecordService tradeRecordService;

    @Autowired
    private UserService userService;

    /**
     * 为controller指定校验器
     * @param binder
     */
    @InitBinder
    public void initBinder(DataBinder binder){
        binder.setValidator(new RechargeValidator());
    }

    /**
     * 账户充值主页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = {"recharge"}, method = RequestMethod.POST)
    public ModelAndView recharge(HttpServletRequest request, HttpServletResponse response, @Validated TradeForm tradeForm, BindingResult result){
        logger.info("recharge is starting and tradeForm={}", tradeForm);

        Map<String, Object> map = new HashMap<String, Object>();
        //参数校验
        if(result.hasErrors()){
            if(StringUtils.isEmpty(tradeForm.getReturnUrl())){
                map.put("operator", ResultConstants.PARAMETERS_ISNULL);
                return new ModelAndView("/common/500", "map", map);
            }
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), (ResultConstants) result.getFieldError().getArguments()[0]);
            return null;
        }

        //获取登录信息
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        Map attributes = principal.getAttributes();
        String username = (String) attributes.get("username");
        if(StringUtils.isEmpty(username)){
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.SESSION_TIME_OUT);
        }

        ResultInfo result = tradeRecordService.createTradeRecord();
        //查询可用的银行信息（银行列表、第三方充值列表）
        map = rechargeBankService.queryRechargeBank();
        if(map != Collections.EMPTY_MAP){
            map.put("operator", ResultConstants.OPERATOR_SUCCESS);
            return new ModelAndView("/recharge/recharge","map",map);
        }

        map.put("operator", ResultConstants.OPERATOR_FAIL);
        JsonUtil.writeJson(map, response);
        return null;
    }

    /**
     * 根据充值数据进行充值支付操作
     * @param request
     * @param response
     * @param rechargeForm 分装充值数据
     * @param result
     */
    @RequestMapping(value = {"toRecharge"}, method = RequestMethod.POST)
    public void toRecharge(HttpServletRequest request, HttpServletResponse response, @Validated RechargeForm rechargeForm, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();

        User user = new User();

        //数据校验
        if(result.hasErrors()){
            map.put("operator",result.getFieldError().getArguments()[0]);
            JsonUtil.writeJson(map, response);
            return ;
        }

        //验证码校验
        String code = (String) request.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        if(StringUtils.isEmpty(code) || !code.toUpperCase().equals(rechargeForm.getValidCaptcha())){
            map.put("operator", ResultConstants.CAPTCHA_ISWRONG);
            JsonUtil.writeJson(map, response);
            return ;
        }

        //封装支付参数
        RechargeInDTO rechargeInDto = new RechargeInDTO(rechargeForm.getBankCode()[0], rechargeForm.getBankCode()[1], rechargeForm.getAmount(), user, request);
        //请求支付并返回支付结果
        RechargeOutDTO rechargeOutDto = rechargeContextService.recharge(rechargeInDto, RechargeModel.RECHARGE);
        return ;
    }

    /**
     * 宝付支付前台回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"baoFooFront"})
    public ModelAndView baoFooFront(HttpServletRequest request, HttpServletResponse response){
        Map<String, Object> map = new HashMap<String, Object>();

        RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.BAOFOO, RechargeModel.FRONT, request);
        //判断第三方支付返回结果
        if(BaoFooConfig.SUCCESS.equals(rechargeOutDTO.getBean()) &&
                BaoFooConfig.RESULT_SUCCESS.equals(rechargeOutDTO.getResult())){
            map.put("operator", ResultConstants.OPERATOR_SUCCESS);
            return new ModelAndView("redirect:/user/account/detail.html", "map", map);
        }
        map.put("operator", ResultConstants.OPERATOR_FAIL);
        logger.error("BaoFooFront occurs an error and cause by {}", request.getParameter("ResultDesc"));
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 宝付支付后台回调
     * @param request
     * @param response
     */
    @RequestMapping({"baoFooBack"})
    public void baoFooBack(HttpServletRequest request, HttpServletResponse response){
        //判断第三方支付返回结果
        RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.BAOFOO, RechargeModel.BACK, request);
        try {
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        }catch (IOException e){
            logger.error("BaoFooFront occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 调用第三方充值统一入口
     * @param rechargeType 充值方式
     * @param rechargeModel 调用模式
     * @param request
     * @return RechargeOutDTO 返回操作结果
     */
    public RechargeOutDTO callRecharge(RechargeType rechargeType, RechargeModel rechargeModel, HttpServletRequest request){
        //封装支付入参
        RechargeInDTO rechargeInDTO = new RechargeInDTO();
        rechargeInDTO.setType(rechargeType.getCode());
        rechargeInDTO.setRequest(request);
        //调用第三方支付接口
        RechargeOutDTO rechargeOutDTO = rechargeContextService.recharge(rechargeInDTO, rechargeModel);
        return rechargeOutDTO;
    }

    /**
     * 更新支付状态为失败
     * @param rechargeType 支付方式
     * @param request
     * @return RechargeOutDTO 返回操作结果
     */
    public RechargeOutDTO toSaveForFail(RechargeType rechargeType, HttpServletRequest request){
        //封装支付入参
        RechargeInDTO rechargeInDTO = new RechargeInDTO();
        rechargeInDTO.setType(rechargeType.getCode());
        rechargeInDTO.setRequest(request);
        //修改支付状态为失败
        RechargeOutDTO rechargeOutDTO = rechargeContextService.saveBackForFail(rechargeInDTO);
        return rechargeOutDTO;
    }
}
