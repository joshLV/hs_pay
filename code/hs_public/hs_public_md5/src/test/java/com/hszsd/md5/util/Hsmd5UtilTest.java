package com.hszsd.md5.util;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by gzhengDu on 2016/7/28.
 */
public class Hsmd5UtilTest {

    @Test
    public void generateMD5Sign() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", "b3b20ba08ea548b9a9852db1b583f8f2");
        map.put("mobile", "15021819164");
        map.put("sourceCode", "SH");
        map.put("returnUrl", "https://www.hszsdpay.com:188/receiveTest");
        map.put("noticeUrl", "https://www.hszsdpay.com:188/receiveTest");

        String MD5Sign = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "shop.zhaoshangdai.com");
        System.out.print(MD5Sign);
    }
}