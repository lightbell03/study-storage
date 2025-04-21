package org.example.chap11.identitykey;

import org.example.common.Mapper;
import org.example.common.MapperRegistry;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderMapper extends IdentifyAbstractMapper<Long, Order> implements Mapper {

    @Override
    protected String findStatement() {
        return "select id, customer from order where id = ?";
    }

    public Order find(Long id)  {
        return abstractFind(id);
    }

    @Override
    protected Order doLoad(Long id, ResultSet resultSet) throws SQLException {
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

    @Override
    protected String insertStatementString() {
        return "INSERT INTO order VALUES (?, ?)";
    }

    @Override
    protected void performInsert(Order subject, PreparedStatement pstmt) throws SQLException {
        pstmt.setLong(1, subject.getId());
        pstmt.setString(2, subject.getCustomer());
    }
}
