package com.cometproject.server.storage.queries.rooms;

import com.cometproject.server.game.rooms.models.types.StaticRoomModel;
import com.cometproject.server.game.rooms.types.RoomData;
import com.cometproject.server.game.rooms.types.misc.settings.RoomBanState;
import com.cometproject.server.game.rooms.types.misc.settings.RoomKickState;
import com.cometproject.server.game.rooms.types.misc.settings.RoomMuteState;
import com.cometproject.server.game.rooms.types.misc.settings.RoomTradeState;
import com.cometproject.server.storage.SqlHelper;
import com.google.common.collect.Lists;
import javolution.util.FastMap;
import javolution.util.FastSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoomDao {
    public static Set<StaticRoomModel> getModels() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Set<StaticRoomModel> data = new FastSet<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM room_models", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new StaticRoomModel(resultSet));
            }

        } catch (Exception e) {
            if(e instanceof SQLException)
                SqlHelper.handleSqlException(((SQLException) e));
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return data;
    }

    public static RoomData getRoomDataById(int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return new RoomData(resultSet);
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return null;
    }

    public static Map<Integer, RoomData> getRoomsByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, RoomData> rooms = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE owner_id = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rooms.put(resultSet.getInt("id"), new RoomData(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return rooms;
    }

    public static List<RoomData> getRoomsByQuery(String query) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<RoomData> rooms = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            if (query.startsWith("owner:")) {
                preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE owner = ?", sqlConnection);
                preparedStatement.setString(1, query.split("owner:")[1]);
            } else if(query.startsWith("tag:")) {
                preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE tags LIKE ? LIMIT 50", sqlConnection);

                String tagName = SqlHelper.escapeWildcards(query.split("tag:")[1]);
                preparedStatement.setString(1, "%" + tagName  + "%");
            } else {
                // escape wildcard characters
                query = SqlHelper.escapeWildcards(query);

                preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE name LIKE ? LIMIT 50", sqlConnection);
                preparedStatement.setString(1, query + "%");
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rooms.add(new RoomData(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return rooms;
    }

    public static int createRoom(String name, String model, String description, int category, int maxVisitors, RoomTradeState tradeState, int userId, String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("INSERT into rooms (`owner_id`, `owner`, `name`, `model`, `description`, `category`, `max_users`, `trade_state`) VALUES(?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, model);
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, category);
            preparedStatement.setInt(7, maxVisitors);
            preparedStatement.setString(8, tradeState.toString());

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

    public static void updateRoom(int roomId, String name, String description, int ownerId, String owner, int category, int maxUsers, String access,
                                  String password, int score, String tags, String decor, String model, boolean hideWalls, int thicknessWall,
                                  int thicknessFloor, boolean allowWalkthrough, boolean allowPets, String heightmap, RoomTradeState tradeState, RoomMuteState whoCanMute,
                                  RoomKickState whoCanKick, RoomBanState whoCanBan, int bubbleMode, int bubbleType, int bubbleScroll,
                                  int chatDistance, int antiFloodSettings) {

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("UPDATE rooms SET name = ?, description = ?, owner_id = ?, owner = ?, category = ?," +
                            " max_users = ?, access_type = ?, password = ?, score = ?, tags = ?, decorations = ?, model = ?, hide_walls = ?, thickness_wall = ?," +
                            " thickness_floor = ?, allow_walkthrough = ?, allow_pets = ?, heightmap = ?, mute_state = ?, ban_state = ?, kick_state = ?," +
                            "bubble_mode = ?, bubble_type = ?, bubble_scroll = ?, chat_distance = ?, flood_level = ?, trade_state = ? WHERE id = ?",
                    sqlConnection);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, ownerId);
            preparedStatement.setString(4, owner);
            preparedStatement.setInt(5, category);
            preparedStatement.setInt(6, maxUsers);
            preparedStatement.setString(7, access);
            preparedStatement.setString(8, password);
            preparedStatement.setInt(9, score);
            preparedStatement.setString(10, tags);
            preparedStatement.setString(11, decor);
            preparedStatement.setString(12, model);
            preparedStatement.setString(13, hideWalls ? "1" : "0");
            preparedStatement.setInt(14, thicknessWall);
            preparedStatement.setInt(15, thicknessFloor);
            preparedStatement.setString(16, allowWalkthrough ? "1" : "0");
            preparedStatement.setString(17, allowPets ? "1" : "0");
            preparedStatement.setString(18, heightmap);
            preparedStatement.setString(19, whoCanMute.toString());
            preparedStatement.setString(20, whoCanBan.toString());
            preparedStatement.setString(21, whoCanKick.toString());
            preparedStatement.setInt(22, bubbleMode);
            preparedStatement.setInt(23, bubbleType);
            preparedStatement.setInt(24, bubbleScroll);
            preparedStatement.setInt(25, chatDistance);
            preparedStatement.setInt(26, antiFloodSettings);
            preparedStatement.setString(27, tradeState.toString());

            preparedStatement.setInt(28, roomId);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void deleteRoom(int roomId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("DELETE FROM rooms WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static List<RoomData> getHighestScoredRooms() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<RoomData> roomData = Lists.newArrayList();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms ORDER by score DESC LIMIT 50", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                roomData.add(new RoomData(resultSet));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return roomData;
    }

}
