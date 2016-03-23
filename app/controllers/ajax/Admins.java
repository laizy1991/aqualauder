package controllers.ajax;

import dao.AdminDao;
import exception.BusinessException;
import models.Administrator;

import common.core.AjaxController;
import service.AdminService;
import utils.StringUtil;

public class Admins extends AjaxController {

    public static void add(Administrator admin) throws BusinessException {
        if (admin == null
                || StringUtil.isNullOrEmpty(admin.getUsername())
                || StringUtil.isNullOrEmpty(admin.getPassword())) {
            throw new BusinessException("Lack of information");
        }

        if (null != AdminService.getByName(admin.getUsername())) {
            throw new BusinessException("User exist");
        }
        AdminService.add(admin);
        renderSuccessJson();
    }

    public static void delete(Administrator admin) throws Exception {
        if(!StringUtil.isNullOrEmpty(admin.getUsername())) {
            admin.setDeleted(1);
            AdminService.update(admin);
        }
        renderSuccessJson();
    }

    public static void update(Administrator admin) {
        if(admin != null) {
            AdminService.update(admin);
        }
        renderSuccessJson();
    }

}
