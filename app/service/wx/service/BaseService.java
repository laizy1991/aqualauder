package service.wx.service;

import service.wx.common.Configure;

import java.io.IOException;
import java.lang.reflect.Field;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.HashMap;
import java.util.Map;

public class BaseService{

    //API的地址
    private String apiURL;

    //发请求的HTTPS请求器
    private IServiceRequest serviceRequest;

    public BaseService(String api) {
        apiURL = api;
        Class c;
		try {
			c = Class.forName(Configure.HttpsRequestClassName);
			serviceRequest = (IServiceRequest) c.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    protected String sendPost(Object xmlObj, boolean withCertFlag) throws UnrecoverableKeyException, IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return serviceRequest.sendPost(apiURL,xmlObj, withCertFlag);
    }

    /**
     * 供商户想自定义自己的HTTP请求器用
     * @param request 实现了IserviceRequest接口的HttpsRequest
     */
    public void setServiceRequest(IServiceRequest request){
        serviceRequest = request;
    }
}
