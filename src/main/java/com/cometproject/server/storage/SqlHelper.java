package com.cometproject.server.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
            if (connection == null) { return; }
            connection.close();
        } catch (SQLException e) { }
    }

    public static void closeStatementSilently(PreparedStatement statement) {
        try {
            if (statement == null) { return; }
            statement.close();
        } catch (SQLException e) { }
    }

    public static void executeStatementSilently(PreparedStatement statement, boolean autoClose) {
        try {
            if (statement == null) { return; }
            statement.execute();

            if (autoClose) {
                statement.close();
            }
        } catch (SQLException e) { }
    }

    public static boolean exists(String query, Connection con, boolean autoClose) throws SQLException {
        try {
            return con.createStatement().executeQuery(query).next();
        } finally {
            if (autoClose) {
                closeSilently(con);
            }
        }
    }

    public static int count(String query, Connection con, boolean autoClose) throws SQLException {
        try {
            int i = 0;

            ResultSet r = con.prepareStatement(query).executeQuery();
            while (r.next()) {
                i++;
            }

            return i;
        } finally {
            if (autoClose) {
                closeSilently(con);
            }
        }
    }

    // *** CANNOT AUTO CLOSE RESULT SETS - THEY MUST BE CLOSED ONCE YOU HAVE FINISHED READING FROM THE RESULTSET *** //

    public static ResultSet getRow(String query, Connection con) throws SQLException {
        ResultSet r = con.prepareStatement(query).executeQuery();

        while (r.next()) {
            return r;
        }

        return null;
    }

    public static ResultSet getRow(PreparedStatement statement, Connection con) throws SQLException {
        ResultSet r = statement.executeQuery();

        if (r.next()) {
            return r;
        }

        return null;
    }

    public static ResultSet getTable(String query, Connection con) throws SQLException {
        return con.prepareStatement(query).executeQuery();
    }

    public static String getString(String query, Connection con) throws SQLException {
        ResultSet result = con.prepareStatement(query).executeQuery();
        result.first();

        String str = query.split(" ")[1];

        if (str.startsWith("`")) {
            str = str.substring(1, str.length() - 1);
        }

        return result.getString(str);
    }

    public static PreparedStatement prepare(String query, Connection con) throws SQLException {
        return prepare(query, con, false);
    }

    public static PreparedStatement prepare(String query, Connection con, boolean returnKeys) throws SQLException {
        return returnKeys ? con.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS) : con.prepareStatement(query);
    }

    public static void handleSqlException(SQLException e) {

    }
}
