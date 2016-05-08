package controllers.front;

import common.annotation.GuestAuthorization;
import common.core.WebController;

public class Users extends WebController {
    @GuestAuthorization
    public static void orders() {
        render("/Front/user/orders.html");
    }
    @GuestAuthorization
    public static void qrcode() {
        render("/Front/user/qrcode.html");
    }
    
}
