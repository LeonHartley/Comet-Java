package com.cometproject.server.storage.queries.items;

import com.cometproject.server.game.rooms.items.data.MoodlightData;
import com.cometproject.server.game.rooms.items.data.MoodlightPresetData;
import com.cometproject.server.storage.SqlHelper;
import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MoodlightDao {
    public static MoodlightData getMoodlightData(int itemId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<MoodlightPresetData> presets = new ArrayList<>();
        MoodlightData data = null;

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT * FROM items_moodlight WHERE item_id = ?", sqlConnection);
            preparedStatement.setInt(1, itemId);
            resultSet = preparedStatement.executeQuery();

            Gson json = new Gson();

            if (resultSet.isBeforeFirst()) {
                if (resultSet.next()) {
                    String preset1 = resultSet.getString("preset_1");
                    String preset2 = resultSet.getString("preset_2");
                    String preset3 = resultSet.getString("preset_3");

                    if (!preset1.equals("")) {
                        presets.add(json.fromJson(preset1, MoodlightPresetData.class));
                    }
                    if (!preset2.equals("")) {
                        presets.add(json.fromJson(preset2, MoodlightPresetData.class));
                    }
                    if (!preset3.equals("")) {
                        presets.add(json.fromJson(preset3, MoodlightPresetData.class));
                    }

                    data = new MoodlightData(resultSet.getString("enabled").equals("1"), resultSet.getInt("active_preset"), presets);
                }
            } else {
                preparedStatement = SqlHelper.prepare("INSERT INTO items_moodlight (item_id,enabled,active_preset,preset_1,preset_2,preset_3) VALUES (?,?,?,?,?,?);", sqlConnection);
                preparedStatement.setInt(1, itemId);
                preparedStatement.setString(2, "0");
                preparedStatement.setString(3, "1");
                preparedStatement.setString(4, "");
                preparedStatement.setString(5, "");
                preparedStatement.setString(6, "");

                preparedStatement.execute();

                data = new MoodlightData(false, 0, presets);
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
}
