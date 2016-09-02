import com.hszsd.core.service.CoreService;
import com.hszsd.entity.Account;
import com.hszsd.webpay.common.GlobalConstants;
import com.hszsd.webpay.condition.TradeRecordCondition;
import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.util.JsonUtil;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by gzhengDu on 2016/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml","classpath:applicationContext.xml","classpath:spring-mybatis.xml","classpath:spring-beetl.xml", "classpath:dubbo-config.xml"})
public class SpringTest{
    @Autowired
    private TradeRecordDao tradeRecordDao;

    @Autowired
    private CoreService coreService;

    @Test
    public void testAutowired(){
        String orderId = "201608021625342673";
        TradeRecordCondition condition = new TradeRecordCondition();
        condition.or()
                .andOrderIdEqualTo(orderId)
                .andTradeStatusEqualTo(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
        List<TradeRecordDTO> tradeRecordDTOs = tradeRecordDao.selectByCondition(condition);
        System.out.print(tradeRecordDTOs);

    }

    @Test
    public void testCoreService(){
        Account account = coreService.selectAccountByUserId("b3b20ba08ea548b9a9852db1b583f8f2");
        System.out.println(JsonUtil.obj2json(account));
    }
}
