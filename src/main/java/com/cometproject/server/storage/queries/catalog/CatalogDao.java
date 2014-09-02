package com.cometproject.server.storage.queries.catalog;

import com.cometproject.server.game.CometManager;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.storage.SqlHelper;
import javolution.util.FastMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class CatalogDao {
    public static void getPages(Map<Integer, CatalogPage> pages) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_pages WHERE visible = '1' ORDER BY order_num", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                pages.put(resultSet.getInt("id"), new CatalogPage(resultSet, getItemsByPage(resultSet.getInt("id"))));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    private static FastMap<Integer, CatalogItem> getItemsByPage(int pageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        FastMap<Integer, CatalogItem> data = new FastMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_items WHERE page_id = ?", sqlConnection);
            preparedStatement.setInt(1, pageId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    if (!CometManager.getItems().getItemDefinitions().containsKey(Integer.parseInt(resultSet.getString("item_ids")))) {
                        //System.out.println("Ignoring catalog item: " + resultSet.getInt("id"));
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }

                data.put(resultSet.getInt("id"), new CatalogItem(resultSet));
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

    public static void updateLimitSellsForItem(int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE catalog_items SET limited_sells = limited_sells + 1 WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
