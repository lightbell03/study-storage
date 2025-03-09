package org.example.chap10.datamapper;

import org.example.common.DomainObject;
import org.example.config.ConnectionFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<K, D> {
    protected Map<K, D> loadedMap = new HashMap<>();

    abstract protected String findStatement();

    protected D abstractFind(K id) {
        D result = loadedMap.get(id);
        if (result != null) return result;
        PreparedStatement findStatement = null;
        try {
            findStatement = ConnectionFactory.getInstance().prepare(findStatement());
            findStatement.setObject(1, id);
            ResultSet rs = findStatement.executeQuery();
            rs.next();
            result = load(rs);

            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected D load(ResultSet rs) throws SQLException {
        K id = (K) rs.getObject(1);
        if(loadedMap.containsKey(id)) {
            return loadedMap.get(id);
        }
        D result = doLoad(id, rs);
        loadedMap.put(id, result);
        return result;
    }

    abstract protected D doLoad(K id, ResultSet rs) throws SQLException;

    protected List<D> loadAll(ResultSet rs) throws SQLException {
        List<D> result = new ArrayList<>();
        while (rs.next()) {
            result.add(load(rs));
        }
        return result;
    }
}
