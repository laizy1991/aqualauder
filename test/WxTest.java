import org.junit.*;

import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import utils.WxUtil;

public class WxTest extends UnitTest {

    @Test
    public void testWx() {
    	String str = String.format("Hi, %s,%s.%s", "a","b","c");
    	System.out.println(str);
    	String accessToken = WxUtil.getAccessToken();
    	System.out.println(accessToken);
    }
    
}