package com.hszsd.webpay.validator;

import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.web.form.BindCardConfirmForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * 确认绑卡表单对象数据校验器
 * Created by suocy on 2016/8/5.
 */
public class BindCardConfirmValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return BindCardConfirmForm.class.equals(aClass);
    }

    /**
     * 对确认绑卡表单对象数据进行校验
     * @param o 确认绑卡表单对象
     * @param errors 封装错误提示信息
     */
    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "userId", ResultConstants.USERID_ISNULL.getCode(), new Object[]{ResultConstants.USERID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "smsCode", ResultConstants.SMSCODE_ISNULL.getCode(), new Object[]{ResultConstants.SMSCODE_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "requestId", ResultConstants.REQUESTID_ISNULL.getCode(), new Object[]{ResultConstants.REQUESTID_ISNULL});
    }

}
