package service.wx.service.qrcode;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import service.wx.common.Configure;
import service.wx.dto.qrcode.CreateQrCodeRspDto;
import service.wx.dto.qrcode.CreateTmpQrCodeReqDto;
import utils.FileTypeUtil;
import utils.WxUtil;
import utils.http.HttpRequester;
import utils.http.HttpRespons;

import com.google.gson.Gson;

import exception.BusinessException;

public class CreateTmpQrCodeService {

    public CreateTmpQrCodeService() {
    }

    /**
     * 创建临时二维码
     * @return 返回本地相对二维码路径
     * @throws Exception
     */
    public CreateQrCodeRspDto request(CreateTmpQrCodeReqDto createTmpQrCodeReqDto) throws BusinessException {
    	if(null == createTmpQrCodeReqDto.getAction_info().getScene().getScene_id() || 
    			createTmpQrCodeReqDto.getAction_info().getScene().getScene_id() <= 0) {
    		throw new BusinessException("请求微信创建临时二维码接口参数错误");
    	}
    	String responseString = "";
    	try {
    		String url = String.format(new String(Configure.CREATE_QRCODE_API), WxUtil.getAccessToken());
    		String params = new Gson().toJson(createTmpQrCodeReqDto);
    		
    		HttpRespons rsp = HttpRequester.sendPost(url, params);
    		if(null == rsp || StringUtils.isBlank(rsp.getContent())) {
    			throw new BusinessException("请求微信创建临时二维码接口返回数据为空");
    		}
    		responseString = rsp.getContent();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信创建临时二维码接口发生错误");
    	}
    	Logger.info("创建临时二维码API返回的数据是：%s", responseString);
    	if(StringUtils.isBlank(responseString)) {
    		throw new BusinessException("请求微信创建临时二维码接口返回数据为空");
    	}
        //将从API返回的XML数据映射到Java对象
    	CreateQrCodeRspDto rsp = new CreateQrCodeRspDto();;
    	JSONObject json = JSONObject.fromObject(responseString);
    	if(!StringUtils.isBlank(json.optString("errcode"))) {
    		rsp.setSuccess(false);
    		rsp.setErrcode(json.optInt("errcode"));
    		rsp.setErrmsg(json.optString("errmsg"));
    		return rsp;
    	} else {
    		Logger.info("创建临时二维码API成功返回数据");
    		rsp.setSuccess(true);
    		rsp.setExpire_seconds(json.optInt("expire_seconds"));
    		rsp.setTicket(json.optString("ticket"));
    		rsp.setUrl(json.optString("url"));
    		if(null != rsp.getErrcode()) {
    			return rsp;
    		}
    		//向微信请求下载
    		String remoteUrl = null;
    		try {
    			remoteUrl = String.format(Configure.DOWNLOAD_QRCODE_API, URLEncoder.encode(rsp.getTicket(), "UTF-8"));
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    			throw new BusinessException("请求微信下载二维码图片发生错误");
    		}
    		//文件夹前缀
    		String picPrefixDir = Play.configuration.getProperty("wx.qrcode.path", "/data/project/aqualauder/pic/");
    		
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/");
    		String dateDir = sdf.format(new Date());
    		String picRelPath =  dateDir + createTmpQrCodeReqDto.getAction_info().getScene().getScene_id();
    		String picAbsPath = picPrefixDir + picRelPath;
    		String picAbsDir = picPrefixDir + dateDir;
    		
    		File absDirFile = new File(picAbsDir);
    		if(!absDirFile.exists()) {
    			if(!absDirFile.mkdirs()) {
    				Logger.error("创建文件夹失败，路径: %s", picAbsDir);
    				throw new BusinessException("创建文件夹发生错误");
    			}
    		}
    		
    		rsp.setPicRelPath(picRelPath);
    		//保存路径
    		File imageFile = new File(picAbsPath);
    		//存在的话就删除
    		if(imageFile.exists()) {
    			imageFile.delete();
    		}
    		if(!HttpRequester.downloadFromNet(remoteUrl, picAbsPath)) {
    			Logger.error("下载二维码图片失败");
    			throw new BusinessException("请求微信下载二维码图片发生错误");
    		}
    		//准备更新图片命名
    		String fileType = FileTypeUtil.getImageFileType(imageFile);
    		if(StringUtils.isNotBlank(fileType)) {
    			fileType = fileType.toLowerCase();
    			String newFileAbsPath = picAbsPath + "." + fileType;
    			String newFileRelPath = picRelPath + "." + fileType;
    			File newFile = new File(newFileAbsPath);
    			if(imageFile.renameTo(newFile)) {
    				rsp.setPicRelPath(newFileRelPath);
    				Logger.info("将二维码图片重命名成功");
    			} else {
    				Logger.error("将二维码图片重命名失败");
    			}
    		} else {
    			Logger.error("将二维码图片重命名失败");
    		}
    	}
		return rsp;
    }
}
