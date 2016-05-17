package controllers.admin;

import java.io.File;
import java.io.IOException;

import play.Logger;
import play.Play;
import utils.FileUtils;
import common.core.WebController;

public class HTMLCtrl extends WebController {

    public static void introduce() {
        String content = FileUtils.read("/app/views/Front/help/introduce_content.html");
        render("/admin/Html/introduce_edit.html", content);
    }
    
    public static void introduceUpdate(String content) {
        try {
            FileUtils.write("/app/views/Front/help/introduce_content.html", content, false);
        } catch (IOException e) {
            Logger.error(e, "");
        }
        render("/admin/Html/introduce_edit.html", content);
    }
}
