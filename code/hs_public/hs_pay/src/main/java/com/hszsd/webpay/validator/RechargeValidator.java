package com.hszsd.webpay.validator;

import com.hszsd.webpay.common.ValidatorConstants;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.RechargeForm;
import com.hszsd.webpay.web.form.TradeForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

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

        //
        if(o instanceof TradeForm){
            rechargeValidate(o, errors);
            return ;
        }

        //
        if(o instanceof RechargeForm){
            toRechargeValidate(o, errors);
            return ;
        }

    }

    /**
     * 对充值数据进行校验
     * 1. 验证码、充值方式、金额的非空校验
     * 2. 金额数据格式校验
     * @param o 充值数据对象
     * @param errors 封装错误提示信息
     */
    public void rechargeValidate(Object o, Errors errors){
        ValidationUtils.rejectIfEmpty(errors, "userId", ValidatorConstants.BANKID_ISNULL.getCode(), new Object[]{ValidatorConstants.BANKID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "mobile", ValidatorConstants.AMOUNT_ISNULL.getCode(), new Object[]{ValidatorConstants.AMOUNT_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "sourceCode", ValidatorConstants.CAPTCHA_ISNULL.getCode(), new Object[]{ValidatorConstants.CAPTCHA_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "returnUrl", ValidatorConstants.BANKID_ISNULL.getCode(), new Object[]{ValidatorConstants.BANKID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "noticeUrl", ValidatorConstants.AMOUNT_ISNULL.getCode(), new Object[]{ValidatorConstants.AMOUNT_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "MD5Sign", ValidatorConstants.CAPTCHA_ISNULL.getCode(), new Object[]{ValidatorConstants.CAPTCHA_ISNULL});

        TradeForm tradeForm = (TradeForm) o;
    }

    public void toRechargeValidate(Object o, Errors errors){
        ValidationUtils.rejectIfEmpty(errors, "validCaptcha", ValidatorConstants.CAPTCHA_ISNULL.getCode(), new Object[]{ValidatorConstants.CAPTCHA_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "onlineBankId", ValidatorConstants.BANKID_ISNULL.getCode(), new Object[]{ValidatorConstants.BANKID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "amount", ValidatorConstants.AMOUNT_ISNULL.getCode(), new Object[]{ValidatorConstants.AMOUNT_ISNULL});

        RechargeForm rechargeForm = (RechargeForm) o;
        Pattern pattern = Pattern.compile("^[+]?(([1-9]\\d*[.]?)|(0.))(\\d{0,2})?$");
        Matcher match = pattern.matcher(rechargeForm.getAmount().trim());
        if (!match.matches()){
            errors.rejectValue("amount", ValidatorConstants.AMOUNT_WRONGFORMAT.getCode(), new Object[]{ValidatorConstants.AMOUNT_WRONGFORMAT}, "");
        }
    }
}
