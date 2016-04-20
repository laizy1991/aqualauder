package dao;

import models.SpecInfo;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class SpecInfoDao {
    public static SpecInfo get(long id) {
        return SpecInfo.findById(id);
    }

    public static void update(SpecInfo specInfo) {
        specInfo.save();
    }

    public static void insert(SpecInfo specInfo) {
        specInfo.save();
    }
}