package com.cometproject.server.storage.queries.bots;

import com.cometproject.server.game.players.components.types.InventoryBot;
import com.cometproject.server.storage.SqlHelper;
import javolution.util.FastMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PlayerBotDao {
    public static Map<Integer, InventoryBot> getBotsByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, InventoryBot> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM bots WHERE owner_id = ? AND room_id = 0", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getInt("id"), new InventoryBot(resultSet));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static int createBot(int playerId, String name, String figure, String gender, String motto) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into bots (`owner_id`, `room_id`, `name`, `figure`, `gender`, `motto`, `x`, `y`, `z`, `messages`, `automatic_chat`, `chat_delay`) VALUES(" +
                    "?, 0, ?, ?, ?, ?, 0, 0, 0, '[]', '1', '14');", sqlConnection, true);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, figure);
            preparedStatement.setString(4, gender);
            preparedStatement.setString(5, motto);

            preparedStatement.execute();

            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return 0;
    }
}
