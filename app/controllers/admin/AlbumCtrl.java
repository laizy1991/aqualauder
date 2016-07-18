package controllers.admin;

import common.core.Pager;
import common.core.WebController;
import models.Album;

import java.util.List;

public class AlbumCtrl extends WebController {

    public static void list(int page) {
    	if(0 == page) {
			page = 1;
		}
		int pageSize = 8;
		
		Long count = Album.count();
        List<Album> album = Album.all().fetch(page, pageSize);
        
        Pager<Album> pageData = new Pager<Album>(count.intValue(), page, pageSize);
        pageData.setList(album);
        
        render("/admin/Album/list.html", pageData);
    }
}
