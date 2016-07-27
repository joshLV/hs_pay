import com.hszsd.common.util.math.MathUtil;
import org.junit.Test;

/**
 * Created by gzhengDu on 2016/7/5.
 */
public class MathTest {
    @Test
    public void testMultiply(){
        double v1 = 1.12;
        double v2 = 1;
        double test = MathUtil.multiply(v1, v2);
        System.out.println(test);
    }
}
