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
import service.wx.dto.qrcode.tmp.CreateTmpQrCodeReqDto;
import utils.FileTypeUtil;
import utils.WxUtil;
import utils.http.HttpRequester;
import utils.http.HttpRespons;

import com.google.gson.Gson;

import exception.BusinessException;

public class CreateTmpQrCodeService {
	public static Gson gson = new Gson();
    public CreateTmpQrCodeService() {
    }

    /**
     * 创建临时二维码
     */
    public CreateQrCodeRspDto request(CreateTmpQrCodeReqDto createTmpQrCodeReqDto) throws BusinessException {
    	if(null == createTmpQrCodeReqDto.getAction_info().getScene().getScene_id() || 
    			createTmpQrCodeReqDto.getAction_info().getScene().getScene_id() <= 0) {
    		throw new BusinessException("请求微信创建临时二维码接口参数错误");
    	}
    	String responseString = "";
    	try {
    		String url = String.format(new String(Configure.CREATE_QRCODE_API), WxUtil.getAccessToken());
    		String params = gson.toJson(createTmpQrCodeReqDto);
    		Logger.info("请求微信创建临时二维码入参为：%s", params);
    		HttpRespons rsp = HttpRequester.sendPost(url, params);
    		if(null == rsp || StringUtils.isBlank(rsp.getContent())) {
    			throw new BusinessException("请求微信创建临时二维码接口返回数据为空");
    		}
    		responseString = rsp.getContent();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信创建临时二维码接口发生错误");
    	}
    	Logger.info("请求创建临时二维码API返回的数据是：%s", responseString);
    	if(StringUtils.isBlank(responseString)) {
    		throw new BusinessException("请求微信创建临时二维码接口返回数据为空");
    	}
    	CreateQrCodeRspDto rsp = new CreateQrCodeRspDto();
    	JSONObject json = JSONObject.fromObject(responseString);
    	Logger.info("请求创建临时二维码API返回数据经解析后为: %s", gson.toJson(json));
    	if(!StringUtils.isBlank(json.optString("errcode"))) {
    		rsp.setSuccess(false);
    		rsp.setErrcode(json.optInt("errcode"));
    		rsp.setErrmsg(json.optString("errmsg"));
    		return rsp;
    	} else {
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
    		String picRelPath =  dateDir + "tmp_" + createTmpQrCodeReqDto.getAction_info().getScene().getScene_id();
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
    		//准备重命名图片
    		String fileType = FileTypeUtil.getImageFileType(imageFile);
    		if(StringUtils.isNotBlank(fileType)) {
    			fileType = fileType.toLowerCase();
    			String newFileAbsPath = picAbsPath + "." + fileType;
    			String newFileRelPath = picRelPath + "." + fileType;
    			File newFile = new File(newFileAbsPath);
    			if(imageFile.renameTo(newFile)) {
    				rsp.setPicRelPath(newFileRelPath);
    				Logger.info("将二维码图片重命名成功, 重命名后文件路径: %s", newFileAbsPath);
    			} else {
    				Logger.error("将二维码图片重命名失败，文件路径：%s", picAbsPath);
    			}
    		}
    	}
		return rsp;
    }
}
