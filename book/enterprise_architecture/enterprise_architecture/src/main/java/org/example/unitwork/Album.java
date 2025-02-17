package org.example.unitwork;

import org.example.common.DomainObject;
import org.example.common.IdGenerator;

public class Album extends DomainObject {
    private String id;
    private String title;

    public Album(String id, String name) {
        this.id = id;
        this.title = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public static Album create(String name) {
        Album album = new Album(IdGenerator.nextId(), name);
        album.markNew();
        return album;
    }
}
