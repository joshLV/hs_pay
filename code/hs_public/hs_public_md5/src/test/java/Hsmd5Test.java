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
        map.put("test", "test");
        String md5sign = Hsmd5Util.getInstance().generateMD5Sign(map, "RECHARGE_REQ", "123123123");
        System.out.print(md5sign.split("&").length);
    }
}
