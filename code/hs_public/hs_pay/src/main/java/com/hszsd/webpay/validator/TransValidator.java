package com.hszsd.webpay.validator;

import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.web.form.TransForm;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * 流水号和支付密码数据校验器
 * Created by suocy on 2016/7/1.
 */
public class TransValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TransForm.class.equals(aClass);
    }

    /**
     * 对流水号和支付密码数据进行校验
     * 1. 账单流水号、支付密码的非空校验
     * @param o 流水号和支付密码数据对象
     * @param errors 封装错误提示信息
     */
    @Override
    public void validate(Object o, Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "payPassword", ResultConstants.PAYPASSWORD_ISNULL.getCode(), new Object[]{ResultConstants.PAYPASSWORD_ISNULL});
       
    }

}
