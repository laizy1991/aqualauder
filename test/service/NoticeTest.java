package service;

import java.util.ArrayList;
import java.util.List;

import models.Notice;

import org.junit.Test;

import play.test.UnitTest;

import com.google.gson.Gson;

import dao.NoticeDao;
import dto.NoticePicInfo;

public class NoticeTest extends UnitTest {

    @Test
    public void test() {
        Notice notice = new Notice();
        notice.setCreateTime(System.currentTimeMillis());
        notice.setUpdateTime(System.currentTimeMillis());
        notice.setContent("这里是公告");
        List<NoticePicInfo> infos = new ArrayList<NoticePicInfo>();
        NoticePicInfo info = new NoticePicInfo();
        info.setActivityUrl("http://120.25.58.100:8080/goods_list.html?fromPage=true&type=-1");
        info.setPicUrl("http://120.25.58.100:8080/public/pictures/goods/4b6c8b205eb748cc8093937468ef00cb.jpg");
        info.setSortNum(0);
        NoticePicInfo info2 = new NoticePicInfo();
        info2.setActivityUrl("http://120.25.58.100:8080/goods_list.html?fromPage=true&type=-1");
        info2.setPicUrl("http://120.25.58.100:8080/public/pictures/goods/8c9f17b4c45041a28960579656bb8190.jpg");
        info2.setSortNum(1);
        NoticePicInfo info3 = new NoticePicInfo();
        info3.setActivityUrl("http://120.25.58.100:8080/goods_list.html?fromPage=true&type=-1");
        info3.setPicUrl("http://120.25.58.100:8080/public/pictures/goods/87dd1e545b8d4ef3b6938c7ad7af7681.jpg");
        info3.setSortNum(2);
        infos.add(info);
        infos.add(info2);
        infos.add(info3);
        notice.setPicInfo(new Gson().toJson(infos));
        NoticeDao.save(notice);
    }
    
}
