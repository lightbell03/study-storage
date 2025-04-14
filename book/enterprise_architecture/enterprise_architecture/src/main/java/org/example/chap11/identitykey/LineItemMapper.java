package org.example.chap11.identitykey;

import org.example.common.Mapper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LineItemMapper extends IdentifyAbstractMapper<Key, LineItem> implements Mapper {

    public LineItem find(long orderId, long id) {
        Key key = new Key(orderId, id);
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

        return null;
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

    public void loadAllLineItemsFor(Order order) {

    }
}
