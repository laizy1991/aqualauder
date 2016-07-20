package controllers.ajax;

import com.google.gson.Gson;
import common.core.AjaxController;
import dao.NoticeDao;
import dto.NoticePicInfo;
import models.Notice;
import org.apache.commons.collections.CollectionUtils;
import play.Logger;
import play.Play;
import play.data.Upload;
import play.libs.Files;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


public class NoticeCtrl extends AjaxController {


    public static void update(String desc, List<NoticePicInfo> imgs) {
        try {
            Collections.sort(imgs, new Comparator<NoticePicInfo>() {
                @Override
                public int compare(NoticePicInfo o1, NoticePicInfo o2) {
                    return o1.getSortNum() > o2.getSortNum()?1:(o1.getSortNum() < o2.getSortNum()?-1:0);
                }
            });

            Notice notice = NoticeDao.get();
            notice.setContent(desc);
            String imgsStr = new Gson().toJson(imgs);
            notice.setPicInfo(imgsStr);
            NoticeDao.save(notice);
            renderSuccessJson();
        }catch (Exception e) {
            Logger.error("fail to update Notice", e.getMessage());
            renderErrorJson("修改失败!");
        }

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
                        Play.configuration.getProperty("wx.notice.pic.dir", "notice/");
                String imgPath = fileDir + fileName; //文件在磁盘中的路径
                File storeFile = new File(imgPath);
                String imgUrl = Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn")
                        + Play.configuration.getProperty("wx.qrcode.prefix", "/qrimg/")
                        + Play.configuration.getProperty("wx.notice.pic.dir", "notice/") + fileName;
                Files.copy(file, storeFile);
                renderSuccessJson(imgUrl);
                return;
            }
        }
        renderErrorJson("upload fail");
        return;
    }
}
