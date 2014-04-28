package com.cometproject.server.storage;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Matty on 28/04/2014.
 */
public class SqlHelper {
    private static SqlStorageEngine storage;

    public static void init(SqlStorageEngine storageEngine) {
        storage = storageEngine;
    }

    public static Connection getConnection() throws SQLException {
        return storage.getConnections().getConnection();
    }

    public static void closeSilently(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) { }
    }
}
