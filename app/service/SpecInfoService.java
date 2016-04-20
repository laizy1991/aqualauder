package service;

import dao.SpecInfoDao;
import models.SpecInfo;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class SpecInfoService {
        public static SpecInfo get(long id) {
            return SpecInfoDao.get(id);
        }

        public static void add(SpecInfo specInfo) {
            specInfo.setCreateTime(System.currentTimeMillis());
            specInfo.setUpdateTime(System.currentTimeMillis());
            SpecInfoDao.insert(specInfo);
        }

//        public static void delete(SpecInfo specInfo) {
//            SpecInfoDao.delete(specInfo);
//        }

        public static void update(SpecInfo specInfo) {
            specInfo.setUpdateTime(System.currentTimeMillis());
            SpecInfoDao.update(specInfo);
        }

    }
