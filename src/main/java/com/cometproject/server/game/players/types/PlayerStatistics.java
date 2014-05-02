package com.cometproject.server.game.players.types;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.game.GameEngine;
import com.cometproject.server.storage.queries.player.PlayerDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlayerStatistics {
    private int userId;
    private int achievementPoints;
    private int dailyRespects;
    private int respectPoints;
    private int friendCount = 0;

    public PlayerStatistics(ResultSet data) throws SQLException {
        this.userId = data.getInt("player_id");
        this.achievementPoints = data.getInt("achievement_score");
        this.dailyRespects = data.getInt("daily_respects") > 3 ? 3 : data.getInt("daily_respects");
        this.respectPoints = data.getInt("total_respect_points");
    }

    public PlayerStatistics(int userId) {
        this.userId = userId;
        this.achievementPoints = 0;
        this.respectPoints = 0;
        this.dailyRespects = 3;
    }

    public void save() {
        if (!PlayerDao.updatePlayerStatistics(achievementPoints, respectPoints, dailyRespects, userId)) {
            GameEngine.getLogger().error(String.format("Error while saving player statistics for id %s", userId));
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

    public int getFriendCount() {
        try {
            if (this.friendCount == 0) {
                PreparedStatement statement = Comet.getServer().getStorage().prepare("SELECT COUNT(1) FROM messenger_friendships WHERE user_one_id = ?");

                statement.setInt(1, this.userId);

                ResultSet data = statement.executeQuery();

                while (data.next()) {
                    this.friendCount = data.getInt(1);
                }
            }
        } catch (Exception e) {
            GameEngine.getLogger().error("Error while counting friends for PlayerStatistics", e);
        }

        return this.friendCount;
    }
}
