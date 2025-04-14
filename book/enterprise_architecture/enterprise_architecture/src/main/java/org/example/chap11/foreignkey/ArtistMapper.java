package org.example.chap11.foreignkey;

import org.example.common.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistMapper extends ForeignKeyAbstractMapper<Long, Artist> implements Mapper {
    public Artist find(Long id) {
        return null;
    }
    @Override
    protected Artist doLoad(Long id, ResultSet rs) throws SQLException, IllegalAccessException {
        return null;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected void doInsert(Artist subject, PreparedStatement insertStatement) throws SQLException {

    }

    @Override
    protected String findStatement() {
        return null;
    }

    @Override
    protected void update(Artist domain) {

    }
}
