package controllers.ajax;

import dao.AdminDao;
import models.Administrator;

import common.core.AjaxController;
import utils.EncryptionUtil;

public class Admins extends AjaxController {

    //@TODO 加密盐从配置中获取
    //加密盐
    private static final String SLAT = "lllaqenk";

    public static void add(Administrator admin) {
        if (admin != null) {
            admin.setCreateTime(System.currentTimeMillis());
            admin.setModifyTime(System.currentTimeMillis());
            admin.setPassword(EncryptionUtil.md5(admin.getPassword() + SLAT));
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
