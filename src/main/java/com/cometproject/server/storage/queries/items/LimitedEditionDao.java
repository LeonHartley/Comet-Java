package com.cometproject.server.storage.queries.items;

import com.cometproject.server.game.items.rares.LimitedEditionItem;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LimitedEditionDao {
    public static void save(LimitedEditionItem item) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT INTO items_limited (`item_id`, `limited_rare`, `limited_rare_total`) (?, ?, ?);", sqlConnection);
            preparedStatement.setInt(1, item.getItemId());
            preparedStatement.setInt(2, item.getLimitedRare());
            preparedStatement.setInt(3, item.getLimitedRareTotal());

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static LimitedEditionItem get(int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM items_limited WHERE id_one = ? LIMIT 1;", sqlConnection);
            preparedStatement.setInt(1, itemId);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new LimitedEditionItem(itemId, resultSet.getInt("limited_rare"), resultSet.getInt("limited_rare_total"));
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
}
