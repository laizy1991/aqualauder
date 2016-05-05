package dao;

import java.util.List;

import javax.persistence.Query;

import models.DistributorSuperior;
import play.db.jpa.Model;

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
      String ids = "";
      String split = "";
      for(Integer id : superiors) {
          ids = ids + split + id;
          split = ",";
      }
      String sql = "SELECT * FROM distributor_superior where superior in (%s);";  
      Query query = Model.em().createNativeQuery(String.format(sql, ids),DistributorSuperior.class);  
      return query.getResultList();  
    }
    
    public static boolean create(int userId, int superior) {
        DistributorSuperior ds = new DistributorSuperior();
        ds.setSuperior(superior);
        ds.setUserId(userId);
        ds.setCreateTime(System.currentTimeMillis());
        return ds.create();
    }
}
