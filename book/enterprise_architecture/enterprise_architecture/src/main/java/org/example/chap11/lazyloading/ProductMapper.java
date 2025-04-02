package org.example.chap11.lazyloading;

import org.example.chap10.datamapper.mapper.AbstractMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ProductMapper extends AbstractMapper<Long, Product> {
    private static ProductMapper INSTANCE = null;

    public static ProductMapper create() {
        if (INSTANCE == null) {
            INSTANCE = new ProductMapper();
        }

        return INSTANCE;
    }

    public List<Product> findForSupplier(long id) {
        return Collections.emptyList();
    }
    @Override
    protected String findStatement() {
        return null;
    }

    @Override
    protected Product doLoad(Long id, ResultSet rs) throws SQLException, IllegalAccessException {
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
