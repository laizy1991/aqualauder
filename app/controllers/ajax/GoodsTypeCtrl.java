package controllers.ajax;

import com.google.gson.Gson;

import models.GoodsType;
import common.core.AjaxController;
import dao.GoodsTypeDao;
import exception.BusinessException;

public class GoodsTypeCtrl  extends AjaxController {
    
    public static void add(GoodsType goodsType) throws BusinessException {
        if(goodsType != null) {
            goodsType.setParentId(0);
            goodsType.setCreateTime(System.currentTimeMillis());
            goodsType.setUpdateTime(System.currentTimeMillis());
            GoodsTypeDao.save(goodsType);
        }
        renderSuccessJson();
    }

    public static void delete(GoodsType goodsType) throws Exception {

        renderSuccessJson();
    }

    public static void update(GoodsType goodsType) {
        if(goodsType != null) {
            System.err.println(new Gson().toJson(goodsType));
            goodsType.setUpdateTime(System.currentTimeMillis());
            GoodsTypeDao.save(goodsType);
        }
        renderSuccessJson();
    }


}

