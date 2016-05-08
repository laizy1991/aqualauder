package controllers.front;

import common.annotation.GuestAuthorization;
import common.core.WebController;

public class Helps extends WebController {
    @GuestAuthorization
    public static void explain() {
        render("/Front/help/explain.html");
    }
    @GuestAuthorization
    public static void introduce() {
        render("/Front/help/introduce.html");
    }
    @GuestAuthorization
    public static void link() {
        render("/Front/help/link.html");
    }
    
}
