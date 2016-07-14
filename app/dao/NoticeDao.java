package dao;

import java.util.List;

import models.Notice;

import org.apache.commons.collections.CollectionUtils;

public class NoticeDao {
    public static void save(Notice notice) {
        notice.save();
    }

    public static Notice get() {
        List<Notice> notices = Notice.all().fetch();
        if(CollectionUtils.isEmpty(notices)) {
            return null;
        }
        
        return notices.get(0);
    }
    
}