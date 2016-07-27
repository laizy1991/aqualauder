package dao;

import java.util.ArrayList;
import java.util.List;

import models.GoodsType;

import org.apache.commons.collections.CollectionUtils;

public class GoodsTypeDao {
    public static GoodsType get(int id) {
        return GoodsType.findById(id);
    }

    public static void save(GoodsType gt) {
        gt.save();
    }

    public static List<GoodsType> all() {
        return GoodsType.find("deleted = 0 order by sort_num asc").fetch();
    }
    
    public static List<Integer> getAllSubTypeAndSelf(int id) {
        List<Integer> ids = new ArrayList<Integer>();
        ids.add(id);
        List<Integer> needSearch = new ArrayList<Integer>();
        needSearch.add(id);
        while(!needSearch.isEmpty()) {
            Integer search = needSearch.get(0);
            needSearch.remove(0);
            List<GoodsType> types = GoodsType.find("parentId = ? and deleted = 0", search).fetch();
            if(CollectionUtils.isNotEmpty(types)) {
                for(GoodsType gt : types) {
                    if(ids.contains(gt.getId())) {
                        continue;
                    }
                    ids.add(gt.getId());
                    needSearch.add(gt.getId());
                }
                
            }
        }
        
        return ids;
    }
}