package controllers.ajax;

import common.core.AjaxController;
import exception.BusinessException;
import models.Album;
import play.Play;
import play.data.Upload;
import play.libs.Files;
import service.AlbumService;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class AlbumCtrl extends AjaxController {

    public static void add(Album album) throws BusinessException {
        if(album!= null) {
            AlbumService.add(album);
            renderSuccessJson();
        } else {
            renderErrorJson("添加失败!");
        }
    }

    public static void delete(Album album) throws Exception {
        if(album!= null) {
            AlbumService.delete(album);
            renderSuccessJson();
        } else {
            renderErrorJson("删除失败!");
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
                        Play.configuration.getProperty("wx.album.pic.dir", "album/");
                String imgPath = fileDir + fileName; //文件在磁盘中的路径
                File storeFile = new File(imgPath);
                String imgUrl = Play.configuration.getProperty("local.host.domain", "http://wx.aqualauder.cn")
                        + Play.configuration.getProperty("wx.qrcode.prefix", "/qrimg/")
                        + Play.configuration.getProperty("wx.album.pic.dir", "album/") + fileName;
                Files.copy(file, storeFile);
                Files.copy(file, storeFile);
                AlbumService.add(imgUrl);
                renderSuccessJson(imgUrl);
                return;
            }
        }
        renderErrorJson("upload fail");
        return;
    }
}
