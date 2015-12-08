package com.cometproject.server.storage.queries.rooms;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.rooms.bundles.types.RoomBundle;
import com.cometproject.server.game.rooms.bundles.types.RoomBundleItem;
import com.cometproject.server.game.rooms.models.types.DynamicRoomModelData;
import com.cometproject.server.storage.SqlHelper;
import com.cometproject.server.utilities.JsonFactory;
import com.google.gson.reflect.TypeToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BundleDao {
    public static void loadActiveBundles(Map<Integer, RoomBundle> bundles) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM room_bundles WHERE enabled = '1'", sqlConnection);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int bundleId = resultSet.getInt("id");
                try {
                    final String alias = resultSet.getString("alias");

                    final DynamicRoomModelData roomModelData = JsonFactory.getInstance().fromJson(
                            resultSet.getString("model_data"), DynamicRoomModelData.class);

                    final List<RoomBundleItem> bundleItems = JsonFactory.getInstance().fromJson(
                            resultSet.getString("bundle_data"),
                            new TypeToken<ArrayList<RoomBundleItem>>() {}.getType());

                    bundles.put(bundleId, new RoomBundle(bundleId, resultSet.getInt("room_id"), alias, roomModelData, bundleItems));
                } catch (Exception e) {
                    Comet.getServer().getLogger().warn("Failed to load room bundle with id: " + bundleId, e);
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

    public static void saveBundle(RoomBundle bundle) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("INSERT into room_bundles (alias, room_id, model_data, bundle_data) VALUES(?, ?, ?, ?);", sqlConnection, true);

            preparedStatement.setString(1, bundle.getAlias());
            preparedStatement.setInt(2, bundle.getRoomId());
            preparedStatement.setString(3, JsonFactory.getInstance().toJson(bundle.getRoomModelData()));
            preparedStatement.setString(4, JsonFactory.getInstance().toJson(bundle.getRoomBundleData()));

            preparedStatement.execute();
            resultSet = preparedStatement.getGeneratedKeys();

            while (resultSet.next()) {
                int bundleId = resultSet.getInt(1);
                bundle.setId(bundleId);
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }
    }
}
