package com.cometproject.server.storage.queries.bots;

import com.cometproject.api.game.bots.BotMode;
import com.cometproject.api.game.bots.BotType;
import com.cometproject.api.game.bots.IBotData;
import com.cometproject.api.game.utilities.Position;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.game.bots.BotData;
import com.cometproject.server.game.rooms.objects.entities.types.data.PlayerBotData;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RoomBotDao {
    public static List<IBotData> getBotsByRoomId(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<IBotData> data = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM bots WHERE room_id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int id = resultSet.getInt("id");
                final String username = resultSet.getString("name");
                final String motto = resultSet.getString("motto");
                final String figure = resultSet.getString("figure");
                final String gender = resultSet.getString("gender");
                final String ownerName = resultSet.getString("owner");
                final int ownerId = resultSet.getInt("owner_id");
                final String messages = resultSet.getString("messages");
                final boolean automaticChat = resultSet.getBoolean("automatic_chat");
                final int chatDelay = resultSet.getInt("chat_delay");
                final BotType botType = BotType.valueOf(resultSet.getString("type").toUpperCase());
                final BotMode mode = BotMode.valueOf(resultSet.getString("mode").toUpperCase());
                final String storedData = resultSet.getString("data");

                PlayerBotData botData = new PlayerBotData(id, username, motto, figure, gender, ownerName, ownerId, messages, automaticChat, chatDelay, botType, mode, storedData);
                botData.setPosition(new Position(resultSet.getInt("x"), resultSet.getInt("y")));

                data.add(botData);
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

    public static void setRoomId(int roomId, int botId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE bots SET room_id = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);
            preparedStatement.setInt(2, botId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveData(BotData data) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE bots SET figure = ?, gender = ?, motto = ?, name = ?, messages = ?, automatic_chat = ?, chat_delay = ?, mode = ?, data = ? WHERE id = ?", sqlConnection);

            preparedStatement.setString(1, data.getFigure());
            preparedStatement.setString(2, data.getGender());
            preparedStatement.setString(3, data.getMotto());
            preparedStatement.setString(4, data.getUsername());
            preparedStatement.setString(5, JsonUtil.getInstance().toJson(data.getMessages()));
            preparedStatement.setString(6, data.isAutomaticChat() ? "1" : "0");
            preparedStatement.setInt(7, data.getChatDelay());
            preparedStatement.setString(8, data.getMode().toString());
            preparedStatement.setString(9, data.getData());

            preparedStatement.setInt(10, data.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void savePosition(int x, int y, double height, int botId, int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE bots SET x = ?, y = ?, z = ?, room_id = ? WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setDouble(3, height);
            preparedStatement.setInt(4, roomId);
            preparedStatement.setInt(5, botId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
