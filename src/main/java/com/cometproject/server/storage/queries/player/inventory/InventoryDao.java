package com.cometproject.server.storage.queries.player.inventory;

import com.cometproject.server.game.players.components.types.InventoryItem;
import com.cometproject.server.storage.SqlHelper;
import javolution.util.FastMap;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class InventoryDao {
    public static String ITEMS_USERID_INDEX = "";
    private static Logger log = Logger.getLogger(InventoryDao.class.getName());

    public static Map<Integer, InventoryItem> getInventoryByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, InventoryItem> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = ITEMS_USERID_INDEX.equals("") ?
                    SqlHelper.prepare("SELECT * FROM items WHERE room_id = 0 AND user_id = ?", sqlConnection)
                    : SqlHelper.prepare("SELECT * FROM items USE INDEX (" + ITEMS_USERID_INDEX + ") WHERE room_id = 0 AND user_id = ?", sqlConnection);

            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                InventoryItem inventoryItem = new InventoryItem(resultSet);

                if(inventoryItem.getDefinition() != null) {
                    data.put(resultSet.getInt("id"), inventoryItem);
                } else {
                    log.warn("InventoryItem: " + inventoryItem.getId() + " with invalid definition ID: " + inventoryItem.getBaseId());
                }
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

    public static Map<String, Integer> getWornBadgesByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<String, Integer> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM player_badges WHERE player_id = ? AND slot != 0 LIMIT 5", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("badge_code"), resultSet.getInt("slot"));
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

    public static Map<String, Integer> getBadgesByPlayerId(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<String, Integer> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM player_badges WHERE player_id = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getString("badge_code"), resultSet.getInt("slot"));
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

    public static void addBadge(String badge, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT INTO player_badges (`player_id`, `badge_code`) VALUES (?, ?)", sqlConnection);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, badge);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void addBadges(String badge, List<Integer> playerIds) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT INTO player_badges (`player_id`, `badge_code`) VALUES (?, ?)", sqlConnection);

            for (Integer playerId : playerIds) {
                preparedStatement.setInt(1, playerId);
                preparedStatement.setString(2, badge);

                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);

            playerIds.clear();
        }
    }

    public static void removeBadge(String badge, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM player_badges WHERE player_id = ? AND badge_code = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, badge);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void updateBadge(String badge, int slot, int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE player_badges SET slot = ? WHERE badge_code = ? AND player_id = ?", sqlConnection);
            preparedStatement.setInt(1, slot);
            preparedStatement.setString(2, badge);
            preparedStatement.setInt(3, playerId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void clearInventory(int userId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("DELETE FROM items WHERE user_id = ? AND room_id = 0", sqlConnection);
            preparedStatement.setInt(1, userId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
