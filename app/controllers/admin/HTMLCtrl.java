package controllers.admin;

import java.io.File;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import play.Logger;
import play.Play;
import utils.FileUtils;
import common.core.WebController;

public class HTMLCtrl extends WebController {
	public static String fileDir = Play.applicationPath.getAbsolutePath();
	public static String introduceFileRelPath = "/html/help/introduce_content.html";
	public static String explainFileRelPath = "/html/help/explain_content.html";
	public static String linkFileRelPath = "/html/help/link_content.html";
	
    public static void introduce() {
        String content = FileUtils.read(introduceFileRelPath);
        render("/admin/Html/introduce_edit.html", content);
    }
    
    public static void introduceUpdate(String content) {
    	if(StringUtils.isEmpty(content)) {
    		renderError("内容必须填写");
    	}
        try {
        	if(fileDir.endsWith(File.separator)) {
        		fileDir.substring(0, fileDir.length()-File.separator.length());
        	}
        	String fileSrcAbsPath = fileDir + introduceFileRelPath;
        	String fileBakRelPath = introduceFileRelPath + ".bak";
        	
        	File srcFile = new File(fileSrcAbsPath);
        	String srcContent = "";
        	if(srcFile.exists()) {
        		srcContent = FileUtils.read(introduceFileRelPath);
        		FileUtils.write(fileBakRelPath, srcContent, false);
        	}
        	
            FileUtils.write(introduceFileRelPath, content, false);
        } catch (IOException e) {
            Logger.error(e, "");
        }
        content = FileUtils.read(introduceFileRelPath);
        render("/admin/Html/introduce_edit.html", content);
    }
    
    public static void explain() {
        String content = FileUtils.read(explainFileRelPath);
        render("/admin/Html/explain_edit.html", content);
    }
    
    public static void explainUpdate(String content) {
        if(StringUtils.isEmpty(content)) {
    		renderError("内容必须填写");
    	}
        try {
        	if(fileDir.endsWith(File.separator)) {
        		fileDir.substring(0, fileDir.length()-File.separator.length());
        	}
        	String fileSrcAbsPath = fileDir + explainFileRelPath;
        	String fileBakRelPath = explainFileRelPath + ".bak";
        	
        	File srcFile = new File(fileSrcAbsPath);
        	String srcContent = "";
        	if(srcFile.exists()) {
        		srcContent = FileUtils.read(explainFileRelPath);
        		FileUtils.write(fileBakRelPath, srcContent, false);
        	}
        	
            FileUtils.write(explainFileRelPath, content, false);
        } catch (IOException e) {
            Logger.error(e, "");
        }
        content = FileUtils.read(explainFileRelPath);
        render("/admin/Html/explain_edit.html", content);
    }
    
    public static void link() {
        String content = FileUtils.read(linkFileRelPath);
        render("/admin/Html/link_edit.html", content);
    }
    
    public static void linkUpdate(String content) {
        if(StringUtils.isEmpty(content)) {
    		renderError("内容必须填写");
    	}
        try {
        	if(fileDir.endsWith(File.separator)) {
        		fileDir.substring(0, fileDir.length()-File.separator.length());
        	}
        	String fileSrcAbsPath = fileDir + linkFileRelPath;
        	String fileBakRelPath = linkFileRelPath + ".bak";
        	
        	File srcFile = new File(fileSrcAbsPath);
        	String srcContent = "";
        	if(srcFile.exists()) {
        		srcContent = FileUtils.read(linkFileRelPath);
        		FileUtils.write(fileBakRelPath, srcContent, false);
        	}
        	
            FileUtils.write(linkFileRelPath, content, false);
        } catch (IOException e) {
            Logger.error(e, "");
        }
        content = FileUtils.read(linkFileRelPath);
        render("/admin/Html/link_edit.html", content);
    }
}
