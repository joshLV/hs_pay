import com.hszsd.webpay.dao.TradeRecordDao;
import com.hszsd.webpay.web.dto.TradeRecordDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by gzhengDu on 2016/7/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml","classpath:applicationContext.xml","classpath:spring-mybatis.xml","classpath:spring-beetl.xml"})
public class SpringTest{
    @Autowired
    private TradeRecordDao tradeRecordDao;

    @Test
    public void testAutowired(){
        TradeRecordDTO tradeRecordDTO = tradeRecordDao.selectByPrimaryKey("123124123");
    }
}
