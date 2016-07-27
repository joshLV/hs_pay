import com.hszsd.webpay.po.TradeRecordPO;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gzhengDu on 2016/7/26.
 */
public class BeanUtilsTest {
    @Test
    public void testCopyProperties(){
        Map<String, String> map = new HashMap<String, String>();
        map.put("transId", "12312312");
        map.put("orderId", "12312");
        map.put("test", "13243");
        TradeRecordPO tradeRecordPO = new TradeRecordPO();
        try {
            BeanUtils.copyProperties(tradeRecordPO, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.print(tradeRecordPO);
    }
}
