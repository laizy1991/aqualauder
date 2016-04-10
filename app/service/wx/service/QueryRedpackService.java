package service.wx.service;

import service.wx.common.Configure;
import service.wx.dto.redpack.QueryRedpackReqDto;

public class QueryRedpackService extends BaseService{

    public QueryRedpackService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.QUERY_REDPACK_API);
    }

    /**
     * 发送现金红包
     * @param sendRedpackReqDto
     * @return
     * @throws Exception
     */
    public String request(QueryRedpackReqDto queryRedpackReqDto) throws Exception {

        String responseString = sendPost(queryRedpackReqDto, true);

        return responseString;
    }

}
