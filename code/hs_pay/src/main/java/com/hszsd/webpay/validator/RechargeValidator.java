package com.hszsd.webpay.validator;

import com.hszsd.md5.util.Hsmd5Util;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.util.MerchantUtil;
import com.hszsd.webpay.web.form.RechargeForm;
import com.hszsd.webpay.web.form.TradeForm;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 充值数据校验器
 * Created by gzhengDu on 2016/7/1.
 */
public class RechargeValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TradeForm.class.equals(aClass) || RechargeForm.class.equals(aClass);
    }

    /**
     * 根据参数对象类型触发相应校验方法
     * @param o 分装校验数据的参数对象
     * @param errors 封装错误提示信息
     */
    @Override
    public void validate(Object o, Errors errors) {

        //进入充值页面时对参数进行校验
        if(o instanceof TradeForm){
            rechargeValidate(o, errors);
            return ;
        }

        //确认充值时对参数进行校验
        if(o instanceof RechargeForm){
            toRechargeValidate(o, errors);
            return ;
        }

    }

    /**
     * 对充值请求所携带参数进行校验
     * 1. 用户编号、手机号、平台来源、前台回调地址、后台回调地址、MD5签名的非空校验
     * 2. MD5签名正确性校验
     * @param o 充值请求所携带参数
     * @param errors 封装错误提示信息
     */
    public void rechargeValidate(Object o, Errors errors){
        ValidationUtils.rejectIfEmpty(errors, "userId", ResultConstants.USERID_ISNULL.getCode(), new Object[]{ResultConstants.USERID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "mobile", ResultConstants.MOBILE_ISNULL.getCode(), new Object[]{ResultConstants.MOBILE_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "sourceCode", ResultConstants.SOURCECODE_ISNULL.getCode(), new Object[]{ResultConstants.SOURCECODE_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "returnUrl", ResultConstants.RETURNURL_ISNULL.getCode(), new Object[]{ResultConstants.RETURNURL_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "noticeUrl", ResultConstants.NOTICEURL_ISNULL.getCode(), new Object[]{ResultConstants.NOTICEURL_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "MD5Sign", ResultConstants.MD5SIGN_ISNULL.getCode(), new Object[]{ResultConstants.MD5SIGN_ISNULL});

        TradeForm tradeForm = (TradeForm) o;
        Map<String, String> map = null;
        try{
            map = BeanUtils.describe(tradeForm);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(null == map){
            errors.rejectValue("tradeForm", ResultConstants.OPERATOR_FAIL.getCode(), new Object[]{ResultConstants.OPERATOR_FAIL}, "");
            return ;
        }
        //校验MD5签名是否正确
        if(!Hsmd5Util.getInstance().checkMD5Sign(map, GlobalConstants.COMMON.RECHARGE_REQ, MerchantUtil.getInstance().getMerchantKey(tradeForm.getSourceCode()), tradeForm.getMD5Sign())){
            errors.rejectValue("MD5Sign", ResultConstants.MD5SIGN_ISWRONG.getCode(), new Object[]{ResultConstants.MD5SIGN_ISWRONG},"");
        }
    }

    /**
     * 对充值数据进行校验
     * 1. 客户来源为H5时：交易流水号、用户编号、验证码、充值方式、金额的非空校验
     *    客户来源不为H5时：交易流水号、用户编号、交易密码、充值方式、金额的非空校验
     * 2. 金额数据格式校验
     * @param o 充值数据对象
     * @param errors 封装错误提示信息
     */
    public void toRechargeValidate(Object o, Errors errors){
        RechargeForm rechargeForm = (RechargeForm) o;

        ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "userId", ResultConstants.USERID_ISNULL.getCode(), new Object[]{ResultConstants.USERID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "sourceCode", ResultConstants.SOURCECODE_ISNULL.getCode(), new Object[]{ResultConstants.SOURCECODE_ISNULL});
        //如果客户来源为微信则不需要校验验证码，但需要校验交易密码；否则只需校验验证码不需要校验交易密码
        if(GlobalConstants.TRADE_RECORD.SOURCE_CODE_H5.equals(rechargeForm.getSourceCode())){
            ValidationUtils.rejectIfEmpty(errors, "payPassword", ResultConstants.PAYPASSWORD_ISNULL.getCode(), new Object[]{ResultConstants.PAYPASSWORD_ISNULL});
        }else {
            ValidationUtils.rejectIfEmpty(errors, "validCaptcha", ResultConstants.CAPTCHA_ISNULL.getCode(), new Object[]{ResultConstants.CAPTCHA_ISNULL});
        }
        ValidationUtils.rejectIfEmpty(errors, "onlineBankId", ResultConstants.BANKID_ISNULL.getCode(), new Object[]{ResultConstants.BANKID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "money", ResultConstants.MONEY_ISNULL.getCode(), new Object[]{ResultConstants.MONEY_ISNULL});

        Pattern pattern = Pattern.compile("^[+]?(([1-9]\\d*[.]?)|(0.))(\\d{0,2})?$");
        Matcher match = pattern.matcher(rechargeForm.getMoney());
        if (!match.matches()){
            errors.rejectValue("money", ResultConstants.MONEY_WRONGFORMAT.getCode(), new Object[]{ResultConstants.MONEY_WRONGFORMAT}, "");
        }
    }
}
