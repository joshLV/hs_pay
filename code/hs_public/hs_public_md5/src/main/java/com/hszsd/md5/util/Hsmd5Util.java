package com.hszsd.md5.util;

import com.hszsd.md5.beartool.MD5;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * 合石招商贷MD5加密工具类
 * Created by gzhengDu on 2016/7/14.
 */
public class Hsmd5Util {

    static Hsmd5Util instance;

    private Properties properties = new Properties();

    public Hsmd5Util() {
        initProperties();
    }

    public static Hsmd5Util getInstance() {
        if(instance == null){
            instance = new Hsmd5Util();
        }
        return instance;
    }

    /**
     * 加载md5加密顺序配置文件
     */
    public void initProperties(){
        InputStream in = getClass().getResourceAsStream("/com/hszsd/md5/config/md5.properties");
        if(null == in) {
            return;
        }
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成MD5加密字符串
     * @param map 加密数据
     * @param type 加密顺序
     * @param MD5Key MD5秘钥
     * @return String md5字符串
     */
    public String generateMD5Sign(Map map, String type, String MD5Key){
        if(map.isEmpty() || StringUtils.isEmpty(type) || StringUtils.isEmpty(MD5Key)){
            return "";
        }
        String sequence = properties.getProperty(type);
        if(StringUtils.isEmpty(sequence)){
            return "";
        }
        StringBuffer signData = new StringBuffer();
        for(String str: sequence.split("&")){
            signData.append(map.get(str)==null?"":map.get(str)).append("&");
        }

        signData.append(MD5Key);
        return new MD5().generateMD5Sign(signData.toString());
    }

    /**
     * 验证MD5是否匹配
     * @param map 加密数据
     * @param type 加密顺序
     * @param MD5Key md5秘钥
     * @param md5sign md5加密字符串
     * @return boolean 是否验证通过
     */
    public boolean checkMD5Sign(Map map, String type, String MD5Key, String md5sign){
        String verify = generateMD5Sign(map, type, MD5Key);
        return md5sign.equals(verify);
    }
}
