package org.example.chap11.lazyloading;


import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.example.chap10.datamapper.mapper.AbstractMapper;

public class SupplierMapper extends AbstractMapper<Long, TestProduct> {

    @Override
    protected String findStatement() {
        return null;
    }

    @Override
    protected TestProduct doLoad(Long id, ResultSet rs) throws SQLException, IllegalAccessException {
        String nameArg = rs.getString(2);
        TestProduct result = new TestProduct(id, nameArg);
        Class<?> clz = result.getClass();
        for (Field field : clz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Lazy.class)) {
                field.set(result, new VirtualList<>(new ProductLoader(id)));
            }
        }

        return result;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected void doInsert(TestProduct subject, PreparedStatement insertStatement) throws SQLException {

    }

    public static class ProductLoader implements VirtualListLoader<Product> {
        private Long id;

        public ProductLoader(Long id) {
            this.id = id;
        }

        @Override
        public List<Product> load() {
            return ProductMapper.create().findForSupplier(id);
        }
    }
}
