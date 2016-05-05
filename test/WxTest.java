import org.junit.*;

import play.Logger;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import service.OrderService;
import utils.WxUtil;

public class WxTest extends UnitTest {

    @Test
    public void testWx() {
		String accessToken = WxUtil.getAccessToken();
		Logger.info("accessToken: %s", accessToken);
    }
    
}