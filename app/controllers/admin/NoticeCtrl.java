package controllers.admin;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.constants.GlobalConstants;
import common.core.Pager;
import common.core.WebController;
import dao.GoodsTypeDao;
import dao.NoticeDao;
import dto.NoticePicInfo;
import models.*;
import utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class NoticeCtrl extends WebController {

    public static void update() {
        Notice notice = NoticeDao.get();
        List<NoticePicInfo> imgs = new ArrayList<>();
        String desc = "";
        if(notice != null) {
            desc = notice.getContent();
            imgs = new Gson().fromJson(notice.getPicInfo(), new TypeToken<List<NoticePicInfo>>(){}.getType());
        }

        String imgsJson = notice.getPicInfo();

        render("/admin/Notice/update.html", desc, imgs, imgsJson);


    }
}
