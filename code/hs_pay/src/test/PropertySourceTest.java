import com.hszsd.webpay.service.RechargeBankService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Properties;

/**
 * Created by gzhengDu on 2016/8/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml","classpath:applicationContext.xml","classpath:spring-mybatis.xml","classpath:spring-beetl.xml", "classpath:dubbo-config.xml"})
public class PropertySourceTest {

    @Autowired
    private Environment env;

    @Autowired
    private RechargeBankService rechargeBankService;
/*
    @Value("#{onlineBankProp}")
    private Properties properties;*/

    @Test
    public void testEnv(){
        System.out.println(env.getProperty("ICBC"));
        //System.out.println(properties);
    }

    @Test
    public void testSpringProperties(){
        rechargeBankService.queryRechargeBank();
    }

}
