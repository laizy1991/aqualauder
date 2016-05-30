package controllers.ajax;

import java.io.File;
import java.security.InvalidParameterException;

import javax.validation.Valid;

import models.QrShare;
import net.sf.json.JSONObject;
import play.data.validation.Required;

import common.core.AjaxController;

import exception.BusinessException;


public class QrShareCtrl extends AjaxController {

   /* public static void add(QrcodeBg goods, List<GoodsStock> goodsStock, List<GoodsIcon> goodsIcon) throws BusinessException {

        goods.setGoodsType(0);
        GoodsService.add(goods, goodsStock, goodsIcon);
        renderSuccessJson();
    }*/

    public static void delete(QrShare qrShare) throws Exception {
        renderSuccessJson();
    }

    public static void update(QrShare qrShare) {
//        GoodsService.update(goods);
        renderSuccessJson();
    }

    /*public static void upload(File boundary) {
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
    }*/

    /**
	 * 上传活动图片
	 * @param file
	 * @throws InvalidParameterException
	 * @throws BusinessException
	 */
	public static void uploadImg(@Required @Valid File file) {
		JSONObject json = new JSONObject();
		boolean isSuccess = true;
		String msg = "";
		String remoteUrl = "http://wx.aqualauder.cn/qrimg/2016/05/limit_10011.jpeg";
		json.put("remoteUrl", remoteUrl);
		json.put("isSuccess", isSuccess);
		json.put("msg", msg);
		renderJSON(json);
	}
}
