package service.wx.service;

import service.wx.common.Configure;
import service.wx.dto.redpack.SendRedpackReqDto;

public class SendRedpackService extends BaseService{

    public SendRedpackService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.SEND_REDPACK_API);
    }

    /**
     * 发送现金红包
     * @param sendRedpackReqDto
     * @return
     * @throws Exception
     */
    public String request(SendRedpackReqDto sendRedpackReqDto) throws Exception {

        String responseString = sendPost(sendRedpackReqDto, true);

        return responseString;
    }

}
