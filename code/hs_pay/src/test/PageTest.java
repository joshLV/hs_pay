import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
public class PageTest{
    @Autowired
    private TradeRecordDao tradeRecordDao;

    @Test
    public void testPage(){
        TradeRecordCondition condition = new TradeRecordCondition();
        condition.or()
                .andTradeStatusEqualTo(GlobalConstants.TRADE_RECORD.TRADE_STATUS_2);
        condition.setOrderByClause("create_date desc");
        PageHelper.startPage(1,20);
        List<TradeRecordDTO> tradeRecordDTOs = tradeRecordDao.selectByCondition(condition);
        PageInfo pageInfo = new PageInfo(tradeRecordDTOs);
        System.out.print(pageInfo);

    }

}
