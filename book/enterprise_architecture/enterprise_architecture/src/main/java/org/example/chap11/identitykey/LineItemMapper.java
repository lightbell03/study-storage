package org.example.chap11.identitykey;

import org.example.common.ApplicationException;
import org.example.common.Mapper;
import org.example.common.MapperRegistry;
import org.example.config.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LineItemMapper extends IdentifyAbstractMapper<Key, LineItem> implements Mapper {

    public LineItem find(long orderId, long id) {
        Key key = new Key(orderId, id);
        return find(key);
    }

    public LineItem find(Key key) {
        return abstractFind(key);
    }


    @Override
    protected void loadFindStatement(Key key, PreparedStatement statement) throws SQLException {
        statement.setObject(1, key.getField(0));
        statement.setObject(2, key.getField(1));
        super.loadFindStatement(key, statement);
    }

    @Override
    protected LineItem doLoad(Key id, ResultSet rs) throws SQLException {
        Order order = MapperRegistry.getMapper(OrderMapper.class).find((Long) id.getField(0));
        return doLoad(id, rs, order);
    }

    private LineItem doLoad(Key id, ResultSet rs, Order order) throws SQLException {
        LineItem result;

        int amount = rs.getInt("amount");
        String product = rs.getString("product");
        result = new LineItem(id, amount, product);
        // link
        order.addLineItem(result);

        return result;
    }


    @Override
    protected Key createKey(ResultSet resultSet) throws SQLException {
        return new Key(resultSet.getLong("order_id"), resultSet.getLong("id"));
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
            statement.setObject(1, order.getKey().getField(0));
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
        Key key = createKey(rs);
        if (loadedMap.containsKey(key)) {
            return loadedMap.get(key);
        }
        LineItem result = doLoad(key, rs, order);
        loadedMap.put(key, result);
        return result;
    }
}
