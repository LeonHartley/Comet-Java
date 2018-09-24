package com.cometproject.server.game.players.types;

import com.cometproject.api.game.players.data.IPlayerStatistics;
import com.cometproject.server.storage.queries.player.PlayerDao;
import com.cometproject.server.storage.queries.player.messenger.MessengerDao;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.sql.ResultSet;
import java.sql.SQLException;


public class PlayerStatistics implements IPlayerStatistics {
    private int playerId;
    private int achievementPoints;

    private Player player;

    private int dailyRespects;
    private int respectPoints;

    private int scratches;

    private int helpTickets;
    private int abusiveHelpTickets;
    private int cautions;
    private int bans;

    public PlayerStatistics(ResultSet data, boolean isLogin, Player player) throws SQLException {
        if (isLogin) {
            this.playerId = data.getInt("playerId");
            this.achievementPoints = data.getInt("playerStats_achievementPoints");
            this.dailyRespects = data.getInt("playerStats_dailyRespects");
            this.respectPoints = data.getInt("playerStats_totalRespectPoints");
            this.helpTickets = data.getInt("playerStats_helpTickets");
            this.abusiveHelpTickets = data.getInt("playerStats_helpTicketsAbusive");
            this.cautions = data.getInt("playerStats_cautions");
            this.bans = data.getInt("playerStats_bans");
            this.scratches = data.getInt("playerStats_scratches");
        } else {
            this.playerId = data.getInt("player_id");
            this.achievementPoints = data.getInt("achievement_score");
            this.dailyRespects = data.getInt("daily_respects");
            this.respectPoints = data.getInt("total_respect_points");
            this.helpTickets = data.getInt("help_tickets");
            this.abusiveHelpTickets = data.getInt("help_tickets_abusive");
            this.cautions = data.getInt("cautions");
            this.bans = data.getInt("bans");
            this.scratches = data.getInt("daily_scratches");
        }

        this.player = player;
    }

    public PlayerStatistics(int userId) {
        this.playerId = userId;
        this.achievementPoints = 0;
        this.respectPoints = 0;
        this.dailyRespects = 3;
        this.scratches = 3;
        this.helpTickets = 0;
        this.abusiveHelpTickets = 0;
        this.cautions = 0;
        this.bans = 0;

        this.player = null;
    }

    public void save() {
        PlayerDao.updatePlayerStatistics(this);

        flush();
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

    public void incrementBans(int amount) {
        this.bans += amount;

        flush();
    }

    public void incrementAbusiveHelpTickets(int amount) {
        this.abusiveHelpTickets += amount;

        flush();
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public int getDailyRespects() {
        return this.dailyRespects;
    }

    @Override
    public void setDailyRespects(int points) {
        this.dailyRespects = points;
    }

    public int getRespectPoints() {
        return this.respectPoints;
    }

    public int getAchievementPoints() {
        return this.achievementPoints;
    }

    public int getFriendCount() {
        return MessengerDao.getFriendCount(this.playerId);
    }

    public int getHelpTickets() {
        return helpTickets;
    }

    public void setHelpTickets(int helpTickets) {
        this.helpTickets = helpTickets;

        flush();
    }

    public int getAbusiveHelpTickets() {
        return abusiveHelpTickets;
    }

    public void setAbusiveHelpTickets(int abusiveHelpTickets) {
        this.abusiveHelpTickets = abusiveHelpTickets;

        flush();
    }

    public int getCautions() {
        return cautions;
    }

    public void setCautions(int cautions) {
        this.cautions = cautions;

        flush();
    }

    public int getBans() {
        return bans;
    }

    public void setBans(int bans) {
        this.bans = bans;

        flush();
    }

    public void addBan() {
        this.bans = this.bans + 1;

        flush();
    }

    @Override
    public int getScratches() {
        return scratches;
    }

    @Override
    public void setScratches(int scratches) {
        this.scratches = scratches;
    }

    public JsonElement toJson() {
        final JsonObject coreObject = new JsonObject();

        coreObject.addProperty("achievementPoints", achievementPoints);
        coreObject.addProperty("dailyRespects", dailyRespects);
        coreObject.addProperty("respectPoints", respectPoints);
        coreObject.addProperty("scratches", scratches);
        coreObject.addProperty("helpTickets", helpTickets);
        coreObject.addProperty("abusiveHelpTickets", abusiveHelpTickets);
        coreObject.addProperty("cautions", cautions);
        coreObject.addProperty("bans", bans);

        return coreObject;
    }


    public Player getPlayer() {
        return player;
    }

    public void flush() {
        if (player != null) {
            this.getPlayer().flush();
        }
    }
}
