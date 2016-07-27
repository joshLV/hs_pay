package com.hszsd.webpay.controller;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.config.BaoFooConfig;
import com.hszsd.webpay.config.HuichaoConfig;
import com.hszsd.webpay.config.LianLianConfig;
import com.hszsd.webpay.config.MobileConfig;
import com.hszsd.webpay.config.OnlineConfig;
import com.hszsd.webpay.service.RechargeBankService;
import com.hszsd.webpay.service.RechargeContextService;
import com.hszsd.webpay.service.TradeRecordService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.validator.RechargeValidator;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.RechargeForm;
import com.hszsd.webpay.web.form.TradeForm;
import org.apache.commons.beanutils.BeanUtils;
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
import java.util.TreeMap;

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
    public ModelAndView recharge(HttpServletRequest request, HttpServletResponse response, @Validated TradeForm tradeForm, BindingResult bind){
        logger.info("recharge is starting and tradeForm={}", tradeForm);

        Map<String, Object> map = new HashMap<String, Object>();
        //参数校验
        if(bind.hasErrors()){
            logger.error("recharge failed and validator is not pass");
            if(StringUtils.isEmpty(tradeForm.getReturnUrl())){
                map.put("operator", bind.getFieldError().getArguments()[0]);
                return new ModelAndView("/common/500", "map", map);
            }
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), (ResultConstants) bind.getFieldError().getArguments()[0]);
            return null;
        }

        //获取用户信息
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        Map attributes = principal.getAttributes();
        Result userResult = userService.getUserInfo((String) attributes.get("userId"));
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("recharge failed and UserResult={}", userResult);
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.SESSION_TIME_OUT);
            return null;
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
        if(GlobalConstants.RECHARGE.USERTYPE_BORROWER == userInfoDTO.getTypeId()){
            logger.error("recharge failed and user type is borrower");
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.USER_ISERROR);
            return null;
        }
        //判断是否实名认证
        if(GlobalConstants.RECHARGE.REALSTATUS_ISYES != userInfoDTO.getRealStatus()){
            logger.error("recharge failed and user isn't auth for real name");
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.REALSTATUS_ISNOT);
            return null;
        }
        map.put("username", userInfoDTO.getUsername());
        map.put("useMoney", userInfoDTO.getUseMoney());

        //保存交易记录信息
        ResultInfo tradeResult = null;
        try {
            Map<String, String> paramMap = BeanUtils.describe(tradeForm);
            paramMap.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.RECHARGE));
            tradeResult = tradeRecordService.createTradeRecord(map);
        } catch (Exception e) {
            logger.error("recharge occurs an error and cause by {}", e.getMessage());
            tradeResult = new ResultInfo();
            tradeResult.setIsSuccess(false);
        } finally {
            if(!tradeResult.getIsSuccess()) {
                map.put("operator", ResultConstants.OPERATOR_FAIL);
                return new ModelAndView("/common/500", "map", map);
            }
        }

        //查询可用的银行信息（银行列表、第三方充值列表）
        map = rechargeBankService.queryRechargeBank();
        if(map == Collections.EMPTY_MAP){
            map.put("operator", ResultConstants.OPERATOR_FAIL);
            return new ModelAndView("/common/500", "map", map);
        }

        map.put("operator", ResultConstants.OPERATOR_SUCCESS);
        return new ModelAndView("/recharge/recharge","map",map);
    }

    /**
     * 根据充值数据进行充值操作
     * @param request
     * @param response
     * @param rechargeForm 分装充值数据
     * @param result
     */
    @RequestMapping(value = {"toRecharge"}, method = RequestMethod.POST)
    public void toRecharge(HttpServletRequest request, HttpServletResponse response, @Validated RechargeForm rechargeForm, BindingResult result){
        Map<String, Object> map = new HashMap<String, Object>();

        //数据校验
        if(result.hasErrors()){
            logger.error("toRecharge failed and validator is not pass");
            map.put("operator",result.getFieldError().getArguments()[0]);
            JsonUtil.writeJson(map, response);
            return ;
        }

        //验证码校验
        String code = (String) request.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        if(StringUtils.isEmpty(code) || !code.toUpperCase().equals(rechargeForm.getValidCaptcha())){
            logger.error("toRecharge failed and captcha is wrong");
            map.put("operator", ResultConstants.CAPTCHA_ISWRONG);
            JsonUtil.writeJson(map, response);
            return ;
        }

        //获取用户信息
        AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        Map attributes = principal.getAttributes();
        Result userResult = userService.getUserInfo((String) attributes.get("userId"));
        if(!ResultCode.RES_OK.equals(userResult.getResCode())){
            logger.error("toRecharge failed and userResult={}", userResult);
            map.put("operator", ResultConstants.SESSION_TIME_OUT);
            JsonUtil.writeJson(map, response);
            return ;
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();

        //封装支付参数
        RechargeInDTO rechargeInDto = new RechargeInDTO(rechargeForm, userInfoDTO, RechargeModel.RECHARGE, request);
        //请求支付并返回支付结果
        RechargeOutDTO rechargeOutDto = rechargeContextService.recharge(rechargeInDto, RechargeModel.RECHARGE);
        map.put("operator", rechargeOutDto.getResult());
        if(ResultConstants.OPERATOR_FAIL == rechargeOutDto.getResult()){
            logger.error("toRecharge failed and rechargeOutDto={}", rechargeOutDto);
            JsonUtil.writeJson(map, response);
            return ;
        }
        map.put("url", rechargeOutDto.getRequest());
        map.put("urlType", rechargeOutDto.getUrlType());
        JsonUtil.writeJson(map, response);
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
        //根据第三方支付平台回调数据操作相关信息
        callRecharge(RechargeType.BAOFOO, RechargeModel.FRONT, request);

        //封装数据并同步通知商户系统
        if(callMerchantFront(request.getParameter("TransId"))){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operator", ResultConstants.OPERATOR_FAIL);
        logger.error("BaoFooFront occurs an error and cause by {}", request.getParameterMap());
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
            logger.error("baoFooBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 汇潮支付前台回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"huichaoFront"})
    public ModelAndView huichaoFront(HttpServletRequest request, HttpServletResponse response){
        //根据第三方支付平台回调数据操作相关信息
        callRecharge(RechargeType.HUICHAO, RechargeModel.FRONT, request);

        //封装数据并同步通知商户系统
        if(callMerchantFront(request.getParameter("BillNo"))){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operator", ResultConstants.OPERATOR_FAIL);
        logger.error("huichaoFront occurs an error and cause by {}", request.getParameterMap());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 汇潮支付后台回调
     * @param request
     * @param response
     */
    @RequestMapping({"huichaoBack"})
    public void huichaoBack(HttpServletRequest request, HttpServletResponse response){
        //判断第三方支付返回结果
        RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.HUICHAO, RechargeModel.BACK, request);
        try {
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        }catch (IOException e){
            logger.error("huichaoBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 网银在线前台回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"onLineFront"})
    public ModelAndView onLineFront(HttpServletRequest request, HttpServletResponse response){
        //根据第三方支付平台回调数据操作相关信息
        callRecharge(RechargeType.ONLINE, RechargeModel.FRONT, request);

        //封装数据并同步通知商户系统
        if(callMerchantFront(request.getParameter("v_oid"))){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operator", ResultConstants.OPERATOR_FAIL);
        logger.error("onLineFront occurs an error and cause by {}", request.getParameterMap());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 网银在线后台回调
     * @param request
     * @param response
     */
    @RequestMapping({"onLineBack"})
    public void onLineBack(HttpServletRequest request, HttpServletResponse response){
        //判断第三方支付返回结果
        RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.ONLINE, RechargeModel.BACK, request);
        try {
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        }catch (IOException e){
            logger.error("onLineBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 连连支付前台回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"lianLianFront"})
    public ModelAndView lianLianFront(HttpServletRequest request, HttpServletResponse response){
        //根据第三方支付平台回调数据操作相关信息
        callRecharge(RechargeType.LIANLIAN, RechargeModel.FRONT, request);

        //封装数据并同步通知商户系统
        if(callMerchantFront(request.getParameter("no_order"))){
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operator", ResultConstants.OPERATOR_FAIL);
        logger.error("lianLianFront occurs an error and cause by {}", request.getParameterMap());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 连连支付后台回调
     * @param request
     * @param response
     */
    @RequestMapping({"lianLianBack"})
    public void lianLianBack(HttpServletRequest request, HttpServletResponse response){
        //判断第三方支付返回结果
        RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.LIANLIAN, RechargeModel.BACK, request);
        try {
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        }catch (IOException e){
            logger.error("lianLianBack occurs an error and cause by {}", e.getMessage());
        }
    }
    /**
     * 移动和包支付前台回调
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"mobileFront"})
    public ModelAndView mobileFront(HttpServletRequest request, HttpServletResponse response){
        //根据第三方支付平台回调数据操作相关信息
        callRecharge(RechargeType.MOBILE, RechargeModel.FRONT, request);

        //封装数据并同步通知商户系统
        if(callMerchantFront(request.getParameter("orderId"))){
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("operator", ResultConstants.OPERATOR_FAIL);
        logger.error("mobileFront occurs an error and cause by {}", request.getParameterMap());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 移动和包支付后台回调
     * @param request
     * @param response
     */
    @RequestMapping({"mobileBack"})
    public void mobileBack(HttpServletRequest request, HttpServletResponse response){
        //判断第三方支付返回结果
        RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.MOBILE, RechargeModel.BACK, request);
        try {
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        }catch (IOException e){
            logger.error("mobileBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 第三方充值平台回调统一入口
     * @param rechargeType 充值方式
     * @param rechargeModel 调用模式
     * @param request
     * @return RechargeOutDTO 返回操作结果
     */
    public RechargeOutDTO callRecharge(RechargeType rechargeType, RechargeModel rechargeModel, HttpServletRequest request){
        //封装支付入参
        RechargeInDTO rechargeInDTO = new RechargeInDTO();
        rechargeInDTO.setType(rechargeType.getCode());
        rechargeInDTO.setRechargeModel(rechargeModel);
        rechargeInDTO.setRequest(request);
        //调用第三方支付接口
        RechargeOutDTO rechargeOutDTO = rechargeContextService.recharge(rechargeInDTO, rechargeModel);
        return rechargeOutDTO;
    }

    /**
     * 同步回调商户接口
     * 根据流水号查询交易记录信息并封装返回参数信息
     * @param transId 交易流水号
     * @return boolean 是否回调成功
     */
    public boolean callMerchantFront(String transId){
        ResultInfo resultInfo = tradeRecordService.queryTradeRecordByTransId(transId);
        if(resultInfo.getIsSuccess()){
            TradeRecordDTO tradeRecordDTO = (TradeRecordDTO) resultInfo.getParams();
            ResultConstants resEnum = tradeRecordDTO.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_2?ResultConstants.OPERATOR_SUCCESS:ResultConstants.WAIT_OPERATOR;
            //封装返回参数信息
            Map<String, String> paramMap = new TreeMap<String, String>();
            paramMap.put("transId", tradeRecordDTO.getTransId());
            paramMap.put("money", String.valueOf(tradeRecordDTO.getMoney()));
            paramMap.put("tradeStatus", String.valueOf(tradeRecordDTO.getTradeStatus()));
            paramMap.put("resCode", resEnum.getCode());
            paramMap.put("resMsg", resEnum.getMsg());

            //生成签名数据
            String md5sign = Hsmd5Util.getInstance().generateMD5Sign(paramMap, GlobalConstants.COMMON.RECHARGE_RES, MerchantUtil.getInstance().getMerchantKey(tradeRecordDTO.getSourceCode()));
            HttpUtils.sendPostRequest(tradeRecordDTO.getReturnUrl(), paramMap, md5sign);

            return true;
        }

        return false;
    }
}
