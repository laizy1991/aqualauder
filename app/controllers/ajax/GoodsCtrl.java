package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Goods;
import models.GoodsIcon;
import models.GoodsStock;
import play.data.Upload;
import play.libs.Files;
import play.mvc.results.RenderJson;
import service.GoodsService;
import service.GoodsStockService;
import utils.StringUtil;

import java.io.File;
import java.util.List;
import java.util.UUID;


public class GoodsCtrl extends AjaxController {

    public static void add(Goods goods, List<GoodsStock> goodsStock, List<GoodsIcon> goodsIcon) throws BusinessException {
        if(!StringUtil.isNullOrEmpty(goods.getIdentifier()) && GoodsService.getByIdentifier(goods.getIdentifier()) != null) {
            renderErrorJson("商品款号重复!");
            return;
        }
        GoodsService.add(goods, goodsStock, goodsIcon);
        renderSuccessJson();
    }

    public static void delete(Goods goods) throws Exception {

        renderSuccessJson();
    }

    public static void update(Goods goods, List<GoodsStock> goodsStock, List<GoodsStock> goodsStockToUpdate, List<GoodsStock> goodsStockToDelete) {
        GoodsService.update(goods);
        if (goodsStock != null && goodsStock.size() != 0) {
            for (GoodsStock gs : goodsStock) {
                if(gs != null) {
                    gs.setGoodsId(goods.getId());
                }
            }
        }
        GoodsStockService.add(goodsStock);
        GoodsStockService.update(goodsStockToUpdate);
        GoodsStockService.delete(goodsStockToDelete);
        renderSuccessJson();
    }

    public static void upload(File boundary, Goods goods) {
        List<Upload> files = (List<Upload>) request.args.get("__UPLOADS");
        if (files.size() > 0) {
            Upload upload = files.get(0);
            if (upload.getSize() > 0) {
                File file = upload.asFile();
                String[] temp = file.getName().split("\\.");
                String suffix = "." + temp[temp.length -1];
                String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
                File storeFile = new File("./public/pictures/goods/" + fileName);
                Files.copy(file, storeFile);
                if(goods.getId() != null) {
                    GoodsIcon goodsIcon = new GoodsIcon();
                    goodsIcon.setGoodsId(goods.getId());
                    goodsIcon.setIconUrl(fileName);
                    GoodsService.addGoodsIcon(goodsIcon);
                }
                renderSuccessJson(fileName);
                return;
            }
        }
        renderErrorJson("upload fail");
        return;
    }

    public static void deleteIcon(GoodsIcon goodsIcon) {
        if(GoodsService.deleteIcon(goodsIcon)) {
            renderSuccessJson();
        }
    }

    public static void changeIcon(GoodsIcon goodsIcon, GoodsIcon nxGoodsIcon) {
        String temp = goodsIcon.getIconUrl();
        goodsIcon.setIconUrl(nxGoodsIcon.getIconUrl());
        nxGoodsIcon.setIconUrl(temp);
        if(GoodsService.updateIcon(goodsIcon) && GoodsService.updateIcon(nxGoodsIcon)) {
            renderSuccessJson();
        }
    }

    public static void get(Goods goods) {
        throw new RenderJson(goods);
    }

    public static void getStock(Goods goods) {
        throw new RenderJson(goods.getGoodsStocks());
    }
}
