package com.hszsd.common.util;

import com.hszsd.common.util.Properties.PropertiesUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 信息服务提供平台<br/>
 * <b>正则校验器</b>
 * @author yangwenjian
 * @since 2016-7-18
 * @version V1.0.0
 */
public class RegularValidate {

    private static String PHONE_REGULAR= PropertiesUtil.getProperty("PHONE_REGULAR",null);

    private static String EMAIL_REGULAR= PropertiesUtil.getProperty("EMAIL_REGULAR",null);

    /**
     * 手机号正则验证
     * @param phone  手机号
     * @return true 验证通过<br/>false 验证失败
     */
    public  static boolean isValidatePhoneRegular(String phone){

        Pattern pattern = Pattern.compile(PHONE_REGULAR);
        Matcher m = pattern.matcher(String.valueOf(phone));
        return m.matches();
    }

    /**
     * 邮箱正则验证
     * @param email  邮箱
     * @return true 验证通过<br/>false 验证失败
     */
    public  static boolean isValidateEmailRegular(String email){

        Pattern pattern = Pattern.compile(EMAIL_REGULAR);
        Matcher m = pattern.matcher(String.valueOf(email));
        return m.matches();
    }

}
