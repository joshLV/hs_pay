import com.hszsd.webpay.web.form.TradeForm;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gzhengDu on 2016/7/8.
 */
public class StringTest {
    @Test
    public void objectTest() throws InterruptedException, IOException {
        Map<String, String> map = new HashMap<String, String>();
        map.put("1", "");
        map.put("2", "");
        map.put("3", "");
        Object o = "12312";
        o = new TradeForm();
        System.out.print(o instanceof TradeForm);
        //System.in.read();
    }
}
