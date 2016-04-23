package service.wx.service.qrcode;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
import play.Logger;
import service.wx.common.Configure;
import service.wx.common.Signature;
import service.wx.common.Util;
import service.wx.dto.qrcode.CreateQrCodeRspDto;
import service.wx.dto.qrcode.CreateTmpQrCodeReqDto;
import service.wx.service.BaseService;
import utils.WxUtil;
import utils.http.HttpRequester;
import exception.BusinessException;

public class CreateTmpQrCodeService extends BaseService{

    public CreateTmpQrCodeService() {
    	super(String.format(new String(Configure.CREATE_QRCODE_API), WxUtil.getAccessToken()));
    }

    /**
     * 创建临时二维码
     * @return 返回本地二维码路径
     * @throws Exception
     */
    public String request(CreateTmpQrCodeReqDto createTmpQrCodeReqDto) throws BusinessException {
    	String responseString = "";
    	try {
    		responseString = sendPost(createTmpQrCodeReqDto, false).trim();
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信创建临时二维码接口发生错误");
    	}
    	Logger.info("创建临时二维码API返回的数据是：%s", responseString);
        //将从API返回的XML数据映射到Java对象
    	CreateQrCodeRspDto rsp = (CreateQrCodeRspDto)Util.getObjectFromXMLWithXStream(responseString, CreateQrCodeRspDto.class);
	    if(null == rsp || StringUtils.isBlank(rsp.getTicket())) {
			//通信失败
			Logger.error("微信创建临时二维码接口通信失败，请求数据为：%s, 返回数据为：%s", JSONObject.fromObject(createTmpQrCodeReqDto).toString(),
					JSONObject.fromObject(rsp).toString());
			 throw new BusinessException("微信创建临时二维码接口通信失败");
		} else {
			Logger.info("创建临时二维码API成功返回数据");
			//向微信请求下载
			String remoteUrl = null;
			try {
				remoteUrl = String.format(Configure.DOWNLOAD_QRCODE_API, URLEncoder.encode(rsp.getTicket(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new BusinessException("请求微信下载二维码图片发生错误");
			}
			//TODO 这里要定义存放文件规则
			String savePath = "";
			if(!HttpRequester.downloadFromNet(remoteUrl, savePath)) {
				Logger.error("下载二维码图片失败");
				throw new BusinessException("请求微信下载二维码图片发生错误");
			}
			return savePath;
	   }
    }
}
