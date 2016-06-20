package dao;

import models.Album;

/**
 * @author nemo
 * @date 2016.06.16
 */
public class AlbumDao {
    public static Album get(long id) {
        return Album.findById(id);
    }

    public static void update(Album album) {
        album.save();
    }

    public static void insert(Album album) {
        album.save();
    }

    public static void delete(Album album) {
        album.delete();
    }
}