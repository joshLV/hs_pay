package com.hszsd.webpay.validator;

import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.web.form.BindCardForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * 绑卡表单对象数据校验器
 * Created by suocy on 2016/8/5.
 */
public class BindCardValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return BindCardForm.class.equals(aClass);
    }

    /**
     * 对绑卡表单对象数据进行校验
     * @param o 绑卡表单对象
     * @param errors 封装错误提示信息
     */
    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "userId", ResultConstants.USERID_ISNULL.getCode(), new Object[]{ResultConstants.USERID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "cardNo", ResultConstants.CARDNO_ISNULL.getCode(), new Object[]{ResultConstants.CARDNO_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "phone", ResultConstants.REGISTERTEL_ISNULL.getCode(), new Object[]{ResultConstants.REGISTERTEL_ISNULL});
    }

}
