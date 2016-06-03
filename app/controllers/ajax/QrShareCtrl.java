package controllers.ajax;

import java.io.File;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

import javax.persistence.Query;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;

import models.QrShare;
import models.QrcodeUploadRsp;
import net.sf.json.JSONObject;
import play.Play;
import play.data.Upload;
import play.data.validation.Required;
import play.db.jpa.Model;
import play.libs.Files;
import service.QrShareService;
import utils.PropertyUtil;
import common.constants.GlobalConstants;
import common.core.AjaxController;
import exception.BusinessException;


public class QrShareCtrl extends AjaxController {

    public static void add(QrShare qrShare) throws BusinessException {
    	if(null == qrShare) {
    		renderErrorJson("传入对象为空");
    	}
    	//新增时启用，判断图片是否存在
    	if(GlobalConstants.IS_ENABLED.equals(qrShare.getIsEnabled()) && StringUtils.isBlank(qrShare.getImgUrl())) {
    		renderErrorJson("启用之前必须上传底图");
    	}
    	
    	qrShare.setCreateTime(System.currentTimeMillis());
    	qrShare.setUpdateTime(System.currentTimeMillis());
    	QrShareService.add(qrShare);
		renderSuccessJson();
    }

    public static void update(QrShare qrShare) {
    	if(null == qrShare) {
    		renderErrorJson("传入对象为空");
    	} 
    	//进行深度copy
    	QrShare qrShareUpdate = null;
    	try {
			qrShareUpdate = (QrShare) qrShare.deepCopy();
		} catch (Exception e) {
			e.printStackTrace();
		}
    	if(null == qrShareUpdate) {
    		renderErrorJson("后台复制对象时发生错误");
    	}
    	QrShare origin = qrShare.refresh();

    	if(null == origin) {
    		renderErrorJson("库中不包含此记录");
    	}
    	//由启用转为不启用，判断库中是否还有已启用的记录
    	if(GlobalConstants.IS_ENABLED.equals(origin.getIsEnabled()) && 
    			GlobalConstants.NOT_ENABLED.equals(qrShareUpdate.getIsEnabled())) {
    		//判断库中是否有已启用的记录
    		if(0 >= QrShareService.countIsEnabledRecsButId(origin.getId(), GlobalConstants.IS_ENABLED)) {
    			renderErrorJson("库中不包含已启用的记录，不允许更新");
    		}
    	}
    	//如果是由不启用转为启用，判断图片是否存在
    	if(GlobalConstants.NOT_ENABLED.equals(origin.getIsEnabled()) && 
    			GlobalConstants.IS_ENABLED.equals(qrShareUpdate.getIsEnabled()) &&
    			StringUtils.isBlank(origin.getImgUrl())) {
    		renderErrorJson("底图未上传，不允许更新");
    	}
    			
    	//这里用qrShareUpdate直接更新的话会报错
    	PropertyUtil.copyPropertyValue(qrShareUpdate, qrShare);
    	qrShare.setUpdateTime(System.currentTimeMillis());
    	
    	QrShareService.update(qrShare);
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
