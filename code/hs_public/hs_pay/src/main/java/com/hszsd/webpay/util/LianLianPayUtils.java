package com.hszsd.webpay.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hszsd.webpay.config.LianLianConfig;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 连连支付工具类单例
 * Created by gzhengDu on 2016/7/11.
 */
public class LianLianPayUtils {
    static LianLianPayUtils instance = null;

    /**
     * 生成连连支付工具类单例对象并进行返回
     * @return LianLianPayUtils 连连支付工具类
     */
    public static LianLianPayUtils getInstance(){
        if(instance == null){
            instance = new LianLianPayUtils();
        }
        return instance;
    }

    /**
     * 根据加密类型及秘钥为数据进行加密
     *
     * @param map      待加密数据
     * @param signType 加密类型 RSA或MD5
     * @param key      秘钥
     * @return String 加密后字符串
     */
    public String generateLianLianSign(Map map, String signType, String key) {
        if (map == null || map.isEmpty()) {
            return "";
        }

        //生成待签名数据
        String signData = generateSignData(map);
        //根据签名类型对待签名数据进行加密
        if (LianLianConfig.SIGN_TYPE_MD5.equals(signType)) {
            return generateSignMD5(signData, key);
        }
        if (LianLianConfig.SIGN_TYPE_RSA.equals(signType)) {
            return generateSignRSA(signData, key);
        }

        return "";
    }

    /**
     * 将请求数据中的数据转换成待签名字符串
     * 数据中所有元素以"key=value"方式拼接，并使用"&"作为元素之间的连接符(元素顺序按首字母升序排序, 值为空则不参与签名)
     *
     * @param map 请求数据
     * @return String 待签名数据
     */
    public String generateSignData(Map map) {
        StringBuffer buffer = new StringBuffer();

        List<String> keys = new ArrayList<String>(map.keySet());
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);

        for (String key : keys) {
            if ("sign".equals(key) || StringUtils.isEmpty((String) map.get(key))) {
                continue;
            }
            if (!StringUtils.isEmpty(buffer)) {
                buffer.append("&");
            }
            buffer.append(key).append("=").append(map.get(key));
        }

        return buffer.toString();
    }

    /**
     * 对待签名数据进行MD5加密
     *
     * @param signData 待签名数据
     * @param key      秘钥
     * @return String 签名数据
     */
    public String generateSignMD5(String signData, String key) {
        if (StringUtils.isEmpty(signData)) {
            return "";
        }
        signData = StringUtils.join(signData, "&key=", key);

        try {
            return Md5Algorithm.getInstance().md5Digest(signData.getBytes("utf-8"));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 对待签名数据进行RSA加密
     * @param signData 待签名数据
     * @param key 私钥
     * @return String 签名数据
     */
    public String generateSignRSA(String signData, String key) {
        try{
            return TraderRSAUtil.sign(key, signData);
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 读取request中JSON数据流的内容并将其转成map
     * @param request 请求对象
     * @return Map 封装返回数据的map对象
     */
    public Map<String, String> requestToMap(HttpServletRequest request){
        Map<String, String> map = new HashMap<String, String>();
        //读取request中的数据流
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        try{
            reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
            for(String str= reader.readLine();str!=null;str=reader.readLine()){
                buffer.append(str);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(reader != null){
                try {
                    reader.close();
                } catch (IOException e){

                }
            }
        }

        //将其转为map对象并返回
        if(StringUtils.isNotEmpty(buffer)){
            try {
                map = JSON.parseObject(buffer.toString(), new TypeReference<Map<String, String>>() {});
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return map;
    }

}
