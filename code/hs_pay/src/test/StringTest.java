import com.hszsd.webpay.po.TradeRecordPO;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import com.hszsd.webpay.web.form.TradeForm;
import junit.framework.Assert;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gzhengDu on 2016/7/8.
 */
public class StringTest {
    @Test
    public void objectTest() throws InterruptedException, IOException {
        Map map = new HashMap<String, String>();
        map.put("1", "");
        map.put("2", "");
        map.put("3", "");
        Object o = "12312";
        o = new TradeForm();
        System.out.print(o instanceof TradeForm);
        //System.in.read();
        String test = (String) map.get("userId");
        Assert.assertTrue(StringUtils.isEmpty(test));

        //List<String> list = Collections.EMPTY_LIST;
        //String te = list.get(0);
        //System.out.print(te);

        TradeForm tradeForm = new TradeForm();
        tradeForm.setTransId("123123");
        System.out.println(tradeForm);
    }

    @Test
    public void testMath(){
        TradeRecordPO tradeRecord = new TradeRecordPO();
        BigDecimal creditBigDecimal = new BigDecimal("0").setScale(0,BigDecimal.ROUND_HALF_UP);
        if(creditBigDecimal.compareTo(BigDecimal.ZERO)< 1){
            throw new RuntimeException("createTradeRecord have an error occurred by : 积分为负值");
        }
        tradeRecord.setCredit(creditBigDecimal);
        System.out.println(tradeRecord);
    }

    @Test
    public void testCalendar(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        System.out.println("Calendar.HOUR_OF_DAY: "+ calendar.get(Calendar.HOUR_OF_DAY)*3600);
        System.out.println("Calendar.MINUTE: "+ calendar.get(calendar.MINUTE)*60);
        System.out.println("Calendar.SECOND: "+ calendar.get(calendar.SECOND));
        System.out.println(System.currentTimeMillis());
        System.out.println(calendar.getTimeInMillis());
        System.out.println(24*60*60-calendar.get(Calendar.HOUR_OF_DAY)*3600-calendar.get(calendar.MINUTE)*60-calendar.get(calendar.SECOND));
    }
}
