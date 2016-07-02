package controllers.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

import models.GoodsType;
import common.core.WebController;
import dao.GoodsTypeDao;

public class GoodsTypeCtrl extends WebController {

    public static void list() {
    	List<GoodsType> infos = GoodsTypeDao.all();
    	Map<Integer, String> names = new HashMap<Integer, String>();
    	for(GoodsType gt : infos) {
    	    names.put(gt.getId(), gt.getName());
    	}
    	for(GoodsType gt : infos) {
            String name = names.get(gt.getParentId());
            if(StringUtils.isBlank(name)) {
                name = "无";
            }
            gt.setParentName(name);
        }
    	String goodsTypesJson = new Gson().toJson(names);
        render("/admin/GoodsType/list.html", infos, goodsTypesJson);
    }
}
