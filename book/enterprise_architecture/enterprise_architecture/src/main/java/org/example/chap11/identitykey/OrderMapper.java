package org.example.chap11.identitykey;

import org.example.chap10.datamapper.mapper.AbstractMapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper extends IdentifyAbstractMapper<Long, Order> {

    @Override
    protected String findStatement() {
        return "select id, customer from order where id = ?";
    }

    public Order find(Long id)  {
        return abstractFind(id);
    }

    @Override
    protected Order doLoad(Long id, ResultSet rs) throws SQLException, IllegalAccessException {
        return null;
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected void doInsert(Order subject, PreparedStatement insertStatement) throws SQLException {

    }
}
