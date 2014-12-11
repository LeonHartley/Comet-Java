package com.cometproject.server.game.players.types;

import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerStatistics {
    private int userId;
    private int achievementPoints;
    private int dailyRespects;
    private int respectPoints;

    private int helpTickets;
    private int abusiveHelpTickets;
    private int cautions;
    private int bans;

    public PlayerStatistics(ResultSet data, boolean isLogin) throws SQLException {
        if (isLogin) {
            this.userId = data.getInt("playerId");
            this.achievementPoints = data.getInt("playerStats_achievementPoints");
            this.dailyRespects = data.getInt("playerStats_dailyRespects") > 3 ? 3 : data.getInt("playerStats_dailyRespects");
            this.respectPoints = data.getInt("playerStats_totalRespectPoints");
            this.helpTickets = data.getInt("playerStats_helpTickets");
            this.abusiveHelpTickets = data.getInt("playerStats_helpTicketsAbusive");
            this.cautions = data.getInt("playerStats_cautions");
            this.bans = data.getInt("playerStats_bans");
        } else {
            this.userId = data.getInt("player_id");
            this.achievementPoints = data.getInt("achievement_score");
            this.dailyRespects = data.getInt("daily_respects") > 3 ? 3 : data.getInt("daily_respects");
            this.respectPoints = data.getInt("total_respect_points");
            this.helpTickets = data.getInt("help_tickets");
            this.abusiveHelpTickets = data.getInt("help_tickets_abusive");
            this.cautions = data.getInt("cautions");
            this.bans = data.getInt("bans");
        }
    }

    public PlayerStatistics(int userId) {
        this.userId = userId;
        this.achievementPoints = 0;
        this.respectPoints = 0;
        this.dailyRespects = 3;
        this.helpTickets = 0;
        this.abusiveHelpTickets = 0;
        this.cautions = 0;
        this.bans = 0;
    }

    public void save() {
        PlayerDao.updatePlayerStatistics(achievementPoints, respectPoints, dailyRespects, userId);
    }

    public void incrementAchievementPoints(int amount) {
        this.achievementPoints += amount;
        this.save();
    }

    public void incrementCautions(int amount) {
        this.cautions += amount;
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
        return MessengerDao.getFriendCount(this.userId);
    }

    public int getHelpTickets() {
        return helpTickets;
    }

    public void setHelpTickets(int helpTickets) {
        this.helpTickets = helpTickets;
    }

    public int getAbusiveHelpTickets() {
        return abusiveHelpTickets;
    }

    public void setAbusiveHelpTickets(int abusiveHelpTickets) {
        this.abusiveHelpTickets = abusiveHelpTickets;
    }

    public int getCautions() {
        return cautions;
    }

    public void setCautions(int cautions) {
        this.cautions = cautions;
    }

    public int getBans() {
        return bans;
    }

    public void setBans(int bans) {
        this.bans = bans;
    }
}
