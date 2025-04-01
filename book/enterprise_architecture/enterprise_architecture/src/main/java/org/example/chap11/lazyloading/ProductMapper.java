package org.example.chap11.lazyloading;

import org.example.chap10.datamapper.mapper.AbstractMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper extends AbstractMapper<Long, Product> {
    @Override
    protected String findStatement() {
        return null;
    }

    @Override
    protected Product doLoad(Long id, ResultSet rs) throws SQLException {
        return null;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected void doInsert(Product subject, PreparedStatement insertStatement) throws SQLException {

    }
}
