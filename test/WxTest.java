import org.junit.*;

import play.Logger;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import service.OrderService;
import utils.WxAccessTokenUtil;

public class WxTest extends UnitTest {

    @Test
    public void testWx() {
		String accessToken = WxAccessTokenUtil.getAccessToken();
		Logger.info("accessToken: %s", accessToken);
    }
    
}