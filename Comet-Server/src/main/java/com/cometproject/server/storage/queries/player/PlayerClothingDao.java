package com.cometproject.server.storage.queries.player;

import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;

public class PlayerClothingDao {

    public static void getClothing(final int playerId, Set<String> clothingItems) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM player_clothing WHERE player_id = ?", sqlConnection);
            preparedStatement.setInt(1, playerId);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                clothingItems.add(resultSet.getString("item_name"));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void redeemClothing(final int playerId, String clothingItem) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into player_clothing (player_id, item_name) VALUES(?, ?); ", sqlConnection);
            preparedStatement.setInt(1, playerId);
            preparedStatement.setString(2, clothingItem);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
