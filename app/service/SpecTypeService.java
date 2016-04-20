package service;

import dao.SpecTypeDao;
import models.SpecType;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class SpecTypeService {
    public static SpecType get(long id) {
        return SpecTypeDao.get(id);
    }

    public static void add(SpecType specType) {
        specType.setCreateTime(System.currentTimeMillis());
        specType.setUpdateTime(System.currentTimeMillis());
        SpecTypeDao.insert(specType);
    }

//        public static void delete(SpecType specType) {
//            SpecTypeDao.delete(specType);
//        }

    public static void update(SpecType specType) {
        specType.setUpdateTime(System.currentTimeMillis());
        SpecTypeDao.update(specType);
    }

}
