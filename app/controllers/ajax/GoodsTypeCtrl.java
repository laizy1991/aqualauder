package controllers.ajax;

import java.util.List;

import models.GoodsType;

import common.core.AjaxController;

import dao.GoodsTypeDao;
import exception.BusinessException;

public class GoodsTypeCtrl  extends AjaxController {
    
    public static void add(GoodsType goodsType) throws BusinessException {
        if(goodsType != null) {
            goodsType.setParentId(goodsType.getParentId());
            goodsType.setCreateTime(System.currentTimeMillis());
            goodsType.setUpdateTime(System.currentTimeMillis());
            goodsType.setDeleted(0);
            GoodsTypeDao.save(goodsType);
        }
        renderSuccessJson();
    }

    public static void delete(Integer id) throws Exception {
        
        List<Integer> ids = GoodsTypeDao.getAllSubTypeAndSelf(id);

        for(Integer item : ids) {
            GoodsType type = GoodsTypeDao.get(item);
            if(type == null) {
                continue;
            }
            type.setUpdateTime(System.currentTimeMillis());
            type.setDeleted(1);
            GoodsTypeDao.save(type);
        }
        
        renderSuccessJson();
    }

    public static void update(GoodsType goodsType) {
        if(goodsType != null) {
            goodsType.setUpdateTime(System.currentTimeMillis());
            GoodsTypeDao.save(goodsType);
        }
        renderSuccessJson();
    }


}

