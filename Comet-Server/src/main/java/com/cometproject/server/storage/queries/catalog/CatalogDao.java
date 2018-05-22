package com.cometproject.server.storage.queries.catalog;

import com.cometproject.api.game.catalog.types.ICatalogFrontPageEntry;
import com.cometproject.api.game.catalog.types.ICatalogItem;
import com.cometproject.api.game.catalog.types.ICatalogPage;
import com.cometproject.api.game.catalog.types.IClothingItem;
import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.catalog.types.CatalogFrontPageEntry;
import com.cometproject.server.game.catalog.types.CatalogItem;
import com.cometproject.server.game.catalog.types.CatalogPage;
import com.cometproject.server.game.catalog.types.ClothingItem;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class CatalogDao {
    public static void getPages(Map<Integer, ICatalogPage> pages) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_pages WHERE visible = '1' ORDER BY order_num;", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    int pageId = resultSet.getInt("id");
                    pages.put(pageId, new CatalogPage(resultSet, CatalogManager.getInstance().getItemsForPage(pageId)));
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Comet.getServer().getLogger().warn("Failed to load catalog page: " + resultSet.getInt("id"));
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

    public static void getItems(Map<Integer, ICatalogItem> items) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_items", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                try {
                    final ICatalogItem catalogItem = itemFromResultSet(resultSet);

                    if (!catalogItem.getItemId().equals("-1") && catalogItem.getItems().size() == 0) {
                        Comet.getServer().getLogger().warn(String.format("Catalog Item with ID: %s and name: %s has invalid item data! (Data: %s)", catalogItem.getId(), catalogItem.getDisplayName(), catalogItem.getItemId()));
                        continue;
                    }

                    items.put(resultSet.getInt("id"), catalogItem);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Comet.getServer().getLogger().warn("Failed to load catalog item: " + resultSet.getString("id"));
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


    private static Map<Integer, ICatalogItem> getItemsByPage(int pageId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<Integer, ICatalogItem> data = new HashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_items WHERE page_id = ?", sqlConnection);
            preparedStatement.setInt(1, pageId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
//                try {
//                    int itemId = Integer.parseInt(resultSet.getString("item_ids"));
//
//                    if (itemId != -1 && !ItemManager.getInstance().getItemDefinitions().containsKey(itemId)) {
//                        continue;
//                    }
//                } catch (Exception e) {
//                    continue;
//                }

                try {
                    final ICatalogItem catalogItem = itemFromResultSet(resultSet);

                    if (!catalogItem.getItemId().equals("-1") && catalogItem.getItems().size() == 0) {
                        Comet.getServer().getLogger().warn(String.format("Catalog Item with ID: %s and name: %s has invalid item data! (Data: %s)", catalogItem.getId(), catalogItem.getDisplayName(), catalogItem.getItemId()));
                        continue;
                    }

                    data.put(resultSet.getInt("id"), catalogItem);
                } catch (Exception e) {
                    Comet.getServer().getLogger().warn("Error while loading catalog item: " + resultSet.getInt("id"));
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

    public static void updateLimitSellsForItem(int itemId, int amount) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("UPDATE catalog_items SET limited_sells = limited_sells + ? WHERE id = ?", sqlConnection);
            preparedStatement.setInt(1, amount);
            preparedStatement.setInt(2, itemId);

            SqlHelper.executeStatementSilently(preparedStatement, false);
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void loadGiftBoxes(List<Integer> giftBoxesOld, List<Integer> giftBoxesNew) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_gift_wrapping", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                if (resultSet.getString("type").equals("old")) {
                    giftBoxesOld.add(resultSet.getInt("sprite_id"));
                } else {
                    giftBoxesNew.add(resultSet.getInt("sprite_id"));
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

    public static void getFeaturedPages(List<ICatalogFrontPageEntry> frontPageEntries) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_featured_pages", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                frontPageEntries.add(new CatalogFrontPageEntry(resultSet.getInt("id"), resultSet.getString("caption"),
                        resultSet.getString("image"), resultSet.getString("page_link"), resultSet.getInt("page_id")));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void getClothing(Map<String, IClothingItem> clothingItems) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM catalog_clothing", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final String itemsStr = resultSet.getString("clothing_items").replace(" ", "");

                if (itemsStr.equals("")) {
                    continue;
                }

                final String itemName = resultSet.getString("item_name");
                final String[] itemsStrArray = itemsStr.split(",");
                int[] items = new int[itemsStrArray.length];

                for (int i = 0; i < itemsStrArray.length; i++) {
                    items[i] = Integer.parseInt(itemsStrArray[i]);
                }

                clothingItems.put(itemName, new ClothingItem(itemName, items));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static void saveRecentPurchase(final int playerId, final int catalogItem, final int amount, final String data) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into `player_recent_purchases` (player_id, catalog_item, amount, data) VALUES(?, ?, ?, ?);", sqlConnection);

            preparedStatement.setInt(1, playerId);
            preparedStatement.setInt(2, catalogItem);
            preparedStatement.setInt(3, amount);
            preparedStatement.setString(4, data);

            preparedStatement.execute();
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }

    public static Set<Integer> findRecentPurchases(final int count, final int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        final Set<Integer> recentPurchases = new HashSet<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT DISTINCT `catalog_item` FROM player_recent_purchases WHERE player_id = ? LIMIT " + count, sqlConnection);
            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                final int catalogItemId = resultSet.getInt("catalog_item");

                recentPurchases.add(catalogItemId);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return recentPurchases;
    }

    private static ICatalogItem itemFromResultSet(ResultSet resultSet) throws SQLException {
        final int id = resultSet.getInt("id");
        final String itemIds = resultSet.getString("item_ids");
        final String catalogName = resultSet.getString("catalog_name");
        final int costCredits = resultSet.getInt("cost_credits");
        final int costPixels = resultSet.getInt("cost_pixels");
        final int costDiamonds = resultSet.getInt("cost_diamonds");
        final int costSeasonal = resultSet.getInt("cost_seasonal");
        final int amount = resultSet.getInt("amount");
        final boolean vip = resultSet.getBoolean("vip");
        final int limitedStack = resultSet.getInt("limited_stack");
        final int limitedSells = resultSet.getInt("limited_sells");
        final boolean offerActive = resultSet.getBoolean("offer_active");
        final String badgeId = resultSet.getString("badge_id");
        final String extraData = resultSet.getString("extradata");
        final int pageId = resultSet.getInt("page_id");

        return new CatalogItem(id, itemIds, catalogName, costCredits, costPixels,
                costDiamonds, costSeasonal, amount, vip, limitedStack, limitedSells, offerActive, badgeId,
                extraData, pageId);
    }
}
