package com.cometproject.server.storage.queries.system;

import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class StatisticsDao {
    public static void saveStatistics(int players, int rooms, String version) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("UPDATE server_status SET active_players = ?, active_rooms = ?, server_version = ?", sqlConnection);

            preparedStatement.setInt(1, players);
            preparedStatement.setInt(2, rooms);
            preparedStatement.setString(3, version);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
