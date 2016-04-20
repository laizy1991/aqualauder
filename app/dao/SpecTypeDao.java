package dao;

import models.SpecType;

/**
 * @author nemo
 * @date 2016.04.19
 */
public class SpecTypeDao {
    public static SpecType get(long id) {
        return SpecType.findById(id);
    }

    public static void update(SpecType specType) {
        specType.save();
    }

    public static void insert(SpecType specType) {
        specType.save();
    }
}