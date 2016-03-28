package dao;

import java.util.List;

import models.DistributorSuperior;

/**
 * 用户上线表
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class DistributorSuperiorDao {
    public static DistributorSuperior get(int userId) {
        DistributorSuperior ds = DistributorSuperior.findById(userId);
        return ds;
    }
    
    public static List<DistributorSuperior> getBySuperiors(List<Integer> superiors) {
        return DistributorSuperior.find("superior in ?", superiors).fetch();
    }
}
