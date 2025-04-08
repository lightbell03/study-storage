package org.example.chap11.identitykey;

import org.example.common.ApplicationException;
import org.example.config.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class KeyGenerator {
    private Connection conn;
    private String keyName;
    private long nextId;
    private long maxId;
    private int incrementBy;

    public KeyGenerator(Connection coon, String keyName, int incrementBy) {
        this.conn = coon;
        this.keyName = keyName;
        this.incrementBy = incrementBy;
        this.nextId = maxId = 0;
        try {
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new ApplicationException("Unable to turn off autocommit", e);
        }
    }

    public synchronized long nextKey() {
        if (nextId == maxId) {
            reserveIds();
        }

        return nextId++;
    }

    private void reserveIds() {
        PreparedStatement stmt = null;
        ResultSet rs = null;
        long newNextId;
        try {
            stmt = conn.prepareStatement("select nextId from keys where name = ? for update");
            stmt.setString(1, keyName);
            rs = stmt.executeQuery();
            rs.next();
            newNextId = rs.getLong(1);
        } catch (SQLException e) {
            throw new ApplicationException("Unable to generate ids", e);
        } finally {
            ConnectionFactory.cleanUp(stmt, rs);
        }

        long newMaxId = newNextId + incrementBy;
        stmt = null;
        try {
            stmt = conn.prepareStatement("update keys set nextId = ? where name = ?");
            stmt.setLong(1, newMaxId);
            stmt.setString(2, keyName);
            stmt.executeUpdate();
            conn.commit();
            nextId = newNextId;
            maxId = newMaxId;
        } catch (SQLException e) {
            throw new ApplicationException("Unable to generate ids", e);
        } finally {
            ConnectionFactory.cleanUp(stmt);
        }
    }
}
