import org.junit.Test;

import java.net.URL;

/**
 * Created by gzhengDu on 2016/8/25.
 */
public class ClassLoadTest {
    @Test
    public void testResource(){
        URL url = getClass().getClassLoader().getResource("com/mchange/v2/util/ResourceClosedException.class");
        System.out.println(url.getPath());
    }
}
