package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Goods;
import models.GoodsIcon;
import models.GoodsStock;
import play.data.Upload;
import play.libs.Files;
import service.GoodsService;

import java.io.File;
import java.util.List;
import java.util.UUID;


public class GoodsCtrl extends AjaxController {

    public static void add(Goods goods, List<GoodsStock> goodsStock, List<GoodsIcon> goodsIcon) throws BusinessException {

        goods.setGoodsType(0);
        GoodsService.add(goods, goodsStock, goodsIcon);
        renderSuccessJson();
    }

    public static void delete(Goods goods) throws Exception {

        renderSuccessJson();
    }

    public static void update(Goods goods) {
        GoodsService.update(goods);
        renderSuccessJson();
    }

    public static void upload(File boundary) {
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
                renderSuccessJson(fileName);
                return;
            }
        }
        renderErrorJson("upload fail");
        return;
    }


}
