package org.example.chap11.identitykey;

import org.example.common.ApplicationException;
import org.example.common.Mapper;
import org.example.common.MapperRegistry;
import org.example.config.ConnectionFactory;

import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LineItemMapper extends IdentifyAbstractMapper<LineItemKey, LineItem> implements Mapper {

    public LineItem find(long orderId, long id) {
        LineItemKey key = new LineItemKey(orderId, id);
        return find(key);
    }

    public LineItem find(LineItemKey key) {
        return abstractFind(key);
    }


    @Override
    protected void loadFindStatement(LineItemKey key, PreparedStatement statement) throws SQLException {
        statement.setObject(1, key.getOrderId());
        statement.setObject(2, key.getId());
        super.loadFindStatement(key, statement);
    }

    @Override
    protected LineItem doLoad(LineItemKey id, ResultSet rs) throws SQLException {
        Order order = MapperRegistry.getMapper(OrderMapper.class).find(id.getOrderId());
        return doLoad(id, rs, order);
    }

    private LineItem doLoad(LineItemKey id, ResultSet rs, Order order) throws SQLException {
        LineItem result;

        int amount = rs.getInt("amount");
        String product = rs.getString("product");
        result = new LineItem(id, amount, product);
        // link
        order.addLineItem(result);

        return result;
    }

    @Override
    protected String findStatement() {
        return "SELECT order_id, id, amount, product, FROM line_item WHERE order_id = ? AND id = ?";
    }

    @Override
    protected String insertStatement() {
        return null;
    }

    @Override
    protected void doInsert(LineItem subject, PreparedStatement insertStatement) throws SQLException {

    }

    private static final String findForOrderString = "SELECT order_id, id, amount, product " +
            " FROM line_item " +
            " WHERE order_id = ?";

    public void loadAllLineItemsFor(Order order) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            statement = ConnectionFactory.getInstance().prepare(findForOrderString);
            statement.setObject(1, order.getId());
            rs = statement.executeQuery();
            while (rs.next()) {
                load(rs, order);
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            ConnectionFactory.cleanUp(statement, rs);
        }
    }

    protected LineItem load(ResultSet rs, Order order) throws SQLException {
        LineItemKey key = createKey(rs);
        if (loadedMap.containsKey(key)) {
            return loadedMap.get(key);
        }
        LineItem result = doLoad(key, rs, order);
        loadedMap.put(key, result);
        return result;
    }

    @Override
    protected String insertStatementString() {
        return "INSERT INTO line_item VALUES (?, ?, ?, ?)";
    }

    @Override
    protected void performInsert(LineItem subject, PreparedStatement pstmt) throws SQLException {
        pstmt.setLong(1, subject.getKey().getOrderId());
        pstmt.setLong(2, subject.getKey().getOrderId());
        pstmt.setInt(3, subject.getAmount());
        pstmt.setString(4, subject.getProduct());
    }
}
