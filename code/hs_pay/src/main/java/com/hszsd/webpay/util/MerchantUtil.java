package com.hszsd.webpay.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 读取商户配置信息工具类
 * Created by gzhengDu on 2016/7/19.
 */
public class MerchantUtil {
    static MerchantUtil instance;
    private static final String MD5SIGN_SUFFIX = "_KEY";
    private static final String MERCHANT_SUFFIX = "_NAME";
    private static final String ISDUBBO_SUFFIX = "_ISENABLE";
    private static final String HOME_SUFFIX = "_HOME";

    private Map<String, String> md5SignMap = new HashMap<String, String>();
    private Map<String, String> merchantMap = new HashMap<String, String>();
    private Map<String, Boolean> isDubboMap = new HashMap<String, Boolean>();
    private Map<String, String> merchantHomeMap = new HashMap<String, String>();

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
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream("/config/merchant.properties");
            if(null == in) {
                return;
            }
            Properties properties = new Properties();
            properties.load(in);
            String key;
            String value;
            for(Map.Entry entry:properties.entrySet()){
                key = String.valueOf(entry.getKey());
                value = String.valueOf(entry.getValue());
                if(key.indexOf(MD5SIGN_SUFFIX)!= -1){
                    md5SignMap.put(key.replace(MD5SIGN_SUFFIX,""), new String(value.getBytes("ISO-8859-1"),"gbk"));
                }else if(key.indexOf(MERCHANT_SUFFIX)!= -1){
                    merchantMap.put(key.replace(MERCHANT_SUFFIX,""), new String(value.getBytes("ISO-8859-1"),"gbk"));
                }else if(key.indexOf(ISDUBBO_SUFFIX)!=-1){
                    isDubboMap.put(key.replace(ISDUBBO_SUFFIX,""), Boolean.valueOf(value));
                }else if(key.indexOf(MERCHANT_SUFFIX)!=-1){
                    merchantHomeMap.put(key.replace(MERCHANT_SUFFIX,""), new String(value.getBytes("ISO-8859-1"),"gbk"));
                }else{}
            }
        } catch (IOException e) {
        } finally {
            if(in!=null)
                try {
                    in.close();
                } catch (IOException e) {}
        }
    }

    /**
     * 获取商户名称
     * @param sourceCode 平台来源（商户标识）
     * @return
     */
    public String getMerchantName(String sourceCode){
        return merchantMap.get(sourceCode);
    }

    /**
     * 获取商户MD5的key
     * @param sourceCode 平台来源（商户标识）
     * @return
     */
    public String getMerchantKey(String sourceCode){
        return md5SignMap.get(sourceCode);
    }

    /**
     * 判断商户是否启用dubbo服务操作资金
     * @param sourceCode 平台来源（商户标识）
     * @return
     */
    public Boolean getIsDubbo(String sourceCode){
        return isDubboMap.get(sourceCode);
    }

    /**
     * 获取商户主页地址
     * @param sourceCode 平台来源（商户标识）
     * @return
     */
    public String getMerchantHome(String sourceCode){
        return merchantHomeMap.get(sourceCode);
    }
}
