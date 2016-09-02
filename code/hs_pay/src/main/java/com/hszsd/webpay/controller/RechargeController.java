package com.hszsd.webpay.controller;

import com.hszsd.common.util.Result;
import com.hszsd.common.util.ResultCode;
import com.hszsd.common.util.math.MathUtil;
import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.user.dto.GetUserInfoDTO;
import com.hszsd.user.service.UserService;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.RechargeModel;
import com.hszsd.webpay.common.RechargeType;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.common.ResultInfo;
import com.hszsd.webpay.service.PasswordCheckService;
import com.hszsd.webpay.service.RechargeBankService;
import com.hszsd.webpay.service.RechargeContextService;
import com.hszsd.webpay.service.TradeRecordService;
import com.hszsd.webpay.service.YeePayService;
import com.hszsd.webpay.util.HttpUtils;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.util.ObjectUtils;
import com.hszsd.webpay.util.WebUtils;
import com.hszsd.webpay.validator.RechargeValidator;
import com.hszsd.webpay.web.dto.QuickBankOutDTO;
import com.hszsd.webpay.web.dto.RechargeInDTO;
import com.hszsd.webpay.web.dto.RechargeOutDTO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.RechargeForm;
import com.hszsd.webpay.web.form.TradeForm;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.code.kaptcha.Constants.KAPTCHA_SESSION_KEY;

/**
 * 账户充值控制器
 * Created by gzhengDu on 2016/6/28.
 */
@Controller
public class RechargeController {
    private static final Logger logger = LoggerFactory.getLogger(RechargeController.class);

    @Autowired
    private RechargeBankService rechargeBankService;

    @Autowired
    private RechargeContextService rechargeContextService;

    @Autowired
    private TradeRecordService tradeRecordService;

    @Autowired
    private YeePayService yeePayService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordCheckService passwordCheckService;

    /**
     * 为controller指定校验器
     *
     * @param binder 数据绑定器
     */
    @InitBinder
    public void initBinder(DataBinder binder) {
        binder.setValidator(new RechargeValidator());
    }

