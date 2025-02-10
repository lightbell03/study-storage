package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static ConnectionFactory INSTANCE;
    private String driver;
    private String url;
    private String user;
    private String password;

    private ConnectionFactory() {}


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

    public Connection getConnection() {
        try {
            Class.forName(driver);
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
