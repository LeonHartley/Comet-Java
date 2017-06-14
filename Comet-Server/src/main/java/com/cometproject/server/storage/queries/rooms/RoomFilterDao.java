package com.cometproject.server.storage.queries.rooms;

import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.utilities.collections.ConcurrentHashSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class RoomFilterDao {
    public static Set<String> getFilterForRoom(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Set<String> data = new ConcurrentHashSet<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `word` FROM room_word_filter WHERE room_id = ?;", sqlConnection);
            preparedStatement.setInt(1, roomId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(resultSet.getString("word"));
            }
        } catch (Exception e) {
            if (e instanceof SQLException)
                SqlHelper.handleSqlException(((SQLException) e));
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static void saveWord(String word, int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into room_word_filter (`word`, `room_id`) VALUES(?, ?);", sqlConnection);
            preparedStatement.setString(1, word);
            preparedStatement.setInt(2, roomId);

            preparedStatement.execute();
        } catch (Exception e) {
            if (e instanceof SQLException)
                SqlHelper.handleSqlException(((SQLException) e));
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void removeWord(String word, int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM room_word_filter WHERE word = ? AND room_id = ?;", sqlConnection);
            preparedStatement.setString(1, word);
            preparedStatement.setInt(2, roomId);

            preparedStatement.execute();
        } catch (Exception e) {
            if (e instanceof SQLException)
                SqlHelper.handleSqlException(((SQLException) e));
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