    /**
     * PC端账户充值主页面
     *
     * @param request
     * @param response
     * @param tradeForm 充值请求参数
     * @param bind      校验结果
     * @return
     */
    @RequestMapping(value = {"/toRecharge"}, method = RequestMethod.POST)
    public ModelAndView toRecharge(HttpServletRequest request, HttpServletResponse response, @Validated TradeForm tradeForm, BindingResult bind) {
        logger.info("toRecharge is starting and tradeForm={}", tradeForm);

        Map<String, Object> map = new HashMap<String, Object>();
        //参数校验
        if (bind.hasErrors()) {
            ResultConstants validatorResult = (ResultConstants) bind.getFieldError().getArguments()[0];
            logger.error("toRecharge failed and validator is not pass and validatorResult={}", JsonUtil.obj2json(validatorResult));
            if (!StringUtils.isEmpty(tradeForm.getReturnUrl())) {
                //向请求方返回错误提示
                HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), validatorResult);
            }
            return new ModelAndView("/common/500");
        }

        //获取用户信息
        Result userResult;
        try {
            userResult = userService.getUserInfo(tradeForm.getUserId());
        }catch (Exception e){
            logger.error("toRecharge occurs an error and cause by {}", e.getMessage());
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.OPERATOR_FAIL);
            return new ModelAndView("/common/500");
        }
        if (!ResultCode.RES_OK.equals(userResult.getResCode())) {
            logger.error("toRecharge failed and UserResult={}", JsonUtil.obj2json(userResult));
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.USERID_ISNULL);
            return new ModelAndView("/common/500");
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
        logger.info("userService.getUserInfo userInfoDTO = {}", userInfoDTO);
        //判断用户类型，借款用户不能进行充值操作
        if (GlobalConstants.RECHARGE.USERTYPE_BORROWER == userInfoDTO.getTypeId()) {
            logger.error("toRecharge failed and user type is borrower");
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.USER_ISERROR);
            return new ModelAndView("/common/500");
        }
        //判断是否实名认证，用户未实名认证则不能进行充值操作
        if (GlobalConstants.RECHARGE.REALSTATUS_ISYES != userInfoDTO.getRealStatus()) {
            logger.error("toRecharge failed and user isn't auth for real name");
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.REALSTATUS_ISNOT);
            return new ModelAndView("/common/500");
        }
        map.put("userId", userInfoDTO.getUserId());
        map.put("username", userInfoDTO.getUsername());
        map.put("useMoney", MathUtil.round(userInfoDTO.getUseMoney(), 2));

        //保存交易记录信息
        ResultInfo tradeResult = new ResultInfo();
        try {
            Map<String, String> paramMap = ObjectUtils.objectToMapValue(tradeForm);
            paramMap.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.RECHARGE));
            tradeResult = tradeRecordService.createTradeRecord(paramMap);
        } catch (Exception e) {
            logger.error("toRecharge occurs an error and cause by {}", e.getMessage());
            tradeResult.setIsSuccess(false);
        } finally {
            if (!tradeResult.getIsSuccess()) {
                HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.OPERATOR_FAIL);
                return new ModelAndView("/common/500");
            }
            map.put("transId", tradeResult.getParams());
        }

        //查询可用的银行信息（银行列表、第三方充值列表）
        Map bankMap = rechargeBankService.queryRechargeBank();
        if (bankMap == Collections.EMPTY_MAP) {
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.OPERATOR_FAIL);
            return new ModelAndView("/common/500");
        }
        map.putAll(bankMap);

        map.put("sourceCode", tradeForm.getSourceCode());
        map.put("resCode", ResultConstants.OPERATOR_SUCCESS.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_SUCCESS.getMsg());
        logger.info("toRecharge success");
        return new ModelAndView("/recharge/recharge", "map", map);
    }

    /**
     * PC端根据充值数据进行充值操作
     *
     * @param request
     * @param response
     * @param rechargeForm 封装充值数据
     * @param bind         校验结果
     */
    @RequestMapping(value = {"/confirmRecharge"}, method = RequestMethod.POST)
    public void confirmRecharge(HttpServletRequest request, HttpServletResponse response, @Validated RechargeForm rechargeForm, BindingResult bind) {
        logger.info("confirmRecharge is starting and rechargeForm={}", rechargeForm);
        Map<String, Object> map = new HashMap<String, Object>();

        //数据校验
        if (bind.hasErrors()) {
            logger.error("confirmRecharge failed and validator is not pass");
            ResultConstants validatorRes = (ResultConstants) bind.getFieldError().getArguments()[0];
            map.put("resCode", validatorRes.getCode());
            map.put("resMsg", validatorRes.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //验证码校验
        String code = (String) request.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        if (StringUtils.isEmpty(code) || !code.equals(rechargeForm.getValidCaptcha().toUpperCase())) {
            logger.error("confirmRecharge failed and captcha is wrong");
            map.put("resCode", ResultConstants.CAPTCHA_ISWRONG.getCode());
            map.put("resMsg", ResultConstants.CAPTCHA_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //获取用户信息
        Result userResult;
        try {
            userResult = userService.getUserInfo(rechargeForm.getUserId());
        }catch (Exception e){
            logger.error("toRecharge occurs an error and cause by {}", e.getMessage());
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        if (!ResultCode.RES_OK.equals(userResult.getResCode())) {
            logger.error("confirmRecharge failed and userResult={}", userResult);
            map.put("resCode", ResultConstants.USER_ISERROR.getCode());
            map.put("resMsg", ResultConstants.USER_ISERROR.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();

        //封装支付参数
        RechargeInDTO rechargeInDto = new RechargeInDTO(rechargeForm, userInfoDTO, request);
        //请求支付并返回支付结果
        RechargeOutDTO rechargeOutDto = rechargeContextService.recharge(rechargeInDto, RechargeModel.RECHARGE);
        ResultConstants rechargeRes = (ResultConstants) rechargeOutDto.getResult();
        map.put("resCode", rechargeRes.getCode());
        map.put("resMsg", rechargeRes.getMsg());
        if (ResultConstants.OPERATOR_FAIL.equals(rechargeRes)) {
            logger.error("confirmRecharge failed and rechargeOutDto={}", rechargeOutDto);
            JsonUtil.writeJson(map, response);
            return;
        }
        map.put("url", rechargeOutDto.getRequest());
        map.put("urlType", rechargeOutDto.getUrlType());

        JsonUtil.writeJson(map, response);
    }

    /**
     * 移动端账户充值主页面
     *
     * @param request
     * @param response
     * @param tradeForm 充值请求参数
     * @param bind      校验结果
     * @return
     */
    @RequestMapping(value = {"/toQuickRecharge"}, method = RequestMethod.POST)
    public ModelAndView toQuickRecharge(HttpServletRequest request, HttpServletResponse response, @Validated TradeForm tradeForm, BindingResult bind) {
        logger.info("toQuickRecharge is starting and tradeForm={}", tradeForm);

        Map<String, Object> map = new HashMap<String, Object>();
        //参数校验
        if (bind.hasErrors()) {
            ResultConstants validatorRes = (ResultConstants) bind.getFieldError().getArguments()[0];
            logger.error("toQuickRecharge failed and validator is not pass and validatorRes={}", validatorRes);
            if (!StringUtils.isEmpty(tradeForm.getReturnUrl())) {
                //向请求方返回错误提示
                HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), validatorRes);
            }
            return new ModelAndView("/common/500");
        }

        //获取用户信息
        Result userResult;
        try {
            userResult = userService.getUserInfo(tradeForm.getUserId());
        }catch (Exception e){
            logger.error("toQuickRecharge occurs an error and cause by {}", e.getMessage());
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.OPERATOR_FAIL);
            return new ModelAndView("/common/500");
        }
        if (!ResultCode.RES_OK.equals(userResult.getResCode())) {
            logger.error("toQuickRecharge failed and UserResult={}", userResult);
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.USER_ISERROR);
            return new ModelAndView("/common/500");
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();
        logger.info("userService.getUserInfo userInfoDTO = {}", userInfoDTO);
        if (GlobalConstants.RECHARGE.USERTYPE_BORROWER == userInfoDTO.getTypeId()) {
            logger.error("toQuickRecharge failed and user type is borrower");
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.USER_ISERROR);
            return new ModelAndView("/common/500");
        }
        //判断是否实名认证
        if (GlobalConstants.RECHARGE.REALSTATUS_ISYES != userInfoDTO.getRealStatus()) {
            logger.error("toQuickRecharge failed and user isn't auth for real name");
            //向请求方返回错误提示
            HttpUtils.sendErrorPostRequest(tradeForm.getReturnUrl(), ResultConstants.REALSTATUS_ISNOT);
            return null;
        }
        map.put("userId", userInfoDTO.getUserId());

        //保存交易记录信息
        ResultInfo tradeResult = new ResultInfo();
        try {
            Map<String, String> paramMap = ObjectUtils.objectToMapValue(tradeForm);
            paramMap.put("operateType", String.valueOf(GlobalConstants.TRADE_RECORD.RECHARGE));
            tradeResult = tradeRecordService.createTradeRecord(paramMap);
        } catch (Exception e) {
            logger.error("toQuickRecharge occurs an error and cause by {}", e.getMessage());
            tradeResult.setIsSuccess(false);
        } finally {
            if (tradeResult == null || !tradeResult.getIsSuccess()) {
                return new ModelAndView("/common/500");
            }
            map.put("transId", tradeResult.getParams());
        }

        //查询可用的快捷卡信息
        ResultInfo queryResult = yeePayService.queryQuickBank(tradeForm.getUserId());
        if(!queryResult.getIsSuccess()){
            return new ModelAndView("/common/500");
        }
        List<QuickBankOutDTO> quickBankOutDTOs = (List<QuickBankOutDTO>)queryResult.getParams();
        map.put("quickBankOutDTOs", quickBankOutDTOs);

        map.put("resCode", ResultConstants.OPERATOR_SUCCESS.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_SUCCESS.getMsg());
        map.put("sourceCode", tradeForm.getSourceCode());
        map.put("callBackUrl", request.getAttribute("basePath")+"/backQuickRecharge.do");//支付平台内部返回充值界面接口
        logger.info("toQuickRecharge success");
        return new ModelAndView("/mobile/recharge", "map", map);
    }

    /**
     * 支付平台内部返回充值界面接口
     *
     * @param request
     * @param response
     * @param transId  交易流水号
     * @Return
     */
    @RequestMapping(value = {"/backQuickRecharge"})
    public ModelAndView backQuickRecharge(HttpServletRequest request, HttpServletResponse response, String transId) {
        logger.info("backRecharge is starting and transId={}", transId);
        Map<String, Object> map = new HashMap<String, Object>();

        TradeRecordDTO tradeRecordDTO = tradeRecordService.queryTradeRecordByTransId(transId);
        if (tradeRecordDTO == null) {
            logger.error("backRecharge failed and trade record is null");
            map.put("resCode", ResultConstants.TRANSID_ISWRONG.getCode());
            map.put("resMsg", ResultConstants.TRANSID_ISWRONG.getMsg());

            return new ModelAndView("/mobile/recharge", "map", map);
        }
        //查询可用的快捷卡信息
        ResultInfo queryResult = yeePayService.queryQuickBank(tradeRecordDTO.getUserId());
        if(!queryResult.getIsSuccess()){
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
            return new ModelAndView("/mobile/recharge", "map", map);
        }
        List<QuickBankOutDTO> quickBankOutDTOs = (List<QuickBankOutDTO>)queryResult.getParams();
        map.put("quickBankOutDTOs", quickBankOutDTOs);

        map.put("transId", tradeRecordDTO.getTransId());
        map.put("userId", tradeRecordDTO.getUserId());
        map.put("sourceCode", tradeRecordDTO.getSourceCode());
        map.put("resCode", ResultConstants.OPERATOR_SUCCESS.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_SUCCESS.getMsg());
        logger.info("backRecharge success");
        return new ModelAndView("/mobile/recharge", "map", map);
    }

    /**
     * 移动端根据充值数据进行充值操作
     *
     * @param request
     * @param response
     * @param rechargeForm 封装充值数据
     * @param bind         校验结果
     */
    @RequestMapping(value = {"/confirmQuickRecharge"}, method = RequestMethod.POST)
    public void confirmQuickRecharge(HttpServletRequest request, HttpServletResponse response, @Validated RechargeForm rechargeForm, BindingResult bind) {
        logger.info("confirmQuickRecharge is starting and rechargeForm={}", rechargeForm);
        Map<String, String> map = new HashMap<String, String>();

        ResultConstants result = null;

        //数据校验
        if (bind.hasErrors()) {
            logger.error("confirmQuickRecharge failed and validator is not pass");
            result = (ResultConstants) bind.getFieldError().getArguments()[0];
            map.put("resCode", result.getCode());
            map.put("resMsg", result.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }

        //校验交易密码
        ResultInfo checkPayPassWord = passwordCheckService.checkPayPassWord(rechargeForm.getUserId(), rechargeForm.getPayPassword());
        if (!checkPayPassWord.getIsSuccess()) {
            map.put("resCode", ResultConstants.PAYPASSWORD_ISWRONG.getCode());
            map.put("resMsg", String.valueOf(checkPayPassWord.getResult()));
            JsonUtil.writeJson(map, response);
            return;
        }
        //校验交易记录是否存在
        TradeRecordDTO tradeRecordDTO = tradeRecordService.queryTradeRecordByTransId(rechargeForm.getTransId());
        if (tradeRecordDTO == null) {
            logger.error("confirmQuickRecharge failed and transId is wrong and transId={}", rechargeForm.getTransId());
            map.put("resCode", ResultConstants.TRANSID_ISWRONG.getCode());
            map.put("resMsg", ResultConstants.TRANSID_ISWRONG.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        //获取用户信息
        Result userResult;
        try{
            userResult = userService.getUserInfo(rechargeForm.getUserId());
        }catch (Exception e){
            logger.error("confirmQuickRecharge occurs an error and cause by {}", e.getMessage());
            map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
            map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        if (!ResultCode.RES_OK.equals(userResult.getResCode())) {
            logger.error("confirmQuickRecharge failed and userResult={}", userResult);
            map.put("resCode", ResultConstants.USER_ISERROR.getCode());
            map.put("resMsg", ResultConstants.USER_ISERROR.getMsg());
            JsonUtil.writeJson(map, response);
            return;
        }
        GetUserInfoDTO userInfoDTO = (GetUserInfoDTO) userResult.getResult();

        //封装支付参数
        RechargeInDTO rechargeInDto = new RechargeInDTO(rechargeForm, userInfoDTO, request);
        //请求支付并返回支付结果
        RechargeOutDTO rechargeOutDto = rechargeContextService.recharge(rechargeInDto, RechargeModel.RECHARGE);

        //初始化返回给商户的数据
        map = initMerchantReturnData(rechargeForm.getTransId(), tradeRecordDTO.getReturnUrl(), rechargeOutDto);

        JsonUtil.writeJson(map, response);
    }

    /**
     * 宝付支付前台回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/callback/baoFooFront"})
    public ModelAndView baoFooFront(HttpServletRequest request, HttpServletResponse response) {
        logger.info("baoFooFront is starting and paramMap = {}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //根据第三方支付平台回调数据操作相关信息
            callRecharge(RechargeType.BAOFOO, RechargeModel.FRONT, request);

            //封装数据并同步通知商户系统
            if (callMerchantFront(request.getParameter("TransID"))) {
                return null;
            }
            logger.error("baoFooFront occurs an error and callMerchantFront failed and transId={}", request.getParameter("TransId"));
        } catch (Exception e) {
            logger.error("baoFooFront occurs an error and cause by {}", e.getMessage());
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
        return new ModelAndView("/common/500", "map", map);

    }

    /**
     * 宝付支付后台回调
     *
     * @param request
     * @param response
     */
    @RequestMapping({"/callback/baoFooBack"})
    public void baoFooBack(HttpServletRequest request, HttpServletResponse response) {
        logger.info("baoFooBack is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //判断第三方支付返回结果
            RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.BAOFOO, RechargeModel.BACK, request);
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        } catch (IOException e) {
            logger.error("baoFooBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 汇潮支付前台回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/callback/huichaoFront"})
    public ModelAndView huichaoFront(HttpServletRequest request, HttpServletResponse response) {
        logger.info("huichaoFront is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //根据第三方支付平台回调数据操作相关信息
            callRecharge(RechargeType.HUICHAO, RechargeModel.FRONT, request);

            //封装数据并同步通知商户系统
            if (callMerchantFront(request.getParameter("BillNo"))) {
                return null;
            }
            logger.error("huichaoFront occurs an error and callMerchantFront failed and transId={}", request.getParameter("BillNo"));
        } catch (Exception e) {
            logger.error("huichaoFront occurs an error and cause by {}", e.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 汇潮支付后台回调
     *
     * @param request
     * @param response
     */
    @RequestMapping({"/callback/huichaoBack"})
    public void huichaoBack(HttpServletRequest request, HttpServletResponse response) {
        logger.info("huichaoBack is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //判断第三方支付返回结果
            RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.HUICHAO, RechargeModel.BACK, request);
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        } catch (IOException e) {
            logger.error("huichaoBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 网银在线前台回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/callback/onLineFront"})
    public ModelAndView onLineFront(HttpServletRequest request, HttpServletResponse response) {
        logger.info("onLineFront is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //根据第三方支付平台回调数据操作相关信息
            callRecharge(RechargeType.ONLINE, RechargeModel.FRONT, request);

            //封装数据并同步通知商户系统
            if (callMerchantFront(request.getParameter("v_oid"))) {
                return null;
            }
            logger.error("onLineFront occurs an error and callMerchantFront failed and transId={}", request.getParameter("v_oid"));
        } catch (Exception e) {
            logger.error("onLineFront occurs an error and cause by {}", e.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 网银在线后台回调
     *
     * @param request
     * @param response
     */
    @RequestMapping({"/callback/onLineBack"})
    public void onLineBack(HttpServletRequest request, HttpServletResponse response) {
        logger.info("onLineBack is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //判断第三方支付返回结果
            RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.ONLINE, RechargeModel.BACK, request);
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        } catch (IOException e) {
            logger.error("onLineBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 连连支付前台回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/callback/lianLianFront"})
    public ModelAndView lianLianFront(HttpServletRequest request, HttpServletResponse response) {
        logger.info("lianLianFront is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //根据第三方支付平台回调数据操作相关信息
            callRecharge(RechargeType.LIANLIAN, RechargeModel.FRONT, request);

            //封装数据并同步通知商户系统
            if (callMerchantFront(request.getParameter("no_order"))) {
                return null;
            }
            logger.error("lianLianFront occurs an error and callMerchantFront failed and transId={}", request.getParameter("no_order"));
        } catch (Exception e) {
            logger.error("lianLianFront occurs an error and cause by {}", e.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 连连支付后台回调
     *
     * @param request
     * @param response
     */
    @RequestMapping({"/callback/lianLianBack"})
    public void lianLianBack(HttpServletRequest request, HttpServletResponse response) {
        logger.info("lianLianBack is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //判断第三方支付返回结果
            RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.LIANLIAN, RechargeModel.BACK, request);
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        } catch (IOException e) {
            logger.error("lianLianBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 移动和包支付前台回调
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping({"/callback/mobileFront"})
    public ModelAndView mobileFront(HttpServletRequest request, HttpServletResponse response) {
        logger.info("mobileFront is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //根据第三方支付平台回调数据操作相关信息
            callRecharge(RechargeType.MOBILE, RechargeModel.FRONT, request);

            //封装数据并同步通知商户系统
            if (callMerchantFront(request.getParameter("orderId"))) {
                return null;
            }
            logger.error("mobileFront occurs an error and callMerchantFront failed and transId={}", request.getParameter("orderId"));
        } catch (Exception e) {
            logger.error("mobileFront occurs an error and cause by {}", e.getMessage());
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
        map.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
        return new ModelAndView("/common/500", "map", map);
    }

    /**
     * 移动和包支付后台回调
     *
     * @param request
     * @param response
     */
    @RequestMapping({"/callback/mobileBack"})
    public void mobileBack(HttpServletRequest request, HttpServletResponse response) {
        logger.info("mobileBack is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //判断第三方支付返回结果
            RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.MOBILE, RechargeModel.BACK, request);
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        } catch (IOException e) {
            logger.error("mobileBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 移动和包支付后台回调
     *
     * @param request
     * @param response
     */
    @RequestMapping({"/callback/yeePayBack"})
    public void yeePayBack(HttpServletRequest request, HttpServletResponse response) {
        logger.info("yeePayBack is starting and paramMap={}", JsonUtil.obj2json(request.getParameterMap()));
        try {
            //判断第三方支付返回结果
            RechargeOutDTO rechargeOutDTO = callRecharge(RechargeType.YEEPAY, RechargeModel.BACK, request);
            //写状态回第三方支付接口
            response.getWriter().print(rechargeOutDTO.getBean());
        } catch (IOException e) {
            logger.error("yeePayBack occurs an error and cause by {}", e.getMessage());
        }
    }

    /**
     * 第三方充值平台回调统一入口
     *
     * @param rechargeType  充值方式
     * @param rechargeModel 调用模式
     * @param request
     * @return RechargeOutDTO 返回操作结果
     */
    public RechargeOutDTO callRecharge(RechargeType rechargeType, RechargeModel rechargeModel, HttpServletRequest request) {
        logger.info("callRecharge is starting and rechargeType={}, rechargeModel={}", rechargeType, rechargeModel);
        //封装支付入参
        RechargeInDTO rechargeInDTO = new RechargeInDTO();
        rechargeInDTO.setType(rechargeType.getCode());
        rechargeInDTO.setRequest(request);

        RechargeOutDTO rechargeOutDTO = new RechargeOutDTO();
        try {
            //调用第三方支付接口
            rechargeOutDTO = rechargeContextService.recharge(rechargeInDTO, rechargeModel);
        } catch (Exception e) {
            logger.error("callRecharge occurs an error and cause by {}", e.getMessage());
        }
        return rechargeOutDTO;
    }

    /**
     * 同步回调商户接口
     * 根据流水号查询交易记录信息并封装返回参数信息
     *
     * @param transId 交易流水号
     * @return boolean 是否回调成功
     */
    public boolean callMerchantFront(String transId) {
        logger.info("callMerchantFront is starting and transId={}", transId);
        try {
            TradeRecordDTO tradeRecordDTO = tradeRecordService.queryTradeRecordByTransId(transId);
            if (tradeRecordDTO == null) {
                logger.info("callMerchantFront failed and tradeRecordDTO is null");
                return false;
            }
            ResultConstants resEnum = tradeRecordDTO.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_2 ? ResultConstants.OPERATOR_SUCCESS : ResultConstants.WAIT_OPERATOR;
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
        } catch (Exception e) {
            logger.error("callMerchantFront occurs an error and cause by {}", e.getMessage());
        }
        return false;
    }

    /**
     * 为移动端的同步回调初始化数据
     * @param transId 交易流水号
     * @param returnUrl 商户同步返回地址
     * @param rechargeOutDTO 易宝充值返回结果
     * @return 包含第三方返回支付信息的map
     */
    public Map<String, String> initMerchantReturnData(String transId, String returnUrl, RechargeOutDTO rechargeOutDTO) {
        logger.info("initMerchantReturnData is starting and transId={}, rechargeOutDTO={}", transId, rechargeOutDTO);
        Map<String, String> paramMap = new HashMap<String, String>();
        ResultConstants resultConstants = (ResultConstants) rechargeOutDTO.getResult();
        paramMap.put("resCode", resultConstants.getCode());
        paramMap.put("resMsg", resultConstants.getMsg());
        if (ResultConstants.OPERATOR_SUCCESS.equals(resultConstants) && rechargeOutDTO.getBean() != null) {
            paramMap.putAll((Map<String, String>) rechargeOutDTO.getBean());
            paramMap.put("resCode", ResultConstants.WAIT_OPERATOR.getCode());
            paramMap.put("resMsg", ResultConstants.WAIT_OPERATOR.getMsg());
            try {
                TradeRecordDTO tradeRecordDTO;
                for (int i = 0; i < 3; i++) {
                    Thread.currentThread().sleep(1000 * 5);
                    tradeRecordDTO = tradeRecordService.queryTradeRecordByTransId(transId);
                    if (tradeRecordDTO.getTradeStatus() != GlobalConstants.TRADE_RECORD.TRADE_STATUS_1) {
                        resultConstants = (tradeRecordDTO.getTradeStatus() == GlobalConstants.TRADE_RECORD.TRADE_STATUS_2
                                ? ResultConstants.OPERATOR_SUCCESS : ResultConstants.OPERATOR_FAIL);
                        paramMap.put("resCode", resultConstants.getCode());
                        paramMap.put("resMsg", resultConstants.getMsg());
                        break;
                    }
                }
            } catch (InterruptedException e) {
                logger.error("initMerchantReturnData occurs an error and cause by {}", e.getMessage());
                paramMap.put("resCode", ResultConstants.OPERATOR_FAIL.getCode());
                paramMap.put("resMsg", ResultConstants.OPERATOR_FAIL.getMsg());
            }
        }

        String url = StringUtils.join(returnUrl, "?", WebUtils.generateUrl(paramMap));
        paramMap.put("url", url);
        logger.info("initMerchantReturnData success and url = {}", url);

        return paramMap;
    }

    @RequestMapping({"testFront"})
    public ModelAndView testFront(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = WebUtils.requestToMap(request);
        logger.info("testFront is starting map={}", map);
        logger.info("testFront map={}", map);

        return new ModelAndView("/common/index");
    }

    @RequestMapping({"testBack"})
    public void testBack(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> map = WebUtils.requestToMap(request);
        logger.info("testBack map={}", map);
        try {
            response.getWriter().write("OK");
        } catch (Exception e) {

        }
    }
}
