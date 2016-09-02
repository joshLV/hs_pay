package com.hszsd.webpay.validator;

import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.web.form.BindCardConfirmForm;
import com.hszsd.webpay.web.form.BindCardForm;
import com.hszsd.webpay.web.form.BindCardPayForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * 易宝支付数据校验器
 * Created by suocy on 2016/8/11.
 */
public class YeePayValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return BindCardForm.class.equals(aClass) || BindCardConfirmForm.class.equals(aClass) || BindCardPayForm.class.equals(aClass);
    }

    /**
     * 根据参数对象类型触发相应校验方法
     * @param o 分装校验数据的参数对象
     * @param errors 封装错误提示信息
     */
    @Override
    public void validate(Object o, Errors errors) {

        //绑卡
        if(o instanceof BindCardForm){
            validateBindCardForm(o, errors);
            return ;
        }

        //确认绑卡
        if(o instanceof BindCardConfirmForm){
            validateBindCardConfirmForm(o, errors);
            return ;
        }

        //快捷支付
        if(o instanceof BindCardPayForm){
            validateBindCardPayForm(o, errors);
            return ;
        }

    }

    /**
     * 对绑卡表单对象数据进行校验
     * @param o 绑卡表单对象
     * @param errors 封装错误提示信息
     */
    public void validateBindCardForm(Object o, Errors errors) {
        //ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "userId", ResultConstants.USERID_ISNULL.getCode(), new Object[]{ResultConstants.USERID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "cardNo", ResultConstants.CARDNO_ISNULL.getCode(), new Object[]{ResultConstants.CARDNO_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "phone", ResultConstants.REGISTERTEL_ISNULL.getCode(), new Object[]{ResultConstants.REGISTERTEL_ISNULL});
    }

    /**
     * 对确认绑卡表单对象数据进行校验
     * @param o 确认绑卡表单对象
     * @param errors 封装错误提示信息
     */
    public void validateBindCardConfirmForm(Object o, Errors errors) {
        //ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "userId", ResultConstants.USERID_ISNULL.getCode(), new Object[]{ResultConstants.USERID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "smsCode", ResultConstants.SMSCODE_ISNULL.getCode(), new Object[]{ResultConstants.SMSCODE_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "requestId", ResultConstants.REQUESTID_ISNULL.getCode(), new Object[]{ResultConstants.REQUESTID_ISNULL});
    }

    /**
     * 对求情快捷卡支付表单对象数据进行校验
     * @param o 求情快捷卡支付表单对象
     * @param errors 封装错误提示信息
     */
    public void validateBindCardPayForm(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "userId", ResultConstants.USERID_ISNULL.getCode(), new Object[]{ResultConstants.USERID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "id", ResultConstants.REQUESTID_ISNULL.getCode(), new Object[]{ResultConstants.REQUESTID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "cardTop", ResultConstants.CARDTOP_ISNULL.getCode(), new Object[]{ResultConstants.CARDTOP_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "cardLast", ResultConstants.CARDLAST_ISNULL.getCode(), new Object[]{ResultConstants.CARDLAST_ISNULL});
    }


}
