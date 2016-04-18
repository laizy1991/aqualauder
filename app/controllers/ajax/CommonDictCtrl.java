package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.CommonDict;
import service.CommonDictService;
import utils.StringUtil;

public class CommonDictCtrl extends AjaxController {

    public static void add(CommonDict commonDict) throws BusinessException {
        if (commonDict == null
                || StringUtil.isNullOrEmpty(commonDict.getKey())
                || StringUtil.isNullOrEmpty(commonDict.getValue())) {
            throw new BusinessException("Lack of information");
        }

        CommonDictService.add(commonDict);
        renderSuccessJson();
    }

    public static void delete(CommonDict commonDict) throws Exception {
        CommonDictService.delete(commonDict);
        renderSuccessJson();
    }

    public static void update(CommonDict commonDict) {
        CommonDictService.update(commonDict);
        renderSuccessJson();
    }

    
}
