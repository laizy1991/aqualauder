package controllers.ajax;

import dto.SessionInfo;
import exception.BusinessException;
import models.Administrator;

import common.core.AjaxController;
import play.mvc.Scope.Session;
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
            AdminService.delete(admin);
        }
        renderSuccessJson();
    }

    public static void update(Administrator admin) throws BusinessException {
        if (admin == null
                || StringUtil.isNullOrEmpty(admin.getPassword())) {
            throw new BusinessException("Lack of information");
        }

        Session session = Session.current.get();

        if (session != null) {
            SessionInfo sessionInfo = AdminService.getSessionInfo(session.get("sid"));
            if (sessionInfo != null && sessionInfo.getAdmin() != null && sessionInfo.getAdmin().getId() == admin.getId()) {
                AdminService.update(admin);
                renderSuccessJson();
            }
        }
        throw new BusinessException("no permission");
    }

}
