package org.example.config;

import org.example.common.ApplicationException;

import java.sql.*;

public class ConnectionFactory {
    private static ConnectionFactory INSTANCE;
    private String driver;
    private String url;
    private String user;
    private String password;

    private ConnectionFactory() {
    }


    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static ConnectionFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionFactory();
        }

        return INSTANCE;
    }

    public PreparedStatement prepare(String statement) throws SQLException {
        return getConnection().prepareStatement(statement);
    }

    public Connection getConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void cleanUp(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }

    public static void cleanUp(Statement statement, ResultSet rs) {
        cleanUp(statement);
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new ApplicationException(e);
        }
    }
}
