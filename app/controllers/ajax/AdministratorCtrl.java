package controllers.ajax;

import common.core.AjaxController;
import dao.RolePrivilegeDao;
import dto.SessionInfo;
import exception.BusinessException;
import models.Administrator;
import models.RolePrivilege;
import org.apache.commons.collections.CollectionUtils;
import play.mvc.Scope.Session;
import service.AdminService;
import utils.StringUtil;

import java.util.List;

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
            if (sessionInfo != null && sessionInfo.getAdmin() != null && (sessionInfo.getAdmin().getId() == administrator.getId() || sessionInfo.getAdmin().getAdminType().intValue() == 0)) {
                AdminService.update(administrator);
                sessionInfo.setAdmin(administrator);
                renderSuccessJson();
            }
        }
        throw new BusinessException("no permission");
    }
    
    public static void updateAdminPrivilege(List<Integer> ids) {
        
        RolePrivilegeDao.delByRole(1);
        if(CollectionUtils.isNotEmpty(ids)) {
            for(Integer id : ids) {
                RolePrivilege rp = new RolePrivilege();
                rp.setPrivilegeId(id);
                rp.setRoleId(1);
                RolePrivilegeDao.insert(rp);
            }
        }
        
        redirect("admin.AdministratorCtrl.adminPrivilege");
    }

}
