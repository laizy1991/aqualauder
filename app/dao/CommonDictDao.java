package dao;

import java.util.List;

import javax.persistence.Query;

import models.CommonDict;

/**
 * 通用字典
 * @author laizy1991@gmail.com
 * @createDate 2016年3月26日
 *
 */
public class CommonDictDao {

    public static List<CommonDict> getByType(int type) {
        /*String sql = "select * from common_dict where dict_type = ? and deleted = 0";
        Query query = CommonDict.em().createQuery(sql);
        query.setParameter(0, type);
        List<CommonDict> dicts = query.getResultList();*/
        return CommonDict.find("type = ? and deleted = 0", type).fetch();
    }
    
    public static CommonDict getById(int id) {
        return CommonDict.findById(id);
    }

    public static void update(CommonDict dict) {
        dict.save();
    }

    public static void insert(CommonDict dict) {
        dict.save();
    }
}
