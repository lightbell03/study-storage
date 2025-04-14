package org.example.chap11.identitykey;

import org.example.chap10.datamapper.mapper.AbstractMapper;
import org.example.common.MapperRegistry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper extends IdentifyAbstractMapper<Key, Order> {

    @Override
    protected String findStatement() {
        return "select id, customer from order where id = ?";
    }

    public Order find(Long id)  {
        return abstractFind(new Key(id));
    }

    @Override
    protected Order doLoad(Key id, ResultSet resultSet) throws SQLException {
        String customer = resultSet.getString("customer");
        Order result = new Order(id, customer);
        MapperRegistry.getMapper(LineItemMapper.class).loadAllLineItemsFor(result);

        return result;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected void doInsert(Order subject, PreparedStatement insertStatement) throws SQLException {

    }
}
