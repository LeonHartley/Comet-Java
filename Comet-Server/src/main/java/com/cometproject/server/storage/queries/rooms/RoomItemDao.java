package com.cometproject.server.storage.queries.rooms;

import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.items.rares.LimitedEditionItem;
import com.cometproject.server.game.rooms.objects.items.RoomItem;
import com.cometproject.server.game.rooms.objects.items.RoomItemFactory;
import com.cometproject.server.game.rooms.objects.items.RoomItemFloor;
import com.cometproject.server.game.rooms.objects.items.RoomItemWall;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.storage.SqlHelper;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class RoomItemDao {

    private static Logger log = Logger.getLogger(RoomItemDao.class.getName());

    public static void getItems(Room room, Map<Integer, RoomItemFloor> floorItems, Map<Integer, RoomItemWall> wallItems) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT i.*, ltd.limited_id, ltd.limited_total FROM items i LEFT JOIN items_limited_edition ltd ON ltd.item_id = i.id WHERE i.room_id = ?", sqlConnection);
            preparedStatement.setInt(1, room.getId());

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LimitedEditionItem limitedEditionItem = null;

                if (resultSet.getInt("limited_id") != 0) {
                    limitedEditionItem = new LimitedEditionItem(resultSet.getInt("id"), resultSet.getInt("limited_id"), resultSet.getInt("limited_total"));
                }

                try {
                    if (ItemManager.getInstance().getDefinition(resultSet.getInt("base_item")) != null) {
                        if (ItemManager.getInstance().getDefinition(resultSet.getInt("base_item")).getType().equals("s"))
                            floorItems.put(resultSet.getInt("id"), RoomItemFactory.createFloor(resultSet.getInt("id"), resultSet.getInt("base_item"), room, resultSet.getInt("user_id"), resultSet.getInt("x"), resultSet.getInt("y"), resultSet.getDouble("z"), resultSet.getInt("rot"), resultSet.getString("extra_data"), limitedEditionItem));
                        else
                            wallItems.put(resultSet.getInt("id"), RoomItemFactory.createWall(resultSet.getInt("id"), resultSet.getInt("base_item"), room, resultSet.getInt("user_id"), resultSet.getString("wall_pos"), resultSet.getString("extra_data"), limitedEditionItem));

                    } else {
                        log.warn("Item (" + resultSet.getInt("base_item") + ") with invalid definition ID: " + resultSet.getInt("base_item"));
                    }
                } catch(Exception e) {
                }
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

    public static void setBaseItem(int itemId, int baseId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE items set base_item = ? WHERE id = ?", sqlConnection);

            preparedStatement.setInt(1, baseId);
            preparedStatement.setInt(2, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void processBatch(List<RoomItem> itemsToStore) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            sqlConnection.setAutoCommit(false);

            preparedStatement = SqlHelper.prepare("UPDATE items SET extra_data = ? WHERE id = ?", sqlConnection);

            for (RoomItem roomItem : itemsToStore) {
                String data;

                if (roomItem instanceof RoomItemFloor) {
                    data = ((RoomItemFloor) roomItem).getDataObject();
                } else {
                    data = roomItem.getExtraData();
                }

                preparedStatement.setString(1, data);
                preparedStatement.setInt(2, roomItem.getId());

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            sqlConnection.commit();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveItem(RoomItemFloor floor) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE items SET x = ?, y = ?, z = ?, rot = ?, extra_data = ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, floor.getPosition().getX());
            preparedStatement.setInt(2, floor.getPosition().getY());
            preparedStatement.setDouble(3, floor.getPosition().getZ());
            preparedStatement.setInt(4, floor.getRotation());
            preparedStatement.setString(5, floor.getDataObject());
            preparedStatement.setInt(6, floor.getId());

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
