import org.junit.*;

import play.Logger;
import play.test.*;
import play.mvc.*;
import play.mvc.Http.*;
import service.OrderService;
import utils.WxUtil;

public class WxTest extends UnitTest {

//    @Test
    public void testWx() {
		String accessToken = WxUtil.getAccessToken();
		Logger.info("accessToken: %s", accessToken);
		Logger.info("appId: %s", "wx744f3da183243cad");
		Logger.info("appSecrect: %s", "9391c34828a8cd46f3375a5d4363cd19");
    }
    
    @Test
    public void genOrder() {
    	Logger.info("OrderId: %s", OrderService.genOrderId());
    }
}