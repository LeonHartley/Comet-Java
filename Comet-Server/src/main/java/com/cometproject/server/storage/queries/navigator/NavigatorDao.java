package com.cometproject.server.storage.queries.navigator;

import com.cometproject.server.game.navigator.types.Category;
import com.cometproject.server.game.navigator.types.categories.NavigatorViewMode;
import com.cometproject.server.game.navigator.types.featured.FeaturedRoom;
import com.cometproject.server.storage.SqlHelper;
import org.apache.commons.collections4.map.ListOrderedMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NavigatorDao {
    public static List<FeaturedRoom> getFeaturedRooms() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<FeaturedRoom> data = new ArrayList<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM navigator_featured_rooms WHERE enabled = '1' ORDER BY order_num", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.add(new FeaturedRoom(resultSet));
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

    public static Map<Integer, Category> getCategories() {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, Category> data = new ListOrderedMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM navigator_categories WHERE enabled = '1' ORDER BY order_id ASC", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                data.put(resultSet.getInt("id"), new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("category"),
                        resultSet.getString("category_identifier"),
                        resultSet.getString("public_name"),
                        true,
                        -1,
                        resultSet.getInt("required_rank"),
                        NavigatorViewMode.valueOf(resultSet.getString("view_mode").toUpperCase()),
                        resultSet.getString("category_type"),
                        resultSet.getString("search_allowance"),
                        resultSet.getInt("order_id")
                ));
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

    public static void disableFeaturedRoom(int id) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE navigator_featured_rooms SET enabled = '0' WHERE room_id = ?", sqlConnection);
            preparedStatement.setInt(1, id);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static int staffPick(int roomId, String name, String description) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();
            preparedStatement = SqlHelper.prepare("INSERT into navigator_featured_rooms VALUES(null, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", sqlConnection, true);

            preparedStatement.setString(1, "small");
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, description);
            preparedStatement.setString(4, "");
            preparedStatement.setString(5, "internal");
            preparedStatement.setInt(6, roomId);
            preparedStatement.setInt(7, 1); // ID OF STAFF PICKED ROOMS CATEGORY!!!
            preparedStatement.setString(8, "1");
            preparedStatement.setString(9, "0"); // recommended
            preparedStatement.setString(10, "room");

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
