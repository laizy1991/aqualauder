package dao;

import java.util.List;

import models.RolePrivilege;


public class RolePrivilegeDao {
    
    public static List<RolePrivilege> getByRole(int role) {
        return RolePrivilege.find("role_id = ?", role).fetch();
    }
    
    public static void delByRole(int role) {
        RolePrivilege.delete("role_id = ?", role);
    }
    
    public static void insert(RolePrivilege rp) {
        if(rp != null)
            rp.save();
    }
}