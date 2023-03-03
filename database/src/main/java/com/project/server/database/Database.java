package com.project.server.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static volatile Database instance;

    private Database(ConnectionInfo info) {
        try {
            this.connection = DriverManager.getConnection(
                    String.format("jdbc:postgresql://%s:%d/%s", info.host(), info.port(), info.database()),
                    info.user(),
                    info.password());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Database getInstance() {
        if (instance == null) throw new NullPointerException("Database not initialized.");

        return instance;
    }

    public static Database initialize(ConnectionInfo connectionInfo) {
        Database localInstance = instance;
        if (localInstance == null) {
            synchronized (Database.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new Database(connectionInfo);
                }
            }
        }
        return localInstance;
    }

    private final Connection connection;

    public Connection getConnection() {
        return connection;
    }
}
