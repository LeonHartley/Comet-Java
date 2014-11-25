package com.cometproject.server.storage.queries.moderation;

import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PresetDao {
    public static void getPresets(List<String> userPresets, List<String> roomPresets, List<String> actionReasons) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM moderation_presets", sqlConnection);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                switch (resultSet.getString("type")) {
                    case "user":
                        userPresets.add(resultSet.getString("message"));
                        break;

                    case "room":
                        roomPresets.add(resultSet.getString("message"));
                        break;

                    case "action":
                        actionReasons.add(resultSet.getString("message"));
                        break;
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
}
