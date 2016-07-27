package com.hszsd.common.util.captcha;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 验证码工具类单例
 * 依赖于Kaptcha工具，该类主要用于加载Kaptcha的相关配置（captcha.properties）并初始化验证码生成器
 * Created by gzhengDu on 2016/6/30.
 */
public class CaptchaUtil {
    static CaptchaUtil instance = null;
    private DefaultKaptcha captchaProcuder = new DefaultKaptcha();
    private Config captchaConfig = null;

    public CaptchaUtil() {
        initCaptcha();
    }

    /**
     * 生成验证码工具类单例对象并进行返回
     * @return CaptchaUtil 验证码工具类
     */
    public static CaptchaUtil getInstance(){
        if (null == instance){
            instance = new CaptchaUtil();
        }
        return instance;
    }

    /**
     * 加载验证码相关配置文件并进行初始化
     */
    private void initCaptcha(){
        Properties properties = new Properties();
        InputStream in = getClass().getResourceAsStream("/captcha.properties");
        if(null == in) {
            return;
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        captchaConfig = new Config(properties);
        captchaProcuder.setConfig(captchaConfig);
    }

    /**
     * 重新加载验证码的配置文件
     */
    public void toReloadCaptcha(){
        initCaptcha();
    }

    /**
     * 返回
     * @return Producer 验证码生成器
     */
    public Producer getProducer(){
        return captchaProcuder;
    }
}
