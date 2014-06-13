package com.cometproject.server.logging.database.queries;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.logging.database.LogDatabaseHelper;
import com.cometproject.server.logging.entries.RoomChatLogEntry;
import com.cometproject.server.logging.entries.RoomVisitLogEntry;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

    public static void updateRoomEntries() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = LogDatabaseHelper.getConnection();

            preparedStatement = LogDatabaseHelper.prepare("UPDATE player_room_visits SET time_exit = ? WHERE time_exit = 0", sqlConnection);

            preparedStatement.setInt(1, (int) Comet.getTime());

            LogDatabaseHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            LogDatabaseHelper.handleSqlException(e);
        } finally {
            LogDatabaseHelper.closeSilently(preparedStatement);
            LogDatabaseHelper.closeSilently(sqlConnection);
        }
    }

    public static List<RoomChatLogEntry> getChatlogsByCriteria(int playerId, int roomId, int entryTime, int exitTime) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<RoomChatLogEntry> chatlogs = new ArrayList<>();

        try {
            sqlConnection = LogDatabaseHelper.getConnection();

            preparedStatement = LogDatabaseHelper.prepare("SELECT `data`,`timestamp` FROM `logs` WHERE `timestamp` > ? AND `timestamp` < ? AND `type` = 'ROOM_CHATLOG' AND `user_id` = ? AND `room_id` = ? ORDER BY `timestamp` DESC LIMIT ?", sqlConnection);

            preparedStatement.setInt(1, entryTime);
            preparedStatement.setInt(2, exitTime == 0 ? (int) Comet.getTime() : exitTime);
            preparedStatement.setInt(3, playerId);
            preparedStatement.setInt(4, roomId);
            preparedStatement.setInt(5, 150); // Limit...

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                chatlogs.add(new RoomChatLogEntry(playerId, roomId, resultSet.getString("data"), resultSet.getInt("timestamp")));
            }
        } catch (SQLException e) {
            LogDatabaseHelper.handleSqlException(e);
        } finally {
            LogDatabaseHelper.closeSilently(preparedStatement);
            LogDatabaseHelper.closeSilently(sqlConnection);
            LogDatabaseHelper.closeSilently(resultSet);
        }

        return chatlogs;
    }

    public static List<RoomVisitLogEntry> getLastRoomVisits(int playerId, int count) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<RoomVisitLogEntry> roomVisits = new ArrayList<>();

        try {
            sqlConnection = LogDatabaseHelper.getConnection();

            preparedStatement = LogDatabaseHelper.prepare("SELECT * FROM player_room_visits WHERE player_id = ? ORDER BY time_enter DESC LIMIT ?", sqlConnection);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, count);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                roomVisits.add(new RoomVisitLogEntry(resultSet.getInt("id"), playerId, resultSet.getInt("room_id"), resultSet.getInt("time_enter"), resultSet.getInt("time_exit")));
            }
        } catch (SQLException e) {
            LogDatabaseHelper.handleSqlException(e);
        } finally {
            LogDatabaseHelper.closeSilently(preparedStatement);
            LogDatabaseHelper.closeSilently(sqlConnection);
            LogDatabaseHelper.closeSilently(resultSet);
        }

        return roomVisits;
    }
}
