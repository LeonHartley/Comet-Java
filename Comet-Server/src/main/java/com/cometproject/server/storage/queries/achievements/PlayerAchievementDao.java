package com.cometproject.server.storage.queries.achievements;

import com.cometproject.server.game.players.components.types.achievements.AchievementProgress;
import com.cometproject.server.storage.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PlayerAchievementDao {
    public static Map<String, AchievementProgress> getAchievementProgress(int playerId) {
        Connection sqlConnection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Map<String, AchievementProgress> achievements = new HashMap<>();

        try {
            sqlConnection = SqlHelper.getConnection();

            preparedStatement = SqlHelper.prepare("SELECT group, level, progress FROM player_achievements WHERE player_id = ?", sqlConnection);

            preparedStatement.setInt(1, playerId);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                achievements.put(resultSet.getString("group"), new AchievementProgress(resultSet.getInt("level"), resultSet.getInt("progress")));
            }
        } catch (SQLException e) {
            SqlHelper.handleSqlException(e);
        } finally {
            SqlHelper.closeSilently(resultSet);
            SqlHelper.closeSilently(preparedStatement);
            SqlHelper.closeSilently(sqlConnection);
        }

        return achievements;
    }
}
