import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hszsd.webpay.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gzhengDu on 2016/7/12.
 */
public class LianLianUtilsTest {
    @Test
    public void testRequestToMap() throws IOException {
        Map<String, String> map = new HashMap<String, String>();
        BufferedReader reader = null;
        StringBuffer buffer = new StringBuffer();
        String stri = "{ " +
                "\r\n\"oid_partner\":\"201103171000000000\", " +
                "\r\n\"dt_order\":\"20130515094013\", " +
                "\r\n\"no_order\":\"2013051500001\", " +
                "\r\n\"oid_paybill\":\"2013051613121201\", " +
                "\r\n\"money_order\":\"210.97\", " +
                "\r\n\"result_pay\":\"SUCCESS\", " +
                "\r\n\"settle_date\":\"20130516\", " +
                "\r\n\"info_order\":\"用户13958069593购买了3桶羽毛球\", " +
                "\r\n\"pay_type\":\"2\", " +
                "\r\n\"bank_code\":\"01020000\", " +
                "\r\n\"sign_type\":\"RSA\", " +
                "\r\n\"sign\":\"ZPZULntRpJwFmGNIVKwjLEF2Tze7bqs60rxQ22CqT5J1UlvGo575QK9z/+p+7E9cOoRoWzqR6xHZ6WVv3dloyGKDR0btvrdqPgUAoeaX/YOWzTh00vwcQ+HBtXE+vPTfAqjCTxiiSJEOY7ATCF1q7iP3sfQxhS0nDUug1LP3OLk=\"" +
                "}";
        try{
            reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(stri.getBytes()), "utf-8"));
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

        if(StringUtils.isNotEmpty(buffer)){
            map = JsonUtil.json2obj(buffer.toString(), Map.class);
        }
        System.out.print(map);
    }
}
