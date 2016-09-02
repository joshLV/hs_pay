import com.google.code.kaptcha.Producer;
import com.hszsd.webpay.util.CaptchaUtil;
import org.junit.Test;

/**
 * Created by gzhengDu on 2016/6/30.
 */
public class CaptchaTest {
    @Test
    public void testCaptchaUtils(){
        Producer captchaProducer = CaptchaUtil.getInstance().getProducer();
        String code = captchaProducer.createText();
        System.out.print(captchaProducer);
    }

    @Test
    public void testReloadCaptcha(){
        CaptchaUtil.getInstance().toReloadCaptcha();
        StringBuffer basePath = new StringBuffer();
        basePath.append(123).append("://").append(12).append(":").append(80).append(80);
        System.out.print(basePath);
    }
}
