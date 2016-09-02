import com.hszsd.md5.beartool.MD5;
import com.hszsd.md5.util.Hsmd5Util;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gzhengDu on 2016/7/14.
 */
public class Hsmd5Test {
    @Test
    public void testGenerateMD5Sign(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("transId", "123456");
        map.put("money", "123");
        map.put("tradeStatus", "2");
        map.put("resCode", "0000");
        map.put("resMsg", "处理成功");
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_RES", "shop.zhaoshangdai.com");
        String md = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_RES", "shop.zhaoshangdai.com");
        System.out.print(md5sign+" "+md);
    }

    @Test
    public void testGenerateRequestMD5Sign(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", "b3b20ba08ea548b9a9852db1b583f8f2");
        map.put("mobile", "15021819164");
        map.put("sourceCode", "H5");
        map.put("returnUrl", "https://pay.zhaoshangdai.com/testFront.html");
        map.put("noticeUrl", "https://pay.zhaoshangdai.com/testBack.html");
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        String md = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        System.out.print(md5sign+" "+md);
    }

    @Test
    public void testSCYGenerateRequestMD5Sign(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", "a1bf6f042dce4a8aa4ba1f71b7c96577");
            map.put("mobile", "13984872453");
        map.put("sourceCode", "H5");
        map.put("returnUrl", "https://pay.zhaoshangdai.com/pay/testFront.html");
        map.put("noticeUrl", "https://pay.zhaoshangdai.com/pay/testBack.html");
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        String md = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        System.out.print(md5sign+" "+md);
    }
    @Test
    public void testJSKGenerateRequestMD5Sign(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", "28649");
        map.put("mobile", "18100856511");
        map.put("sourceCode", "H5");
        map.put("returnUrl", "https://222.85.161.56:188/testFront.html");
        map.put("noticeUrl", "https://222.85.161.56:188/testBack.html");
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        String md = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        System.out.print(md5sign+" "+md);
    }

    @Test
    public void testSCYBindGenerateRequestMD5Sign(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("userId", "a1bf6f042dce4a8aa4ba1f71b7c96577");
        map.put("mobile", "13984872453");
        map.put("sourceCode", "H5");
        map.put("returnUrl", "https://pay.zhaoshangdai.com/pay/testFront.html");
        map.put("noticeUrl", "https://pay.zhaoshangdai.com/pay/testBack.html");
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        String md = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "www.zhaoshangdai.com");
        System.out.print(md5sign+" "+md);
    }

    @Test
    public void testMD5Sign(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("v_md5str", "B0F5B7225CC39E24E1497B90935A4ED0");
        map.put("v_moneytype", "CNY");
        map.put("v_oid", "20160810183054214350");
        map.put("v_pstring", "%E6%94%AF%E4%BB%98%E6%88%90%E5%8A%9F");
        map.put("v_pstatus", "20");
        map.put("v_pmode", "CMB");
        map.put("v_amount", "0.01");
        map.put("remark2", "%5B%3A%3Dhttps%3A%2F%2F222.85.161.56%3A188%2Fpayment%2FonLineBack.html%5D");
        map.put("MD5Key", "gzhszsd520");

        String md5sign = new MD5().generateMD5Sign("20160810183054214350200.01CNYgzhszsd520");
        //String md = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "shop.zhaoshangdai.com");
        System.out.print(md5sign);
    }
}
