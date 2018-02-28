package com.cometproject.server.storage.queries.rooms;

import com.cometproject.api.game.rooms.IRoomData;
import com.cometproject.api.game.rooms.RoomType;
import com.cometproject.api.game.rooms.settings.*;
import com.cometproject.api.utilities.JsonUtil;
import com.cometproject.server.boot.Comet;
import com.cometproject.api.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.models.types.StaticRoomModel;
import com.cometproject.server.game.rooms.types.RoomPromotion;
import com.cometproject.server.storage.SqlHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.map.ListOrderedMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoomDao {
    public static Map<String, StaticRoomModel> getModels() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<String, StaticRoomModel> data = new HashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM room_models", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("id"), new StaticRoomModel(resultSet));
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

    public static IRoomData getRoomDataById(int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE id = ? LIMIT 1", sqlConnection);
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return roomDataFromResultSet(resultSet);
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

    public static Map<Integer, IRoomData> getRoomsByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, IRoomData> rooms = new ListOrderedMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE owner_id = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rooms.put(resultSet.getInt("id"), roomDataFromResultSet(resultSet));
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

    public static Map<Integer, IRoomData> getRoomsWithRightsByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, IRoomData> rooms = new ListOrderedMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE id IN (SELECT room_id FROM room_rights WHERE player_id = ?) AND group_id = 0 ORDER BY SUBSTRING(users_now FROM 1 FOR 1) DESC, name ASC LIMIT 50", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rooms.put(resultSet.getInt("id"), roomDataFromResultSet(resultSet));
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

    public static List<IRoomData> getRoomsByQuery(String query) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<IRoomData> rooms = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            if (query.equals("owner:")) return rooms;

            if (query.startsWith("owner:")) {
                preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE owner = ? ORDER BY name ASC", sqlConnection);
                preparedStatement.setString(1, query.split("owner:")[1]);
            } else if (query.startsWith("tag:")) {
                preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE tags LIKE ? ORDER BY SUBSTRING(users_now FROM 1 FOR 1) DESC, name ASC LIMIT 50", sqlConnection);

                String tagName = SqlHelper.escapeWildcards(query.split("tag:")[1]);
                preparedStatement.setString(1, tagName + "%");
            } else if (query.startsWith("group:")) {
                preparedStatement = SqlHelper.prepare("SELECT * FROM rooms WHERE group_id IN (SELECT id FROM groups WHERE name LIKE ?) ORDER BY SUBSTRING(users_now FROM 1 FOR 1) DESC, name ASC LIMIT 50", sqlConnection);

                String groupName = SqlHelper.escapeWildcards(query.split("group:")[1]);
                preparedStatement.setString(1, groupName + "%");
            } else {
                // escape wildcard characters
                query = SqlHelper.escapeWildcards(query);

                preparedStatement = SqlHelper.prepare("(SELECT * FROM rooms WHERE owner = ? ORDER BY users_now DESC LIMIT 50) UNION (SELECT * FROM rooms WHERE name LIKE ? ORDER BY SUBSTRING(users_now FROM 1 FOR 1) DESC, name ASC LIMIT 50)", sqlConnection);
                preparedStatement.setString(1, query);
                preparedStatement.setString(2, query + "%");
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                rooms.add(roomDataFromResultSet(resultSet));
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

    public static int createRoom(String name, CustomFloorMapData model, String description, int category, int maxVisitors, RoomTradeState tradeState, int userId, String username, int wallThickness, int floorThickness, String decorations, boolean hideWalls) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("INSERT into rooms (`owner_id`, `owner`, `name`, `heightmap`, `description`, `category`, `max_users`, `trade_state`, `thickness_wall`, `thickness_floor`, `decorations`, `hide_walls`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, JsonUtil.getInstance().toJson(model));
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, category);
            preparedStatement.setInt(7, maxVisitors);
            preparedStatement.setString(8, tradeState.toString());
            preparedStatement.setInt(9, wallThickness);
            preparedStatement.setInt(10, floorThickness);
            preparedStatement.setString(11, decorations);
            preparedStatement.setString(12, hideWalls ? "1" : "0");

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

    public static int createRoom(String name, String model, String description, int category, int maxVisitors, RoomTradeState tradeState, int userId, String username) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("INSERT into rooms (`owner_id`, `owner`, `name`, `model`, `description`, `category`, `max_users`, `trade_state`, `thickness_floor`, `thickness_wall`) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, model);
            preparedStatement.setString(5, description);
            preparedStatement.setInt(6, category);
            preparedStatement.setInt(7, maxVisitors);
            preparedStatement.setString(8, tradeState.toString());
            preparedStatement.setInt(9, 0);
            preparedStatement.setInt(10, 0);

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

    public static void updateRoom(int roomId, String name, String description, int ownerId, String owner, int category, int maxUsers, RoomAccessType access,
                                  String password, int score, String tags, String decor, String model, boolean hideWalls, int thicknessWall,
                                  int thicknessFloor, boolean allowWalkthrough, boolean allowPets, String heightmap, RoomTradeState tradeState, RoomMuteState whoCanMute,
                                  RoomKickState whoCanKick, RoomBanState whoCanBan, int bubbleMode, int bubbleType, int bubbleScroll,
                                  int chatDistance, int antiFloodSettings, String disabledCommands, int groupId, String requiredBadge, String thumbnail, boolean hideWired) {

        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("UPDATE rooms SET name = ?, description = ?, owner_id = ?, owner = ?, category = ?," +
                            " max_users = ?, access_type = ?, password = ?, score = ?, tags = ?, decorations = ?, model = ?, hide_walls = ?, thickness_wall = ?," +
                            " thickness_floor = ?, allow_walkthrough = ?, allow_pets = ?, heightmap = ?, mute_state = ?, ban_state = ?, kick_state = ?," +
                            "bubble_mode = ?, bubble_type = ?, bubble_scroll = ?, chat_distance = ?, flood_level = ?, trade_state = ?, disabled_commands = ?, group_id = ?, required_badge = ?, thumbnail = ?, hide_wired = ? WHERE id = ?",
                    sqlConnection);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, description);
            preparedStatement.setInt(3, ownerId);
            preparedStatement.setString(4, owner);
            preparedStatement.setInt(5, category);
            preparedStatement.setInt(6, maxUsers);
            preparedStatement.setString(7, access.toString().toLowerCase());
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
            preparedStatement.setString(28, disabledCommands);
            preparedStatement.setInt(29, groupId);
            preparedStatement.setString(30, requiredBadge);
            preparedStatement.setString(31, thumbnail);
            preparedStatement.setString(32, hideWired ? "1" : "0");

            preparedStatement.setInt(33, roomId);

            preparedStatement.execute();
        } catch (SQLException e) {
            Comet.getServer().getLogger().error("Failed to save room data" + (e.getMessage().contains("access_type") ? " - Access type: " + access.toString().toLowerCase() : ""));
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

    public static List<IRoomData> getHighestScoredRooms() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<IRoomData> roomData = Lists.newArrayList();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms ORDER by score DESC LIMIT 50", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                roomData.add(roomDataFromResultSet(resultSet));
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

    public static void getActivePromotions(Map<Integer, RoomPromotion> roomPromotions) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM rooms_promoted WHERE time_expire > " + Comet.getTime(), sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                roomPromotions.put(resultSet.getInt("room_id"), new RoomPromotion(resultSet.getInt("room_id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getLong("time_start"), resultSet.getLong("time_expire")));
            }

        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void updatePromotedRoom(RoomPromotion roomPromotion) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE rooms_promoted SET name = ?, description = ?, time_expire = ? WHERE room_id = ?", sqlConnection);

            preparedStatement.setString(1, roomPromotion.getPromotionName());
            preparedStatement.setString(2, roomPromotion.getPromotionDescription());

            preparedStatement.setLong(3, roomPromotion.getTimestampFinish());
            preparedStatement.setInt(4, roomPromotion.getRoomId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void createPromotedRoom(RoomPromotion roomPromotion) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into rooms_promoted (room_id, name, description, time_start, time_expire) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE name = VALUES(name), " +
                    "description = VALUES(description), time_start = VALUES(time_start), time_expire = VALUES(time_expire);", sqlConnection);

            preparedStatement.setInt(1, roomPromotion.getRoomId());
            preparedStatement.setString(2, roomPromotion.getPromotionName());
            preparedStatement.setString(3, roomPromotion.getPromotionDescription());
            preparedStatement.setLong(4, roomPromotion.getTimestampStart());
            preparedStatement.setLong(5, roomPromotion.getTimestampFinish());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void deleteExpiredRoomPromotions() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM rooms_promoted WHERE time_expire < " + Comet.getTime(), sqlConnection);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveUserCounts(Map<Integer, Integer> roomStatuses) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE `rooms` SET users_now = ? WHERE `id` = ?;", sqlConnection);

            for (Map.Entry<Integer, Integer> room : roomStatuses.entrySet()) {
                preparedStatement.setInt(1, room.getValue());
                preparedStatement.setInt(2, room.getKey());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveUserCount(int roomId, int userCount) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE `rooms` SET users_now = ? WHERE `id` = ?", sqlConnection);

            preparedStatement.setInt(1, userCount);
            preparedStatement.setInt(2, roomId);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    private static IRoomData roomDataFromResultSet(final ResultSet room) throws SQLException {
        final int id = room.getInt("id");
        final RoomType type = RoomType.valueOf(room.getString("type"));
        final String name = room.getString("name");
        final String description = room.getString("description");
        final int ownerId = room.getInt("owner_id");
        final String owner = room.getString("owner");
        final int category = room.getInt("category");
        final int maxUsers = room.getInt("max_users");
        final String thumbnail = room.getString("thumbnail");

        String accessTypeString = room.getString("access_type");

        if (!accessTypeString.equals("open") && !accessTypeString.equals("doorbell") && !accessTypeString.equals("password")) {
            accessTypeString = "open";
        }

        final String password = room.getString("password");
        final RoomAccessType access = RoomAccessType.valueOf(accessTypeString.toUpperCase());
        final String originalPassword = password;

        final int score = room.getInt("score");

        final String[] tags = room.getString("tags").isEmpty() ? new String[0] :
                room.getString("tags").split(",");

        final Map<String, String> decorations = new HashMap<>();

        String[] decorationsArray = room.getString("decorations").split(",");

        fillDecorations(decorations, decorationsArray);

        final String model = room.getString("model");

        final boolean hideWalls = room.getString("hide_walls").equals("1");
        final int thicknessWall = room.getInt("thickness_wall");
        final int thicknessFloor = room.getInt("thickness_floor");
        final boolean allowWalkthrough = room.getString("allow_walkthrough").equals("1");
        final boolean allowPets = room.getString("allow_pets").equals("1");
        final String heightmap = room.getString("heightmap");
        final RoomTradeState tradeState = RoomTradeState.valueOf(room.getString("trade_state"));

        final RoomKickState kickState = RoomKickState.valueOf(room.getString("kick_state"));
        final RoomBanState banState = RoomBanState.valueOf(room.getString("ban_state"));
        final RoomMuteState muteState = RoomMuteState.valueOf(room.getString("mute_state"));

        final int bubbleMode = room.getInt("bubble_mode");
        final int bubbleScroll = room.getInt("bubble_scroll");
        final int bubbleType = room.getInt("bubble_type");
        final int antiFloodSettings = room.getInt("flood_level");
        final int chatDistance = room.getInt("chat_distance");

        final List<String> disabledCommands = Lists.newArrayList(room.getString("disabled_commands").split(","));
        final int groupId = room.getInt("group_id");
        final String requiredBadge = room.getString("required_badge");
        final boolean wiredHidden = room.getBoolean("hide_wired");

        return null;//new RoomData(id, type, name, description, ownerId, owner, category, maxUsers, access, password,
//                originalPassword, tradeState, score, tags, decorations, model, hideWalls, thicknessWall, thicknessFloor,
//                allowWalkthrough, allowPets, heightmap, muteState, kickState, banState, bubbleMode, bubbleType,
//                bubbleScroll, chatDistance, antiFloodSettings, disabledCommands, groupId, System.currentTimeMillis(),
//                requiredBadge, thumbnail, wiredHidden);
    }

    private static void fillDecorations(Map<String, String> decorations, String[] decorationsArray) {
        for (int i = 0; i < decorationsArray.length; i++) {
            String[] decoration = decorationsArray[i].split("=");

            if (decoration.length == 2)
                decorations.put(decoration[0], decoration[1]);
        }
    }
}
