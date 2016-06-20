package service;

import dao.AlbumDao;
import models.Album;

/**
 * @author nemo
 * @date 2016.06.16
 */
public class AlbumService {
        public static void add(Album album) {
            AlbumDao.insert(album);
        }

        public static void delete(Album album) {
            AlbumDao.delete(album);
        }

        public static void update(Album album) {
            AlbumDao.update(album);
        }

        public static void add(String imgUrl) {
            Album album = new Album();
            long now = System.currentTimeMillis();
            album.setUpdateTime(now);
            album.setCreateTime(now);
            album.setPath(imgUrl);
            AlbumDao.insert(album);
        }

    }
