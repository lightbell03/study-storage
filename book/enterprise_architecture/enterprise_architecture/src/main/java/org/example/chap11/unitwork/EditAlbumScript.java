package org.example.chap10.unitwork;

import org.example.common.Mapper;
import org.example.common.MapperRegistry;

public class EditAlbumScript {

    public static void updateTitle(String albumId, String title) {
        UnitOfWork.CurrentUnitOfWorkThreadLocal.newCurrent();
        Mapper<Album, String> mapper = MapperRegistry.getMapper(Album.class, String.class);
        Album album = mapper.find(albumId);
        album.setTitle(title);
        UnitOfWork.CurrentUnitOfWorkThreadLocal.getCurrent().commit();
    }
}
