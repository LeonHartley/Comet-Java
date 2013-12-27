package com.cometsrv.game.players.types;

import com.cometsrv.boot.Comet;
import com.cometsrv.game.GameEngine;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerStatistics {
    private int userId;
    private int achievementPoints;
    private int dailyRespects;
    private int respectPoints;

    public PlayerStatistics(ResultSet data) throws SQLException {
        this.userId = data.getInt("player_id");
        this.achievementPoints = data.getInt("achievement_score");
        this.dailyRespects = data.getInt("daily_respects");
        this.respectPoints = data.getInt("total_respect_points");
    }

    public PlayerStatistics(int userId) {
        this.userId = userId;
        this.achievementPoints = 0;
        this.respectPoints = 0;
        this.dailyRespects = 3;
    }

    public void save() {
        try {
            PreparedStatement statement = Comet.getServer().getStorage().prepare("UPDATE player_stats SET achievement_score = ?, total_respect_points = ?, daily_respects = ? WHERE player_id = " + this.userId);

            statement.setInt(1, achievementPoints);
            statement.setInt(2, respectPoints);
            statement.setInt(3, dailyRespects);
            statement.setInt(3, userId);

            statement.execute();
        } catch(SQLException e) {
            GameEngine.getLogger().error("Error while saving player statistics", e);
        }
     }

    public void incrementAchievementPoints(int amount) {
        this.achievementPoints += amount;
        this.save();
    }

    public void incrementRespectPoints(int amount) {
        this.respectPoints += amount;
        this.save();
    }

    public void decrementDailyRespects(int amount) {
        this.dailyRespects -= amount;
        this.save();
    }

    public int getDailyRespects() {
        return this.dailyRespects;
    }

    public int getRespectPoints() {
        return this.dailyRespects;
    }

    public int getAchievementPoints() {
        return this.achievementPoints;
    }
}
