package service.wx.service;

import com.google.gson.Gson;

import net.sf.json.JSONObject;
import play.Logger;
import service.wx.common.Configure;
import service.wx.common.Util;
import service.wx.dto.redpack.QueryRedpackReqDto;
import service.wx.dto.redpack.QueryRedpackRspDto;
import service.wx.dto.redpack.SendRedpackRspDto;
import exception.BusinessException;

public class QueryRedpackService extends BaseService{
	public static Gson gson = new Gson();
	
    public QueryRedpackService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.QUERY_REDPACK_API);
    }

    /**
     * 发送现金红包
     * @param sendRedpackReqDto
     * @return
     * @throws Exception
     */
    public QueryRedpackRspDto request(QueryRedpackReqDto queryRedpackReqDto) throws Exception {
    	String responseString = "";
    	try {
    		responseString = sendPost(queryRedpackReqDto, true);
    	} catch(Exception e) {
    		e.printStackTrace();
    		throw new BusinessException("请求微信查询现金红包接口发生错误");
    	}
    	Logger.info("查询现金红包API返回的数据是：%s", responseString);
    	QueryRedpackRspDto rsp = (QueryRedpackRspDto)Util.getObjectFromXMLWithXStream(responseString, QueryRedpackRspDto.class);
    	if(null == rsp || null == rsp.getReturn_code()) {
			//通信失败
			Logger.error("查询微信现金红包接口通信失败，请求数据为：%s, 返回数据为：%s", JSONObject.fromObject(queryRedpackReqDto).toString(),
					JSONObject.fromObject(rsp).toString());
			 throw new BusinessException("微信现金红包接口通信失败");
		} else {
			Logger.info("查询现金红包API成功返回对象数据：%s", gson.toJson(rsp));
			if(!rsp.getReturn_code().equals("SUCCESS")) {
				throw new BusinessException("查询微信现金红包API接口通信失败");
			}
			
			if (rsp.getResult_code().equals("SUCCESS")) {
				return rsp;
            } else {
            	throw new BusinessException("查询微信现金红包API接口交易失败");
            }
	   }
    }

}
