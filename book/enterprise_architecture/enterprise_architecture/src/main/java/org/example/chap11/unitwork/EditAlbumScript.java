package org.example.chap11.unitwork;

import org.example.chap11.foreignkey.Album;
import org.example.chap11.foreignkey.AlbumMapper;
import org.example.common.MapperRegistry;

public class EditAlbumScript {

    public static void updateTitle(Long albumId, String title) {
        UnitOfWork.CurrentUnitOfWorkThreadLocal.newCurrent();
        AlbumMapper mapper = MapperRegistry.getMapper(AlbumMapper.class);
        Album album = mapper.find(albumId);
        album.setTitle(title);
        UnitOfWork.CurrentUnitOfWorkThreadLocal.getCurrent().commit();
    }
}
