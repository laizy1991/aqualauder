package controllers.ajax;

import dao.AdminDao;
import models.Administrator;

import common.core.AjaxController;

public class Admins extends AjaxController {
    public static void add(Administrator admin) {
        if (admin != null) {
            admin.setCreateTime(System.currentTimeMillis());
            admin.setModifyTime(System.currentTimeMillis());
            AdminDao.insert(admin);
        }
        renderSuccessJson();
    }

    public static void delete(Administrator admin) {
        if(admin != null) {
            AdminDao.delete(admin.getUsername());

        }
        renderSuccessJson();
    }

    public static void update(Administrator admin) {
        if(admin != null) {
            admin.setModifyTime(System.currentTimeMillis());
            AdminDao.update(admin);
        }
        renderSuccessJson();
    }
    
}
