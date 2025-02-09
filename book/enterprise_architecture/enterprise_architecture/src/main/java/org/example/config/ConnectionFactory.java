package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static ConnectionFactory INSTANCE;
    private final String url;
    private final String user;
    private final String password;

    private ConnectionFactory() {
        this.url = "";
        this.user = "";
        this.password = "";
    }

    public static ConnectionFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ConnectionFactory();
        }

        return INSTANCE;
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
