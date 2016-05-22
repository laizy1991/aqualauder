package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import play.Logger;

public class FileUtils {

    /**
     * 
     * @param relativePath
     * @param content
     * @param append
     * @throws IOException 
     */
    public static void write(String relativePath, byte[] content, boolean append) throws IOException {
        if(StringUtils.isBlank(relativePath) || content == null) {
            return;
        }
        
        String dir = System.getProperty("user.dir");
        File file = new File(new File(dir), relativePath);
        File pFile = new File(file.getParent());
        if(!pFile.exists() || pFile.isFile()) {
            pFile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream(file, append);
        fos.write(content);
        fos.flush();
        fos.close();
    }
 
    public static void write(String relativePath, String content, boolean append) throws IOException {
        write(relativePath, content.getBytes(), append);
    }
    
    public static String read(String relativePath) {
        String dir = System.getProperty("user.dir");
        File file = new File(new File(dir), relativePath);
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
        } catch (IOException e) {
            Logger.error(e, "");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }
}
