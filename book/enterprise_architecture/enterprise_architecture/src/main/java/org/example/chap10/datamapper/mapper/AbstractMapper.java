package org.example.chap10.datamapper.mapper;

import org.example.chap10.datamapper.StatementSource;
import org.example.chap10.datamapper.annotation.Id;
import org.example.chap10.datamapper.annotation.Table;
import org.example.chap10.datamapper.common.DomainObject;
import org.example.common.ApplicationException;
import org.example.config.ConnectionFactory;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractMapper<K, D> {
    private static final String nextIdQuery =
            "SELECT AUTO_INCREMENT " +
                    " FROM INFORMATION_SCHEMA.TABLES " +
                    " WHERE TABLE_SCHEMA = DATABASE() " +
                    " AND TABLE_NAME = %s";
    private K findNextIncrementId(String tableName) {
        try {
            PreparedStatement pstmt = ConnectionFactory.getInstance().prepare(nextIdQuery);
            pstmt.setString(0, tableName);
            ResultSet rs = pstmt.executeQuery();

            return (K) rs.getObject(0);
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

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
        if (loadedMap.containsKey(id)) {
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

    protected List<D> findMany(StatementSource source) {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = ConnectionFactory.getInstance().prepare(source.sql());
            for (int i = 0; i < source.parameters().length; i++) {
                stmt.setObject(i, source.parameters()[i]);
            }
            rs = stmt.executeQuery();
            return loadAll(rs);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public K insert(DomainObject<K, D> subject) {
        PreparedStatement insertStatement = null;
        try {
            insertStatement = ConnectionFactory.getInstance().prepare(insertStatement());

            D domainObject = subject.getObject();
            Class<?> domainObjectClass = domainObject.getClass();

            Table table = domainObjectClass.getAnnotation(Table.class);

            K nextId = findNextIncrementId(table.name());
            subject.setId(nextId);
            insertStatement.setObject(1, subject.getId());

            for (Field field : domainObjectClass.getDeclaredFields()) {
                if(field.isAnnotationPresent(Id.class)) {
                    field.set(domainObject, nextId);
                }
            }


            doInsert(subject.getObject(), insertStatement);

            insertStatement.execute();
            loadedMap.put(subject.getId(), subject.getObject());

            return subject.getId();
        } catch (SQLException | IllegalAccessException e) {
            throw new ApplicationException(e);
        }
    }

    abstract protected String insertStatement();

    abstract protected void doInsert(D subject, PreparedStatement insertStatement) throws SQLException;
}
