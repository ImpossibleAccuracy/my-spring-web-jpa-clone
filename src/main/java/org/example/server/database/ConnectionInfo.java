package org.example.server.database;

public record ConnectionInfo(
        String host,
        int port,
        String database,
        String user,
        String password
) {
}
