package controllers.ajax;

import dto.SessionInfo;
import exception.BusinessException;
import models.Administrator;

import common.core.AjaxController;
import play.mvc.Scope.Session;
import service.AdminService;
import utils.StringUtil;

public class AdministratorCtrl extends AjaxController {

    public static void add(Administrator administrator) throws BusinessException {
        if (administrator == null
                || StringUtil.isNullOrEmpty(administrator.getUsername())
                || StringUtil.isNullOrEmpty(administrator.getPassword())) {
            throw new BusinessException("Lack of information");
        }

        if (null != AdminService.getByName(administrator.getUsername())) {
            throw new BusinessException("User exist");
        }
        AdminService.add(administrator);
        renderSuccessJson();
    }

    public static void delete(Administrator administrator) throws Exception {
        if(!StringUtil.isNullOrEmpty(administrator.getUsername())) {
            AdminService.delete(administrator);
        }
        renderSuccessJson();
    }

    public static void update(Administrator administrator) throws BusinessException {
        if (administrator == null
                || StringUtil.isNullOrEmpty(administrator.getPassword())) {
            throw new BusinessException("Lack of information");
        }

        Session session = Session.current.get();

        if (session != null) {
            SessionInfo sessionInfo = AdminService.getSessionInfo(session.get("sid"));
            if (sessionInfo != null && sessionInfo.getAdmin() != null && sessionInfo.getAdmin().getId() == administrator.getId()) {
                AdminService.update(administrator);
                renderSuccessJson();
            }
        }
        throw new BusinessException("no permission");
    }

}
