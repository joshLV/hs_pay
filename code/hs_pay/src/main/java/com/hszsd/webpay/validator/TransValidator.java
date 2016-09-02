package com.hszsd.webpay.validator;

import com.hszsd.webpay.common.ResultConstants;
import com.hszsd.webpay.web.form.SelectTradesForm;
import com.hszsd.webpay.web.form.TransForm;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


/**
 * 支付交易校验器
 * Created by suocy on 2016/7/1.
 */
public class TransValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return TransForm.class.equals(aClass)||SelectTradesForm.class.equals(aClass);
    }

    /**
     * 根据参数对象类型触发相应校验方法
     * @param o 分装校验数据的参数对象
     * @param errors 封装错误提示信息
     */
    @Override
    public void validate(Object o, Errors errors) {

        //验证支付密码
        if(o instanceof TransForm){
            validateTransForm(o, errors);
            return ;
        }

        //分页查询交易记录参数
        if(o instanceof SelectTradesForm){
            validateSelectTradesForm(o, errors);
            return ;
        }
    }

    /**
     * 对流水号和支付密码数据进行校验
     * 1. 账单流水号、支付密码的非空校验
     * @param o 流水号和支付密码数据对象
     * @param errors 封装错误提示信息
     */

    public void validateTransForm(Object o, Errors errors) {
        ValidationUtils.rejectIfEmpty(errors, "transId", ResultConstants.TRANSID_ISNULL.getCode(), new Object[]{ResultConstants.TRANSID_ISNULL});
        ValidationUtils.rejectIfEmpty(errors, "payPassword", ResultConstants.PAYPASSWORD_ISNULL.getCode(), new Object[]{ResultConstants.PAYPASSWORD_ISNULL});
       
    }

    /**
     * 对分页查询交易记录参数数据进行校验
     * 1. 当前页、每页条数的校验
     * @param o 分页查询交易记录参数数据对象
     * @param errors 封装错误提示信息
     */

    public void validateSelectTradesForm(Object o, Errors errors) {
        SelectTradesForm selectTradesForm = (SelectTradesForm) o;
        if(null == selectTradesForm){
            errors.rejectValue("selectTradesForm", ResultConstants.OPERATOR_FAIL.getCode(), new Object[]{ResultConstants.OPERATOR_FAIL}, "");
            return;
        }
        if(selectTradesForm.getPageNum()<=0){
            errors.rejectValue("pageNum", ResultConstants.PAGENUM_ISWRONG.getCode(), new Object[]{ResultConstants.PAGENUM_ISWRONG}, "");
        }
        if(selectTradesForm.getPageSize()<=0){
            errors.rejectValue("pageSize", ResultConstants.PAGESIZE_ISWRONG.getCode(), new Object[]{ResultConstants.PAGESIZE_ISWRONG}, "");
        }
    }

}
