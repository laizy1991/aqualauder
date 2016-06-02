package controllers.ajax;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import models.QrShare;
import models.QrcodeUploadRsp;
import net.sf.json.JSONObject;
import play.Play;
import play.data.Upload;
import play.data.validation.Required;
import play.libs.Files;
import service.QrShareService;
import common.constants.GlobalConstants;
import common.core.AjaxController;
import exception.BusinessException;


public class QrShareCtrl extends AjaxController {

    public static void add(QrShare qrShare) throws BusinessException {
    	if(null == qrShare) {
    		renderErrorJson("传入对象为空");
    	}
    	qrShare.setCreateTime(System.currentTimeMillis());
    	qrShare.setUpdateTime(System.currentTimeMillis());
    	QrShareService.add(qrShare);
		renderSuccessJson();
    }

//    public static void delete(QrShare qrShare) throws Exception {
//    	qrShare.delete();
//        renderSuccessJson();
//    }

    public static void update(QrShare qrShare) {
    	if(null == qrShare) {
    		renderErrorJson("传入对象为空");
    	} 
    	//判断是否由启用转为不启用
    	QrShare origin = QrShareService.get(qrShare.getId());
    	if(null == origin) {
    		renderErrorJson("库中不包含此记录");
    	}
    	if(GlobalConstants.IS_ENABLED == origin.getIsEnabled() && 
    			GlobalConstants.NOT_ENABLED == qrShare.getIsEnabled()) {
    		//判断库中是否有已启用的记录
    		if(0 >= QrShareService.countIsEnabledRecs(GlobalConstants.IS_ENABLED)) {
    			renderErrorJson("库中不包含已启用的记录，不允许更新");
    		}
    	}
    	qrShare.setUpdateTime(System.currentTimeMillis());
    	qrShare.save();
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
				String fileDir = Play.configuration.getProperty("wx.qrcode.path", "/data/project/aqualauder/pic/") +
						Play.configuration.getProperty("wx.qrcode.bg.dir", "qrcode_bg/");
				String imgPath = fileDir + fileName; //文件在磁盘中的路径
				File storeFile = new File(imgPath);
				String imgUrl = Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn")
						+ Play.configuration.getProperty("wx.qrcode.prefix", "/qrimg/") 
						+ Play.configuration.getProperty("wx.qrcode.bg.dir", "qrcode_bg/") + fileName; //文件在网络中的路径
				Files.copy(file, storeFile);
				
				QrcodeUploadRsp rsp = new QrcodeUploadRsp();
				rsp.setImgPath(imgPath);
				rsp.setImgUrl(imgUrl);
				
				renderSuccessJson(rsp);
				return;
			}
		}
		renderErrorJson("upload fail");
		return;
	}
}
