package com.hszsd.webpay.validator;

import com.hszsd.webpay.common.ValidatorConstants;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.RechargeForm;
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
        return RechargeForm.class.equals(aClass);
    }

    /**
     * 对充值数据进行校验
     * 1. 验证码、充值方式、金额的非空校验
     * 2. 金额数据格式校验
     * @param o 充值数据对象
     * @param errors 封装错误提示信息
     */
    @Override
    public void validate(Object o, Errors errors) {

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

    public void test(Object o, Errors errors){
        ValidationUtils.rejectIfEmpty(errors, "transId", ValidatorConstants.CAPTCHA_ISNULL.getCode(), new Object[]{ValidatorConstants.CAPTCHA_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "userId", ValidatorConstants.BANKID_ISNULL.getCode(), new Object[]{ValidatorConstants.BANKID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "money", ValidatorConstants.AMOUNT_ISNULL.getCode(), new Object[]{ValidatorConstants.AMOUNT_ISNULL});

    }
}
