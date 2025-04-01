package org.example.chap10.datamapper;

import org.example.chap10.datamapper.entity.Artist;

public interface ArtistFinder {
    Artist find(Long id);
}
