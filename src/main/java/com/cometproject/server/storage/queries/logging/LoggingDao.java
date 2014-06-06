package com.cometproject.server.storage.queries.logging;

import com.cometproject.server.logging.AbstractLogEntry;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LoggingDao {
    public static int saveLog(AbstractLogEntry entry) {

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into logs (`type`, `room_id`, `user_id`, `data`, `timestamp`) (?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setString(1, entry.getType().toString());
            preparedStatement.setInt(2, entry.getRoomId());
            preparedStatement.setInt(3, entry.getUserId());
            preparedStatement.setString(4, entry.getString());
            preparedStatement.setInt(5, entry.getTimestamp());

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }
}
