package org.example.chap10.datamapper.entity;

import org.example.chap10.datamapper.annotation.Id;
import org.example.chap10.datamapper.annotation.Table;

@Table(name="artists")
public class Artist {
    @Id
    private Long id;

    private String name;

    public Artist(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
