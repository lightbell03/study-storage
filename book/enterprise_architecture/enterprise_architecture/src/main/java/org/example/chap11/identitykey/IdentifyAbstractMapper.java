package org.example.chap11.identitykey;

import org.example.chap10.datamapper.mapper.AbstractMapper;
import org.example.common.ApplicationException;
import org.example.config.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class IdentifyAbstractMapper<K, D> extends AbstractMapper<K, D> {

    public D abstractFind(K key) {
        D result = loadedMap.get(key);
        if (result != null) return result;
        ResultSet rs = null;
        PreparedStatement statement = null;

        try {
            statement = ConnectionFactory.getInstance().prepare(findStatement());
            loadFindStatement(key, statement);
            rs = statement.executeQuery();
            rs.next();
            if (rs.isAfterLast()) return null;

            result = load(rs);
            return result;
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            ConnectionFactory.cleanUp(statement);
        }
    }

    protected void loadFindStatement(K key, PreparedStatement statement) throws SQLException {
        statement.setObject(1, key);
    }

    protected D load(ResultSet resultSet) throws SQLException {
        Key key = createKey(resultSet);
        if (loadedMap.containsKey(key)) {
            return loadedMap.get(key);
        }

        D result = doLoad(key, resultSet);
        loadedMap.put(key, result);
        return result;
    }

    abstract protected D doLoad(Key id, ResultSet resultSet) throws SQLException;

    protected Key createKey(ResultSet resultSet) throws SQLException {
        return new Key(resultSet.getLong(1));
    }
}
