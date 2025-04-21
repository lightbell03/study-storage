package org.example.chap11.identitykey;

import org.example.chap10.datamapper.mapper.AbstractMapper;
import org.example.common.ApplicationException;
import org.example.common.Column;
import org.example.common.Id;
import org.example.common.MultiId;
import org.example.config.ConnectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.security.Key;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class IdentifyAbstractMapper<K, D> extends AbstractMapper<K, D> {
    private Class<K> domainClazz;

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
        K key = createKey(resultSet);
        if (loadedMap.containsKey(key)) {
            return loadedMap.get(key);
        }

        D result = doLoad(key, resultSet);
        loadedMap.put(key, result);
        return result;
    }

    abstract protected D doLoad(K id, ResultSet resultSet) throws SQLException;

    protected K createKey(ResultSet resultSet) throws SQLException {
        try {
            for (Field field : domainClazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        String columnName = column.name();
                        Object key = resultSet.getObject(columnName);

                        return (K) key;
                    } else {
                        throw new ApplicationException("not found column annotation");
                    }
                } else if (field.isAnnotationPresent(MultiId.class)) {
                    if (field.isAnnotationPresent(Column.class)) {
                        Class<?> multiIdClazz = field.getDeclaringClass();

                        Constructor<?> multiIdClazzConstructor = multiIdClazz.getDeclaredConstructor();
                        Object o = multiIdClazzConstructor.newInstance();

                        for (Field multiIdClazzField : multiIdClazz.getDeclaredFields()) {
                            if (multiIdClazzField.isAnnotationPresent(Column.class)) {
                                Column column = multiIdClazzField.getAnnotation(Column.class);
                                String columnName = column.name();

                                Object columnValue = resultSet.getObject(columnName);
                                multiIdClazzField.setAccessible(true);
                                multiIdClazzField.set(o, columnValue);
                            }
                        }

                        return (K) o;
                    }
                }
            }
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new ApplicationException(e);
        }

        throw new ApplicationException("not found id");
    }

    public D insert(D subject) {
        PreparedStatement pstmt = null;
        try {
            pstmt = ConnectionFactory.getInstance().prepare(insertStatementString());
            performInsert(subject, pstmt);
            pstmt.executeQuery();
            return subject;
        } catch (SQLException e) {
            throw new ApplicationException(e);
        } finally {
            ConnectionFactory.cleanUp(pstmt);
        }
    }

    abstract protected String insertStatementString();

    abstract protected void performInsert(D subject, PreparedStatement pstmt) throws SQLException;
}
