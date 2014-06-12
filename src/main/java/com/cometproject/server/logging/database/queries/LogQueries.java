package com.cometproject.server.logging.database.queries;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.database.LogDatabaseHelper;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogQueries {
    public static void putEntry(AbstractLogEntry entry) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = LogDatabaseHelper.getConnection();

            preparedStatement = LogDatabaseHelper.prepare("INSERT into logs (`type`, `room_id`, `user_id`, `data`, `timestamp`) VALUES(?, ?, ?, ?, ?);", sqlConnection);

            preparedStatement.setString(1, entry.getType().toString());
            preparedStatement.setInt(2, entry.getRoomId());
            preparedStatement.setInt(3, entry.getUserId());
            preparedStatement.setString(4, entry.getString());
            preparedStatement.setInt(5, entry.getTimestamp());

            LogDatabaseHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            LogDatabaseHelper.handleSqlException(e);
        } finally {
            LogDatabaseHelper.closeSilently(preparedStatement);
            LogDatabaseHelper.closeSilently(sqlConnection);
        }
    }

    public static RoomVisitLogEntry putRoomVisit(int playerId, int roomId, int entryTime) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = LogDatabaseHelper.getConnection();

            preparedStatement = LogDatabaseHelper.prepare("INSERT into player_room_visits (`player_id`, `room_id`, `time_enter`) VALUES(?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, roomId);
            preparedStatement.setInt(3, entryTime);

            LogDatabaseHelper.executeStatementSilently(preparedStatement, false);

            resultSet = preparedStatement.getGeneratedKeys();

            while(resultSet.next()) {
                return new RoomVisitLogEntry(resultSet.getInt(1), playerId, roomId, entryTime);
            }
        } catch (SQLException e) {
            LogDatabaseHelper.handleSqlException(e);
        } finally {
            LogDatabaseHelper.closeSilently(preparedStatement);
            LogDatabaseHelper.closeSilently(sqlConnection);
            LogDatabaseHelper.closeSilently(resultSet);
        }

        return null;
    }

    public static void updateRoomEntry(RoomVisitLogEntry entry) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = LogDatabaseHelper.getConnection();

            preparedStatement = LogDatabaseHelper.prepare("UPDATE player_room_visits SET time_exit = ? WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, entry.getExitTime());
            preparedStatement.setInt(2, entry.getId());

            LogDatabaseHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            LogDatabaseHelper.handleSqlException(e);
        } finally {
            LogDatabaseHelper.closeSilently(preparedStatement);
            LogDatabaseHelper.closeSilently(sqlConnection);
        }
    }
}
