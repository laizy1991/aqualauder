package controllers.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.GoodsType;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;
import common.core.WebController;

import dao.GoodsTypeDao;
import dto.GoodsTypeDto;

public class GoodsTypeCtrl extends WebController {

    public static void list() {
    	List<GoodsType> ddls = GoodsTypeDao.all();
    	Map<Integer, String> names = new HashMap<Integer, String>();
    	for(GoodsType gt : ddls) {
    	    names.put(gt.getId(), gt.getName());
    	}
    	
    	
    	List<GoodsTypeDto> infos = new ArrayList<GoodsTypeDto>();
    	for(GoodsType gt : ddls) {
            String name = names.get(gt.getParentId());
            if(StringUtils.isBlank(name)) {
                name = "无";
            }
            GoodsTypeDto dto = new GoodsTypeDto();
            dto.setCreateTime(gt.getCreateTime());
            dto.setName(gt.getName());
            dto.setId(gt.getId());
            dto.setParentId(gt.getParentId());
            dto.setTypeDesc(gt.getTypeDesc());
            dto.setUpdateTime(gt.getUpdateTime());
            dto.setSortNum(gt.getSortNum());
            dto.setParentName(name);
            infos.add(dto);
        }
    	String goodsTypesJson = new Gson().toJson(names);
        render("/admin/GoodsType/list.html", infos, goodsTypesJson);
    }
}
