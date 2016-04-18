package service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AdminDao;
import models.Administrator;
import models.CommonDict;
import play.Logger;
import utils.EncryptionUtil;
import utils.MapUtil;
import common.constants.CommonDictType;
import dao.CommonDictDao;

public class CommonDictService {
    //字典值比较少变动，保存在内存中
    private static Map<Integer, Map<String, CommonDict>> commonDictMap = new HashMap<Integer, Map<String, CommonDict>>();
    
    public static Map<String, CommonDict> getByType(CommonDictType type) {
        
        if(type == null) {
            return Collections.EMPTY_MAP;
        }
        
        if(commonDictMap.containsKey(type.getCode())) {
            return commonDictMap.get(type.getCode());
        }
        
        List<CommonDict> dicts = CommonDictDao.getByType(type.getCode());
        Map<String, CommonDict> map = MapUtil.wrapToMap(dicts, "dictKey", new String());
        commonDictMap.put(type.getCode(), map);
        return map;
    }
    
    public static String getValue(CommonDictType type, String key) {
        Map<String, CommonDict> map = getByType(type);
        CommonDict dict = map.get(key);
        if(dict == null) {
            return null;
        }
        return dict.getValue();
    }
    
    public static boolean updateValue(int id, String value) {
        CommonDict dict = CommonDictDao.getById(id);
        if(dict == null) {
            Logger.error("dict not found, id:%s", id);
            return false;
        }
        dict.setUpdateTime(System.currentTimeMillis());
        dict.setValue(value);
        CommonDictDao.update(dict);
        commonDictMap.remove(dict.getType());
        return true;
    }


    public static void add(CommonDict dict) {
        dict.setCreateTime(System.currentTimeMillis());
        dict.setUpdateTime(System.currentTimeMillis());
        commonDictMap.remove(dict.getType());
        CommonDictDao.insert(dict);
    }

    public static void delete(CommonDict dict) {
        dict.setDeleted(1);
        dict.setUpdateTime(System.currentTimeMillis());
        commonDictMap.remove(dict.getType());
        CommonDictDao.insert(dict);
    }

    public static void update(CommonDict dict) {
        dict.setUpdateTime(System.currentTimeMillis());
        commonDictMap.remove(dict.getType());
        CommonDictDao.update(dict);
    }
    
    
    
}
