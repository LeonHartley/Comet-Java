package com.cometproject.server.storage.queries.rooms;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.rooms.items.RoomItemFactory;
import com.cometproject.server.game.rooms.items.RoomItemFloor;
import com.cometproject.server.game.rooms.items.RoomItemWall;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

public class RoomItemDao {

    public static void getItems(int roomId, Collection<RoomItemFloor> floorItems, Collection<RoomItemWall> wallItems) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM items WHERE room_id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (CometManager.getItems().getDefinition(resultSet.getInt("base_item")).getType().equals("s"))
                    floorItems.add(RoomItemFactory.createFloor(resultSet.getInt("id"), resultSet.getInt("base_item"), roomId, resultSet.getInt("user_id"), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getDouble("z"), resultSet.getInt("rot"), resultSet.getString("extra_data")));
                else
                    wallItems.add(RoomItemFactory.createWall(resultSet.getInt("id"), resultSet.getInt("base_item"), roomId, resultSet.getInt("user_id"), resultSet.getString("wall_pos"), resultSet.getString("extra_data")));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void removeItemFromRoom(int itemId, int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE items SET room_id = 0, user_id = ?, x = 0, y = 0, z = 0, wall_pos = '' WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void deleteItem(int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM items WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveData(int itemId, String data) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE items SET extra_data = ? WHERE id = ?", sqlConnection);
            preparedStatement.setString(1, data);
            preparedStatement.setInt(2, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static int getRoomIdById(int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT `room_id` FROM items WHERE id = ? LIMIT 1;", sqlConnection);
            preparedStatement.setInt(1, itemId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("room_id");
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

    public static void saveItemPosition(int x, int y, double height, int rotation, int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE items SET x = ?, y = ?, z = ?, rot = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setDouble(3, height);
            preparedStatement.setInt(4, rotation);
            preparedStatement.setInt(5, id);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void placeFloorItem(int roomId, int x, int y, double height, int rot, String data, int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE items SET x = ?, y = ?, z = ?, rot = ?, room_id = ?, extra_data = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, x);
            preparedStatement.setInt(2, y);
            preparedStatement.setDouble(3, height);
            preparedStatement.setInt(4, rot);
            preparedStatement.setInt(5, roomId);
            preparedStatement.setString(6, data);
            preparedStatement.setInt(7, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void placeWallItem(int roomId, String wallPos, String data, int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE items SET room_id = ?, wall_pos = ?, extra_data = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, roomId);

            preparedStatement.setString(2, wallPos);
            preparedStatement.setString(3, data);
            preparedStatement.setInt(4, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
