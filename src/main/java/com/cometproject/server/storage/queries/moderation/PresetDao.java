package com.cometproject.server.storage.queries.moderation;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.moderation.types.Ban;
import com.cometproject.server.storage.SqlHelper;
import javolution.util.FastMap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PresetDao {
    public static void getPresets(List<String> userPresets, List<String> roomPresets) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM moderation_presets", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                (resultSet.getString("type").equals("user") ? userPresets : roomPresets).add(resultSet.getString("message"));
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
