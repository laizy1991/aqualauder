package controllers;

import common.core.WebController;
import models.User;

import java.util.List;

public class Users extends WebController {

    public static void list() {
        List<User> users = User.all().fetch();
        render("/User/list.html", users);
    }
    
}
