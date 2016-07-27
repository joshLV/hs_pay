package com.hszsd.webpay.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by gzhengDu on 2016/7/19.
 */
public class MerchantUtil {
    static MerchantUtil instance;
    private static final String MD5SIGN_SUFFIX = "_KEY";
    private static final String MERCHANT_SUFFIX = "_NAME";

    private Map<String, String> md5SignMap = new HashMap<String, String>();
    private Map<String, String> merchantMap = new HashMap<String, String>();

    public MerchantUtil() {
        initProperties();
    }

    public static MerchantUtil getInstance() {
        if(instance == null){
            instance = new MerchantUtil();
        }
        return instance;
    }

    /**
     * 加载MD5key配置文件
     */
    public void initProperties(){
        InputStream in = getClass().getResourceAsStream("config/merchant.properties");
        if(null == in) {
            return;
        }
        try {
            Properties properties = new Properties();
            properties.load(in);
            for(Object key:properties.keySet()){
                if(String.valueOf(key).indexOf(MD5SIGN_SUFFIX)!= -1){
                    md5SignMap.put(key.toString(), properties.get(key).toString());
                }else if(String.valueOf(key).indexOf(MERCHANT_SUFFIX)!= -1){
                    merchantMap.put(key.toString(), properties.get(key).toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取商户名称
     * @param sourceCode 平台来源
     * @return
     */
    public String getMerchantName(String sourceCode){
        return merchantMap.get(sourceCode);
    }

    /**
     * 获取商户MD5的key
     * @param sourceCode 平台来源
     * @return
     */
    public String getMerchantKey(String sourceCode){
        return md5SignMap.get(sourceCode);
    }
}
