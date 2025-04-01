package org.example.chap10.datamapper.mapper;

import org.example.chap10.datamapper.ArtistFinder;
import org.example.chap10.datamapper.entity.Artist;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistMapper extends AbstractMapper<Long, Artist> implements ArtistFinder {
    private static final String COLUMN_LIST = "id, name";

    @Override
    protected String findStatement() {
        return "SELECT " + COLUMN_LIST + " from artists where Id = ?";
    }

    @Override
    protected Artist doLoad(Long id, ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        return new Artist(id, name);
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected void doInsert(Artist subject, PreparedStatement insertStatement) throws SQLException {

    }

    @Override
    public Artist find(Long id) {
        return abstractFind(id);
    }
}
